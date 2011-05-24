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
import nsc.Global
import nsc.plugins.PluginComponent
import nsc.transform.{Transform, TypingTransformers}

class GenerateMocks(plugin: BorachioPlugin, val global: Global) extends PluginComponent
  with Transform
  with TypingTransformers
{
  import global._
  import definitions.getClass

  val runsAfter = List[String]("typer")
  val phaseName = "generatemocks"

  val MockAnnotation = definitions.getClass("com.borachio.mocks")

  def newTransformer(unit: CompilationUnit) = new MocksTransformer(unit)

  class MocksTransformer(unit: CompilationUnit) extends TypingTransformer(unit) {
    
    override def transform(tree: Tree) = {
      val newTree = tree match {
        case ClassDef(mods, name, tparams, impl) =>
          if (tree.symbol hasAnnotation MockAnnotation) {
            log(name.toString +" has the @mocks annotation")
          }
          tree
        
        case _ => tree
      }
      super.transform(newTree)
    }
  }
}
