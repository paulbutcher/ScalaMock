package com.paulbutcher.test

// Copyright (c) 2011-2015 ScalaMock Contributors (https://github.com/paulbutcher/ScalaMock/graphs/contributors)
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

import com.paulbutcher.test.*
import org.scalamock.scalatest.MockFactory
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class MockTestScala3 extends AnyFreeSpec with MockFactory with Matchers {

  autoVerify = false

  "In Scala 3, Mocks should" - {

    "mock type constructor arguments" in {
      class WithTC[TC[_]](tc: TC[Int])
      type ID[A] = A
      "mock[WithTC[List]]" should compile
      pendingUntilFixed {
        "mock[WithTC[ID]]" should compile
      }
    }

    "mock generic arguments" in {
      class WithGeneric[T](t: T)
      "mock[WithGeneric[String]]" should compile
      "mock[WithGeneric[Int]]" should compile
    }

    "mock type constructor context bounds" in {
      trait Async[F[_]]
      class A[F[_]: Async](val b: B[F])
      class B[F[_]: Async](val c: C[F])
      trait C[F[_]]
      "mock[A[List]]" should compile
      "mock[B[List]]" should compile
      "mock[C[List]]" should compile
    }
  }
}