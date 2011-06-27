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
import scala.collection.mutable.ListBuffer

class GenerateMocks(plugin: BorachioPlugin, val global: Global) extends PluginComponent {
  import global._
  import definitions._
  
  val runsAfter = List[String]("typer")
  val phaseName = "generatemocks"
  
  val mocks = new ListBuffer[(String, String)]

  lazy val MockAnnotation = definitions.getClass("com.borachio.annotation.mock")
  lazy val mockOutputDirectory = plugin.mockOutputDirectory.get
  lazy val testOutputDirectory = plugin.testOutputDirectory.get
  
  def newPhase(prev: Phase) = new StdPhase(prev) {
    def apply(unit: CompilationUnit) {
      if (plugin.mockOutputDirectory.isDefined) {
        if (plugin.testOutputDirectory.isDefined) {
          createOutputDirectories
          new ForeachTreeTraverser(findMockAnnotations).traverse(unit.body)
          generateMockFactory
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
  
  def generateMockFactory() {
    val writer = new FileWriter(new File(testOutputDirectory, "GeneratedMockFactory.scala"))
    writer.write(mockFactory)
    writer.close
  }
  
  def mockFactory = {
    "package com.borachio.generated\n\n"+
    "trait GeneratedMockFactory extends com.borachio.GeneratedMockFactoryBase { self: com.borachio.AbstractMockFactory =>\n"+
      (mocks map { case (target, mock) => mockFactoryEntry(target, mock) }).mkString("\n") +"\n"+
    "}"
  }
  
  def mockFactoryEntry(target: String, mock: String) =
    "  implicit def toMock(m: "+ target +") = m.asInstanceOf["+ mock +"]"
  
  def createOutputDirectories {
    new File(mockOutputDirectory).mkdirs
    new File(testOutputDirectory).mkdirs
  }

  class MockClass(classSymbol: Symbol) {
    
    def generate() {
      log("Creating mock for: "+ classSymbol)

      if (isClass) {
        val mockWriter = new FileWriter(new File(mockOutputDirectory, mockFilename))
        mockWriter.write(mock)
        mockWriter.close
      }
      
      val testWriter = new FileWriter(new File(testOutputDirectory, mockFilename))
      testWriter.write(test)
      testWriter.close
      
      mocks += (qualify(className) -> qualify(mockTraitOrClassName))
    }

    lazy val mockFilename = qualify(className) +".scala"  

    lazy val mock =
      packageStatement +"\n\n"+
        classDeclaration +" {\n"+
          mockMethods +"\n\n"+
          mockMembers +"\n"+
        "}"
        
    lazy val test =
      packageStatement +"\n\n"+
        mockTraitOrClassDeclaration +" {\n"+
          expectForwarders +"\n"+ (
            if (!isClass) {
              "\n"+
              mockMethods +"\n\n"+
              mockMembers +"\n"
            } else {
              ""
            }
          ) +
        "}\n"
        
    lazy val isClass = !classSymbol.isTrait

    lazy val packageStatement = "package "+ packageName

    lazy val classDeclaration = 
      "class "+ className +"(factory: com.borachio.AbstractMockFactory) extends "+ mockTraitOrClassName

    lazy val mockMethods = (methodsToMock map mockMethod _).mkString("\n")

    lazy val expectForwarders = "  val expects = new {\n"+
        "    lazy val clazz = "+ mockTraitOrClassName +".this.getClass\n\n"+
        (methodsToMock map expectForwarder _).mkString("\n") +"\n\n"+
        (methodsToMock map cachedMockMethod _).mkString("\n") + "\n"+
      "  }"

    lazy val mockMembers = (methodsToMock map mockMember _).mkString("\n")
    
    lazy val mockTraitOrClassDeclaration = 
      if (isClass)
        "trait "+ mockTraitOrClassName
      else
        "class "+ mockTraitOrClassName +"(factory: com.borachio.AbstractMockFactory) extends "+ className
        
    lazy val packageName = classSymbol.enclosingPackage.fullName.toString

    lazy val className = classSymbol.name.toString
    
    lazy val mockTraitOrClassName = "Mock$"+ className

    lazy val methodsToMock = classSymbol.info.nonPrivateMembers filter { s => 
        s.isMethod && !s.isMemberOf(ObjectClass)
      }
      
    lazy val mockMethodNames = (methodsToMock.zipWithIndex map { case (m, i) => (m -> ("mock$"+ i)) }).toMap
      
    def qualify(name: String) = packageName +"."+ name
    
    def mockMethod(method: Symbol): String =
      method.info match {
        case MethodType(params, result) => mockMethod(method, Some(params))
        case NullaryMethodType(result) => mockMethod(method, None)
        case PolyType(params, result) => "  //"+ method +" // Borachio doesn't (yet) handle type-parameterised methods"
        case _ => sys.error("Borachio plugin: Don't know how to handle "+ method)
      }

    def mockMethod(method: Symbol, params: Option[List[Symbol]]) =
      mockDeclaration(method, params) +" = "+ (
          if (method.isConstructor)
            mockBodyConstructor(method, params)
          else
            mockBodyNormal(method, params)
        )
        
    def mockDeclaration(method: Symbol, params: Option[List[Symbol]]) = 
      "  def "+ method.name.decode + mockParams(params)
        
    def mockParams(params: Option[List[Symbol]]) = params match {
        case Some(ps) => (ps map parameterDeclaration _).mkString("(", ", ", ")")
        case None => ""
      }
      
    def parameterDeclaration(parameter: Symbol) = parameter.name +": "+ parameter.tpe
    
    def mockBodyConstructor(method: Symbol, params: Option[List[Symbol]]) =
      "{ this(null); "+ mockBodyNormal(method, params) +" }"
    
    def mockBodyNormal(method: Symbol, params: Option[List[Symbol]]) = mockMethodName(method) + forwardParams(params)

    def mockMethodName(method: Symbol) = mockMethodNames(method)

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
      if (method.isConstructor)
        expectForwarderConstructor(method, params, result)
      else
        expectForwarderNormal(method, params, result)
        
    def expectForwarderConstructor(method: Symbol, params: Option[List[Symbol]], result: Type) =
      forwarderDeclarationConstructor(method, params) +" = "+ forwarderBody(method, params)
      
    def expectForwarderNormal(method: Symbol, params: Option[List[Symbol]], result: Type) =
      forwarderDeclarationNormal(method, params, result) +" = "+ forwarderBody(method, params)
      
    def forwarderDeclarationConstructor(method: Symbol, params: Option[List[Symbol]]) =
      "    def newInstance"+ forwarderParams(params)
        
    def forwarderDeclarationNormal(method: Symbol, params: Option[List[Symbol]], result: Type) =
      "    def "+ method.name.decode + forwarderParams(params) +": "+ expectationType(result)
        
    def forwarderParams(params: Option[List[Symbol]]) = params match {
        case Some(ps) => (ps map forwarderParam _).mkString("(", ", ", ")")
        case None => ""
      }
      
    def forwarderParam(parameter: Symbol) = parameter.name +": "+ "com.borachio.MockParameter["+ parameter.tpe +"]"
    
    def expectationType(result: Type) = "com.borachio.TypeSafeExpectation["+ result +"]"
        
    def forwarderBody(method: Symbol, params: Option[List[Symbol]]) =
      mockMethodName(method) +".expects"+ forwardParams(params)
    
    def cachedMockMethod(method: Symbol): String =
      method.info match {
        case MethodType(params, result) => cachedMockMethod(method, params, result)
        case NullaryMethodType(result) => cachedMockMethod(method, Nil, result)
        case PolyType(params, result) => "  //"+ method +" // Borachio doesn't (yet) handle type-parameterised methods"
        case _ => sys.error("Borachio plugin: Don't know how to handle "+ method)
      }
    
    def cachedMockMethod(method: Symbol, params: List[Symbol], result: Type) =
      "    lazy val "+ mockMethodName(method) +" = "+ cacheLookup(method, params, result)
      
    def cacheLookup(method: Symbol, params: List[Symbol], result: Type) =
      "clazz.getMethod(\""+ mockMethodName(method) +"\").invoke("+ mockTraitOrClassName +".this).asInstanceOf["+ 
        mockFunction(params, result) +"]"

    def mockMember(method: Symbol): String = 
      method.info match {
        case MethodType(params, result) => mockMember(method, params, result)
        case NullaryMethodType(result) => mockMember(method, Nil, result)
        case PolyType(params, result) => "  //"+ method +" // Borachio doesn't (yet) handle type-parameterised methods"
        case _ => sys.error("Borachio plugin: Don't know how to handle "+ method)
      }

    def mockMember(method: Symbol, params: List[Symbol], result: Type) = "  protected lazy val "+ 
      mockMethodName(method) +" = new "+ mockFunction(params, result) +"(factory, Symbol(\""+ method.name +"\"))"

    def mockFunction(params: List[Symbol], result: Type) =
      "com.borachio.MockFunction"+ params.length +"["+ (paramTypes(params) :+ result).mkString(", ") +"]"

    def paramTypes(params: List[Symbol]) = params map (_.tpe)
  }
}
