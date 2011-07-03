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
  
  val parameterTypes = new ListBuffer[String]

  lazy val MockAnnotation = definitions.getClass("com.borachio.annotation.mock")
  lazy val mockOutputDirectory = plugin.mockOutputDirectory.get
  lazy val testOutputDirectory = plugin.testOutputDirectory.get
  
  def newPhase(prev: Phase) = new StdPhase(prev) {
    def apply(unit: CompilationUnit) {
      if (plugin.mockOutputDirectory.isDefined) {
        if (plugin.testOutputDirectory.isDefined) {
          createOutputDirectories
          new ForeachTreeTraverser(findMockAnnotations).traverse(unit.body)
          generateExtra
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
  
  def generateExtra() {
    generateMockFactory()
    generateMockParameterTypes()
  }
  
  def generateMockFactory() {
    val writer = new FileWriter(new File(testOutputDirectory, "GeneratedMockFactory.scala"))
    writer.write(mockFactory)
    writer.close
  }
  
  def mockFactory =
    "package com.borachio.generated\n\n"+
    "trait GeneratedMockFactory extends com.borachio.GeneratedMockFactoryBase { self: com.borachio.AbstractMockFactory =>\n"+
      (mocks map { case (target, mock) => mockFactoryEntry(target, mock) }).mkString("\n") +"\n\n"+
      (parameterTypes map (parameterConverter _)).mkString("\n") +"\n"+
    "}"
  
  def mockFactoryEntry(target: String, mock: String) =
    "  implicit def toMock(m: "+ target +") = m.asInstanceOf["+ mock +"]"
    
  def parameterConverter(typeName: String) = {
    val parameterTypeName = mockParameterName(typeName)
    "  protected implicit def to"+ typeName +"(v: "+ typeName +") = new "+ parameterTypeName +"(v)\n"+
      "  protected implicit def to"+ typeName +"(v: com.borachio.MatchAny) = new "+ parameterTypeName +"(v)\n"
  }
    
  def mockParameterName(typeName: String) = "com.borachio.MockParameter"+ typeName
    
  def generateMockParameterTypes() {
    val writer = new FileWriter(new File(testOutputDirectory, "GeneratedParameterTypes.scala"))
    writer.write(mockParameterTypes)
    writer.close
  }
    
  def mockParameterTypes = 
    "package com.borachio\n\n"+
    (parameterTypes map (generatedMockParameterType _)).mkString("\n\n")
  
  def generatedMockParameterType(typeName: String) =
    "class MockParameter"+ typeName +"(value: AnyRef) extends com.borachio.MockParameter["+ typeName +"](value) {\n"+
      "  def this(v: "+ typeName +") = this(v.asInstanceOf[AnyRef])\n"+
      "  def this(v: MatchAny) = this(v.asInstanceOf[AnyRef])\n"+
    "}"
  
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
      
      mocks += (qualifiedClassName -> qualify(mockTraitOrClassName))
    }

    lazy val mockFilename = qualifiedClassName +".scala"  

    lazy val mock =
      packageStatement +"\n\n"+
        classDeclaration +" {\n\n"+
          factoryReference +"\n\n"+
          forwardTo +"\n\n"+
          mockMethods +"\n\n"+
          mockMembers +"\n"+
        "}"
        
    lazy val test =
      packageStatement +"\n\n"+
        mockTraitOrClassDeclaration +" {\n\n"+
          expectForwarders +"\n"+ (
            if (!isClass) {
              "\n"+
              factoryReference +"\n\n"+
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
      "class "+ className +"(dummy: com.borachio.MockConstructorDummy) extends "+ mockTraitOrClassName
      
    lazy val factoryReference = 
      "  val factory = {\n"+
      "    val classLoader = getClass.getClassLoader\n"+
      "    val method = classLoader.getClass.getMethod(\"getFactory\")\n"+
      "    method.invoke(classLoader).asInstanceOf[com.borachio.AbstractMockFactory]\n"+
      "  }"
      
    lazy val forwardTo = "  var forwardTo: Option["+ className +"] = None"

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
        "class "+ mockTraitOrClassName +"(dummy: com.borachio.MockConstructorDummy) extends "+ className
        
    lazy val packageName = classSymbol.enclosingPackage.fullName.toString

    lazy val className = classSymbol.name.toString
    
    lazy val mockTraitOrClassName = "Mock$"+ className

    lazy val methodsToMock = classSymbol.info.nonPrivateMembers filter { s => 
        s.isMethod && !s.isMemberOf(ObjectClass)
      }
      
    lazy val mockMethodNames = (methodsToMock.zipWithIndex map { case (m, i) => (m -> ("mock$"+ i)) }).toMap
    
    lazy val qualifiedClassName = qualify(className)
      
    def qualify(name: String) = packageName +"."+ name
    
    def mockMethod(method: Symbol): String =
      method.info match {
        case MethodType(params, result) => mockMethod(method, Some(params), result)
        case NullaryMethodType(result) => mockMethod(method, None, result)
        case PolyType(params, result) => "  //"+ method +" // Borachio doesn't (yet) handle type-parameterised methods"
        case _ => sys.error("Borachio plugin: Don't know how to handle "+ method)
      }
      
    def mockMethod(method: Symbol, params: Option[List[Symbol]], result: Type) =
      if (method.isConstructor)
        mockMethodConstructor(method, params)
      else
        mockMethodNormal(method, params, result)
        
    def mockMethodConstructor(method: Symbol, params: Option[List[Symbol]]) =
      mockDeclaration(method, params) +" = "+ mockBodyConstructor(method, params)

    def mockMethodNormal(method: Symbol, params: Option[List[Symbol]], result: Type) =
      mockDeclaration(method, params) +": "+ result +" = "+ mockBodyNormal(method, params)
        
    def mockDeclaration(method: Symbol, params: Option[List[Symbol]]) = 
      "  def "+ method.name.decode + mockParams(params)
        
    def mockParams(params: Option[List[Symbol]]) = params match {
        case Some(ps) => (ps map parameterDeclaration _).mkString("(", ", ", ")")
        case None => ""
      }
      
    def parameterDeclaration(parameter: Symbol) = parameter.name +": "+ parameter.tpe
    
    def mockBodyConstructor(method: Symbol, params: Option[List[Symbol]]) = "{\n"+
      "    this(new com.borachio.MockConstructorDummy)\n"+
      "    val mock = "+ mockBodySimple(method, params) +"\n"+
      "    if (mock != null) {\n"+
      "      forwardTo = Some(mock)\n"+
      "    } else {\n"+
      "      val classLoader = getClass.getClassLoader\n"+
      "      val method = classLoader.getClass.getMethod(\"loadClassNormal\", classOf[String])\n"+
      "      val clazz = method.invoke(classLoader, \""+ qualifiedClassName +"\").asInstanceOf[Class[_]]\n"+
      "      val constructor = clazz.getConstructor("+ constructorParamTypes(params) +")\n"+
      "      forwardTo = Some(constructor.newInstance"+ forwardConstructorParams(params) +".asInstanceOf["+ qualifiedClassName +"])\n"+
      "    }\n"+
      "  }"
      
    def mockBodyNormal(method: Symbol, params: Option[List[Symbol]]) = 
      if (isClass)
        "if (forwardTo.isDefined) forwardTo.get."+ method.name + 
          forwardParamsWithNull(params) +" else "+ mockBodySimple(method, params)
      else
        mockBodySimple(method, params)
        
    def mockBodySimple(method: Symbol, params: Option[List[Symbol]]) = mockMethodName(method) + forwardParams(params)
    
    def mockMethodName(method: Symbol) = mockMethodNames(method)

    def forwardParams(params: Option[List[Symbol]]) = params match {
        case Some(ps) => (ps map (_.name)).mkString("(", ", ", ")")
        case None => "()"
      }
      
    def forwardParamsWithNull(params: Option[List[Symbol]]) = params match {
        case Some(ps) => (ps map (_.name)).mkString("(", ", ", ")")
        case None => ""
      }
      
    def forwardConstructorParams(params: Option[List[Symbol]]) = params match {
        case Some(ps) => (ps map (_.name +".asInstanceOf[AnyRef]")).mkString("(", ", ", ")")
        case None => "()"
      }
      
    def constructorParamTypes(params: Option[List[Symbol]]) = params match {
        case Some(ps) => (ps map (p => "classOf["+ p.tpe +"]")).mkString(", ")
        case None => ""
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
      forwarderDeclarationConstructor(method, params) +" = "+ forwarderBody(method, params) +
        ".returning("+ mockTraitOrClassName +".this.asInstanceOf["+ className +"])"
      
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
      
    def forwarderParam(parameter: Symbol) = parameter.name +": "+ mockParameterType(parameter)
    
    def mockParameterType(parameter: Symbol) = {
      val typeName = parameter.tpe.toString
      if (!(parameterTypes contains typeName))
        parameterTypes += typeName
      mockParameterName(typeName)
    }
    
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
        mockFunction(method, params, result) +"]"

    def mockMember(method: Symbol): String = 
      method.info match {
        case MethodType(params, result) => mockMember(method, params, result)
        case NullaryMethodType(result) => mockMember(method, Nil, result)
        case PolyType(params, result) => "  //"+ method +" // Borachio doesn't (yet) handle type-parameterised methods"
        case _ => sys.error("Borachio plugin: Don't know how to handle "+ method)
      }

    def mockMember(method: Symbol, params: List[Symbol], result: Type) = "  protected lazy val "+ 
      mockMethodName(method) +" = new "+ mockFunction(method, params, result) +"(factory, Symbol(\""+ method.name +"\"))"

    def mockFunction(method: Symbol, params: List[Symbol], result: Type) =
      mockFunctionType(method) + params.length +"["+ (paramTypes(params) :+ result).mkString(", ") +"]"
      
    def mockFunctionType(method: Symbol) =
      if (method.isConstructor)
        "com.borachio.MockConstructor"
      else
        "com.borachio.MockFunction"

    def paramTypes(params: List[Symbol]) = params map (_.tpe)
  }
}
