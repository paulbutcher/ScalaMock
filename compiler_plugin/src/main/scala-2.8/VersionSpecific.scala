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
import nsc._

trait VersionSpecific { this: GenerateMocks =>
  import global._
  import definitions._
  
  def handleMethodByType(method: Symbol, tpe: Type)(handler: (Symbol, Option[List[Symbol]], Type) => String) =
    tpe match {
      case MethodType(params, result) => handler(method, Some(params), result)
      case PolyType(params, result) if params.isEmpty => handler(method, None, result)
      case PolyType(params, result) => "  //"+ method +" // ScalaMock doesn't (yet) handle type-parameterised methods"
      case _ => throw new RuntimeException("ScalaMock plugin: Don't know how to handle "+ method)
    }
}