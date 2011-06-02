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
  lazy val outputDirectory = plugin.outputDirectory.get
  
  def newPhase(prev: Phase) = new StdPhase(prev) {
    def apply(unit: CompilationUnit) {
      if (plugin.outputDirectory.isDefined) {
        createOutputDirectory
        new ForeachTreeTraverser(findMocks).traverse(unit.body)
      }
    }
  }
  
  def findMocks(tree: Tree) {
    tree match {
      case ClassDef(_, _, _, _) =>
        if (tree.hasSymbol && (tree.symbol hasAnnotation MockAnnotation))
          new MockClass(tree.symbol).generate

      case _ =>
    }
  }
  
  def createOutputDirectory {
    new File(outputDirectory).mkdirs
  }

  class MockClass(classSymbol: Symbol) {
    
    def generate() {
      log("Creating mock for: "+ classSymbol)
      val writer = new FileWriter(new File(outputDirectory, mockFilename))
      writer.write(mock)
      writer.close
    }

    lazy val mockFilename = packageName +"."+ className +".scala"  

    lazy val mock =
      packageStatement + "\n\n" +
        classDeclaration + " {\n" +
          mockMethods + "\n\n" +
          expectForwarders + "\n\n" +
          mockMembers + "\n" +
        "}\n"

    lazy val packageStatement = "package "+ packageName

    lazy val classDeclaration = 
      "class "+ className +"(factory: com.borachio.AbstractMockFactory)"+ parents.mkString(" extends ", " with ", "")

    lazy val mockMethods = (methodsToMock map mockMethod _).mkString("\n")

    lazy val expectForwarders = "  val expects = new {\n"+
        (methodsToMock map expectForwarder _).mkString("\n") +
      "\n  }"

    lazy val mockMembers = (methodsToMock map mockMember _).mkString("\n")

    lazy val packageName = classSymbol.enclosingPackage.fullName.toString

    lazy val className = "Mock"+ classSymbol.name

    lazy val parents = classSymbol.info.parents filter { _.typeSymbol != ScalaObjectClass }

    lazy val methodsToMock = classSymbol.info.nonPrivateMembers filter { s => s.isMethod && !s.isConstructor && !s.isFinal }

    def mockMethod(method: Symbol): String =
      method.info match {
        case MethodType(params, result) => mockMethod(method.name, params)
        case NullaryMethodType(result) => "  //"+ method +" // Borachio doesn't (yet) handle nullary methods"
        case PolyType(params, result) => "  //"+ method +" // Borachio doesn't (yet) handle type-parameterised methods"
        case _ => sys.error("Borachio plugin: Don't know how to handle "+ method)
      }

    def mockMethod(name: Name, params: List[Symbol]) =
      mockDeclaration(name, params) +" = "+ mockBody(name, params)

    def mockDeclaration(name: Name, params: List[Symbol]) = 
      "  override def "+ name.decode + (params map parameterDeclaration _).mkString("(", ", ", ")")

    def parameterDeclaration(parameter: Symbol) = parameter.name +": "+ parameter.tpe.typeSymbol.fullName

    def mockBody(name: Name, params: List[Symbol]) = mockMethodName(name) + forwardParams(params)

    def mockMethodName(name: Name) = "mock$"+ name

    def forwardParams(params: List[Symbol]) = (params map (_.name)).mkString("(", ", ", ")")

    def expectForwarder(method: Symbol): String =
      method.info match {
        case MethodType(params, result) => expectForwarder(method.name, params)
        case NullaryMethodType(result) => "  //"+ method +" // Borachio doesn't (yet) handle nullary methods"
        case PolyType(params, result) => "  //"+ method +" // Borachio doesn't (yet) handle type-parameterised methods"
        case _ => sys.error("Borachio plugin: Don't know how to handle "+ method)
      }

    def expectForwarder(name: Name, params: List[Symbol]) =
      "  "+ mockDeclaration(name, params) +" = "+ mockMethodName(name) + forwardParams(params)

    def mockMember(method: Symbol): String = 
      method.info match {
        case MethodType(params, result) => mockMember(method.name, params, result)
        case NullaryMethodType(result) => "  //"+ method +" // Borachio doesn't (yet) handle nullary methods"
        case PolyType(params, result) => "  //"+ method +" // Borachio doesn't (yet) handle type-parameterised methods"
        case _ => sys.error("Borachio plugin: Don't know how to handle "+ method)
      }

    def mockMember(name: Name, params: List[Symbol], result: Type) =
      "  private val "+ mockMethodName(name) + mockFunction(params, result) +"(factory, "+ Symbol(name.toString) +")"

    def mockFunction(params: List[Symbol], result: Type) =
      " = new com.borachio.MockFunction"+ params.length +"["+ 
        (paramTypes(params) :+ result.typeSymbol.fullName).mkString(", ") +"]"

    def paramTypes(params: List[Symbol]) = params map (_.tpe.typeSymbol.fullName)
  }
}
