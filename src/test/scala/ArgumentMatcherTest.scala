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

package com.borachio

import org.scalatest.Suite

class ArgumentMatcherTest extends Suite {

  def testBasic {
    assert(new ArgumentMatcher(42, "foo") == (42, "foo"))
    assert(!(new ArgumentMatcher(42, "foo") == (42, "bar")))
  }
  
  def testMatchAny {
    assert(new ArgumentMatcher(42, new MatchAny) == (42, "foo"))
    assert(!(new ArgumentMatcher(42, new MatchAny) == (43, "foo")))
  }
  
  def testMatchEpsilon {
    assert(new ArgumentMatcher("foo", new MatchEpsilon(42)) == ("foo", 42.0001))
    assert(!(new ArgumentMatcher("foo", new MatchEpsilon(42)) == ("foo", 42.01)))
  }
}
