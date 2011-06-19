// Copyright (c) 2011 Paul Butcher
// 
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package com.borachio.plugin

import scala.tools.nsc
import nsc._
import nsc.plugins.PluginComponent

import java.io.{File, FileWriter}

class GenerateMocks(plugin: BorachioPlugin, val global: Global) extends PluginComponent {
  import global._
  import definitions._
  
  val runsAfter = List[String]("typer")
  val phaseName = "generatemocks"

  lazy val MockAnnotation = definitions.getClass("com.borachio.annotation.mock")
  lazy val mockOutputDirectory = plugin.mockOutputDirectory.get
  lazy val testOutputDirectory = plugin.testOutputDirectory.get
  
  def newPhase(prev: Phase) = new StdPhase(prev) {
    def apply(unit: CompilationUnit) {
      if (plugin.mockOutputDirectory.isDefined) {
        if (plugin.testOutputDirectory.isDefined) {
          createOutputDirectories
          new ForeachTreeTraverser(findMockAnnotations).traverse(unit.body)
        } else {
          globalError("Both -P:borachio:generatemocks and -P:borachio:generatetest must be given")
        }
      }
    }
  }
  
  def findMockAnnotations(tree: Tree) {
    tree match {
      case ClassDef(_, _, _, _) if tree.hasSymbol =>
        for {
          AnnotationInfo(tp, args, _) <- tree.symbol.annotations if tp.typeSymbol == MockAnnotation
          Literal(const) <- args
        } new MockClass(const.typeValue.typeSymbol).generate
          
      case _ =>
    }
  }
  
  def createOutputDirectories {
    new File(mockOutputDirectory).mkdirs
    new File(testOutputDirectory).mkdirs
  }

  class MockClass(classSymbol: Symbol) {
    
    def generate() {
      log("Creating mock for: "+ classSymbol)

      val mockWriter = new FileWriter(new File(mockOutputDirectory, mockFilename))
      mockWriter.write(mock)
      mockWriter.close
      
      val testWriter = new FileWriter(new File(testOutputDirectory, mockFilename))
      testWriter.write(test)
      testWriter.close
    }

    lazy val mockFilename = packageName +"."+ className +".scala"  

    lazy val mock =
      packageStatement +"\n\n"+
        classDeclaration +" {\n"+
          mockMethods +"\n\n"+
          mockMembers +"\n"+
        "}"
        
    lazy val test =
      packageStatement +"\n\n"+
        mockTraitDeclaration +" {\n"+
          factoryDefinition +"\n\n"+
          expectForwarders +"\n"+
        "}\n"

    lazy val packageStatement = "package "+ packageName

    lazy val classDeclaration = "class "+ className +" extends "+ mockTraitName

    lazy val mockMethods = (methodsToMock map mockMethod _).mkString("\n")

    lazy val expectForwarders = "  val expects = new {\n"+
        "    lazy val clazz = "+ mockTraitName +".this.getClass\n\n"+
        (methodsToMock map expectForwarder _).mkString("\n") +"\n\n"+
        (methodsToMock map cachedMockMethod _).mkString("\n") + "\n"+
      "  }"

    lazy val mockMembers = (methodsToMock map mockMember _).mkString("\n")
    
    lazy val mockTraitDeclaration = "trait "+ mockTraitName
    
    lazy val factoryDefinition = "  var factory: com.borachio.AbstractMockFactory = _"
    
    lazy val packageName = classSymbol.enclosingPackage.fullName.toString

    lazy val className = classSymbol.name
    
    lazy val mockTraitName = "Mock$"+ className

    lazy val methodsToMock = classSymbol.info.nonPrivateMembers filter { s => 
        s.isMethod && !s.isConstructor && !s.isMemberOf(ObjectClass)
      }
      
    def mockMethod(method: Symbol): String =
      method.info match {
        case MethodType(params, result) => mockMethod(method, Some(params))
        case NullaryMethodType(result) => mockMethod(method, None)
        case PolyType(params, result) => "  //"+ method +" // Borachio doesn't (yet) handle type-parameterised methods"
        case _ => sys.error("Borachio plugin: Don't know how to handle "+ method)
      }

    def mockMethod(method: Symbol, params: Option[List[Symbol]]) =
      mockDeclaration(method, params) +" = "+ mockBody(method.name, params)

    def mockDeclaration(method: Symbol, params: Option[List[Symbol]]) = 
      "  def "+ method.name.decode + mockParams(params)
        
    def mockParams(params: Option[List[Symbol]]) = params match {
        case Some(ps) => (ps map parameterDeclaration _).mkString("(", ", ", ")")
        case None => ""
      }
      
    def parameterDeclaration(parameter: Symbol) = parameter.name +": "+ parameter.tpe
    
    def mockBody(name: Name, params: Option[List[Symbol]]) = mockMethodName(name) + forwardParams(params)

    def mockMethodName(name: Name) = "mock$"+ name

    def forwardParams(params: Option[List[Symbol]]) = params match {
        case Some(ps) => (ps map (_.name)).mkString("(", ", ", ")")
        case None => "()"
      }

    def expectForwarder(method: Symbol): String =
      method.info match {
        case MethodType(params, result) => expectForwarder(method, Some(params), result)
        case NullaryMethodType(result) => expectForwarder(method, None, result)
        case PolyType(params, result) => "  //"+ method +" // Borachio doesn't (yet) handle type-parameterised methods"
        case _ => sys.error("Borachio plugin: Don't know how to handle "+ method)
      }

    def expectForwarder(method: Symbol, params: Option[List[Symbol]], result: Type) =
      forwarderDeclaration(method, params) +": "+ expectationType(result) +" = "+ 
        mockMethodName(method.name) +".expects"+ forwardParams(params)
        
    def forwarderDeclaration(method: Symbol, params: Option[List[Symbol]]) =
      "    def "+ method.name.decode + forwarderParams(params)
        
    def forwarderParams(params: Option[List[Symbol]]) = params match {
        case Some(ps) => (ps map forwarderParam _).mkString("(", ", ", ")")
        case None => ""
      }
      
    def forwarderParam(parameter: Symbol) = parameter.name +": "+ "com.borachio.MockParameter["+ parameter.tpe +"]"
    
    def expectationType(result: Type) = "com.borachio.TypeSafeExpectation["+ result +"]"
    
    def cachedMockMethod(method: Symbol): String =
      method.info match {
        case MethodType(params, result) => cachedMockMethod(method.name, params, result)
        case NullaryMethodType(result) => cachedMockMethod(method.name, Nil, result)
        case PolyType(params, result) => "  //"+ method +" // Borachio doesn't (yet) handle type-parameterised methods"
        case _ => sys.error("Borachio plugin: Don't know how to handle "+ method)
      }
    
    def cachedMockMethod(name: Name, params: List[Symbol], result: Type) =
      "    lazy val "+ mockMethodName(name) +" = "+ cacheLookup(name, params, result)
      
    def cacheLookup(name: Name, params: List[Symbol], result: Type) =
      "clazz.getMethod(\""+ mockMethodName(name) +"\").invoke("+ mockTraitName +".this).asInstanceOf["+ 
        mockFunction(params, result) +"]"

    def mockMember(method: Symbol): String = 
      method.info match {
        case MethodType(params, result) => mockMember(method.name, params, result)
        case NullaryMethodType(result) => mockMember(method.name, Nil, result)
        case PolyType(params, result) => "  //"+ method +" // Borachio doesn't (yet) handle type-parameterised methods"
        case _ => sys.error("Borachio plugin: Don't know how to handle "+ method)
      }

    def mockMember(name: Name, params: List[Symbol], result: Type) = "  protected lazy val "+ 
      mockMethodName(name) +" = new "+ mockFunction(params, result) +"(factory, "+ Symbol(name.toString) +")"

    def mockFunction(params: List[Symbol], result: Type) =
      "com.borachio.MockFunction"+ params.length +"["+ (paramTypes(params) :+ result).mkString(", ") +"]"

    def paramTypes(params: List[Symbol]) = params map (_.tpe)
  }
}
