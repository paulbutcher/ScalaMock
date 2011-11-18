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

package org.scalamock.plugin

import scala.tools.nsc
import nsc.Global
import nsc.plugins.{Plugin, PluginComponent}

class ScalaMockPlugin(val global: Global) extends Plugin {
  import global._

  val name = "scalamock"
  val description = "support for the @mock annotation"

  val components = List[PluginComponent](
    new GenerateMocks(this, global)
  )
  
  var mockOutputDirectory: Option[String] = None
  var testOutputDirectory: Option[String] = None

  override def processOptions(options: List[String], error: String => Unit) {
    for (option <- options) {
      if (option.startsWith("generatemocks:")) {
        mockOutputDirectory = Some(option.substring("generatemocks:".length))
      } else if (option.startsWith("generatetest:")) {
        testOutputDirectory = Some(option.substring("generatetest:".length))
      } else {
        error("Option not understood: "+ option)
      }
    }
  }
  
  override val optionsHelp: Option[String] = Some(
    "  -P:scalamock:generatemocks:<path>  Generate mock objects in directory <path>\n"+
    "  -P:scalamock:generatetest:<path>   Generate test code in directory <path>")
}
