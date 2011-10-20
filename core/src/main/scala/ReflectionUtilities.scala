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

package org.scalamock

object ReflectionUtilities {

  def getUnmockedClass(clazz: Class[_], name: String) = {
    val classLoader = clazz.getClassLoader
    val method = classLoader.getClass.getMethod("loadClassNormal", classOf[String])
    method.invoke(classLoader, name).asInstanceOf[Class[_]]
  }

  def getBooleanField(clazz: Class[_], name: String) = clazz.getField(name).getBoolean(null)
  def getByteField(clazz: Class[_], name: String) = clazz.getField(name).getByte(null)
  def getCharField(clazz: Class[_], name: String) = clazz.getField(name).getChar(null)
  def getDoubleField(clazz: Class[_], name: String) = clazz.getField(name).getDouble(null)
  def getFloatField(clazz: Class[_], name: String) = clazz.getField(name).getFloat(null)
  def getIntField(clazz: Class[_], name: String) = clazz.getField(name).getInt(null)
  def getLongField(clazz: Class[_], name: String) = clazz.getField(name).getLong(null)
  def getShortField(clazz: Class[_], name: String) = clazz.getField(name).getShort(null)
  def getObjectField(clazz: Class[_], name: String) = clazz.getField(name).get(null)
}