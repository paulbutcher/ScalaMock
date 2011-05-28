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

class GenerateMocks(plugin: BorachioPlugin, val global: Global) extends PluginComponent {
  import global._
  import definitions.getClass

  val runsAfter = List[String]("typer")
  val phaseName = "generatemocks"

  val MockAnnotation = definitions.getClass("com.borachio.annotation.mock")
  
  def newPhase(prev: Phase) = new StdPhase(prev) {
    def apply(unit: CompilationUnit) {
      new ForeachTreeTraverser(findMocks).traverse(unit.body)
    }
  }
  
  def findMocks(tree: Tree) {
    tree match {
      case ClassDef(mods, name, tparams, impl) =>
        if (tree.hasSymbol && (tree.symbol hasAnnotation MockAnnotation)) {
          log(name.toString +" has the @mock annotation")

          mockClass(tree.symbol)
        }
      case _ =>
    }
  }
  
  def mockClass(symbol: Symbol) {
    outputPackage(symbol)

    val methods = methodsToMock(symbol)
    methods foreach outputMethod _
  }
  
  def methodsToMock(symbol: Symbol) = symbol.info.nonPrivateMembers filter { s => s.isMethod && !s.isConstructor }
  
  def outputPackage(symbol: Symbol) {
    log(symbol.enclosingPackage)
  }
  
  def outputMethod(method: Symbol) {
    method.info match {
      case MethodType(params, result) => log(mockMethod(method.name, params, result))
      case NullaryMethodType(result) => log("Borachio doesn't (yet) handle nullary methods")
      case PolyType(params, result) => log("Borachio doesn't (yet) handle type-parameterised methods")
      case _ => sys.error("Borachio plugin: Don't know how to handle "+ method)
    }
  }
  
  def mockMethod(name: Name, params: List[Symbol], result: Type) =
    mockDeclaration(name, params) +" = "+ mockBody(name, params, result)
    
  def mockDeclaration(name: Name, params: List[Symbol]) = 
    "  def "+ name.decode + (params map parameterDeclaration _).mkString("(", ", ", ")")
    
  def mockBody(name: Name, params: List[Symbol], result: Type) =
    "mocks("+ Symbol(name.toString) +")("+ forwardParams(params) +").asInstanceOf["+ result +"]"
    
  def forwardParams(params: List[Symbol]) = 
    (params map (_.name +".asInstanceOf[AnyRef]")).mkString("Array[AnyRef](", ", ", ")")
  
  def parameterDeclaration(parameter: Symbol) = parameter.name +": "+ parameter.tpe
}
