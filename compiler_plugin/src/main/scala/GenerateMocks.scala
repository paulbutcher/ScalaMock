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
import scala.collection.mutable.{ListBuffer, Map}

class GenerateMocks(plugin: BorachioPlugin, val global: Global) extends PluginComponent {
  import global._
  import definitions._
  
  val runsAfter = List[String]("typer")
  val phaseName = "generatemocks"
  
  val mocks = new ListBuffer[(String, String)]
  val mockObjects = new ListBuffer[(String, String)]
  
  val parameterTypes = Map[Type, Int]()

  lazy val MockAnnotation = definitions.getClass("com.borachio.annotation.mock")
  lazy val MockObjectAnnotation = definitions.getClass("com.borachio.annotation.mockObject")
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
        for (AnnotationInfo(atp, args, _) <- tree.symbol.annotations)
          atp.typeSymbol match {
            case MockAnnotation => mockClass(atp)
            case MockObjectAnnotation => mockObject(args)
            case _ =>
          }
                  
      case _ =>
    }
  }
  
  def mockClass(atp: Type) {
    assert(atp.typeArgs.length == 1)
    val symbol = atp.typeArgs.head.typeSymbol
    if (symbol.isTrait)
      new MockTrait(symbol).generate
    else
      new MockClass(symbol).generate
  }
  
  def mockObject(args: List[Tree]) {
    assert(args.length == 1)
    val tpe = args.head.tpe.typeSymbol
    if (tpe.isModuleClass)
      new MockObject(tpe).generate
    else
      globalError("@mockObject parameter must be a singleton object")
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
      "  lazy val mockObject = Map(\n"+
      (mockObjects map { case (target, mock) => mockObjectEntry(target, mock) }).mkString(",\n") +"\n"+
      "  )\n\n"+
      (parameterTypes.keys map (parameterConverter _)).mkString("\n") +"\n"+
    "}"
  
  def mockFactoryEntry(target: String, mock: String) =
    "  implicit def toMock(m: "+ target +") = m.asInstanceOf["+ mock +"]"
    
  def mockObjectEntry(target: String, mock: String) = "    ("+ target +" -> "+ target +".asInstanceOf["+ mock +"])"
    
  def parameterConverter(tpe: Type) = {
    val parameterName = mockParameterName(tpe)
    "  protected implicit def to"+ parameterName +"(v: "+ tpe +") = new com.borachio."+ parameterName +"(v)\n"+
      "  protected implicit def to"+ parameterName +"(v: com.borachio.MatchAny) = new com.borachio."+ parameterName +"(v)\n"
  }
    
  def mockParameterName(tpe: Type) = "MockParameter$"+ parameterTypes(tpe)
    
  def generateMockParameterTypes() {
    val writer = new FileWriter(new File(testOutputDirectory, "GeneratedParameterTypes.scala"))
    writer.write(mockParameterTypes)
    writer.close
  }
    
  def mockParameterTypes = 
    "package com.borachio\n\n"+
    (parameterTypes.keys map (generatedMockParameterType _)).mkString("\n\n")
  
  def generatedMockParameterType(tpe: Type) =
    "class "+ mockParameterName(tpe) +"(value: AnyRef) extends com.borachio.MockParameter["+ tpe +"](value) {\n"+
      "  def this(v: "+ tpe +") = this(v.asInstanceOf[AnyRef])\n"+
      "  def this(v: MatchAny) = this(v.asInstanceOf[AnyRef])\n"+
    "}"
  
  def createOutputDirectories {
    new File(mockOutputDirectory).mkdirs
    new File(testOutputDirectory).mkdirs
  }

  abstract class Mock(mockSymbol: Symbol) {
    
    def generate() {
      log("Creating mock for: "+ mockSymbol)

      generateMock()
      generateTest()
      
      val mapping = (qualifiedClassName -> qualifiedMockTraitOrClassName)
      recordMapping(mapping)
    }
    
    def generateMock() {
      val mockWriter = new FileWriter(new File(mockOutputDirectory, qualifiedClassName +".scala"))
      mockWriter.write(mock)
      mockWriter.close
    }
    
    def generateTest() {
      val testWriter = new FileWriter(new File(testOutputDirectory, qualifiedMockTraitOrClassName +".scala"))
      testWriter.write(test)
      testWriter.close
    }
    
    def recordMapping(mapping: (String, String)) {
      mocks += mapping
    }

    lazy val mock =
      packageStatement +"\n\n"+
        classOrObjectDeclaration +" {\n\n"+
          mockClassEntries +"\n\n"+
          forwardTo +"\n"+
        "}"
        
    lazy val test =
      packageStatement +"\n\n"+
        mockTraitOrClassDeclaration +" {\n\n"+
          expectForwarders +"\n\n"+
          mockTraitEntries +"\n"+
        "}\n"
        
    lazy val mockClassEntries = 
      mockMethods +"\n\n"+
      factoryReference +"\n\n"+
      mockMembers
      
    lazy val mockTraitEntries = ""
        
    lazy val packageStatement = "package "+ packageName

    lazy val classOrObjectDeclaration = classOrObject +" extends "+ mockTraitOrClassName
        
    lazy val classOrObject: String = "class "+ className +"(dummy: com.borachio.MockConstructorDummy)"
      
    lazy val factoryReference = 
      "  val factory = {\n"+
      "    val classLoader = getClass.getClassLoader\n"+
      "    val method = classLoader.getClass.getMethod(\"getFactory\")\n"+
      "    method.invoke(classLoader).asInstanceOf[com.borachio.AbstractMockFactory]\n"+
      "  }"
      
    lazy val forwardTo = "  var forwardTo: AnyRef = _\n\n"+ forwarders
    
    lazy val forwarders = (methodsToMock map forwardMethod _).mkString("\n")

    lazy val mockMethods = (methodsToMock map mockMethod _).mkString("\n")

    lazy val expectForwarders = "  val expects = new {\n"+
        "    lazy val clazz = "+ mockTraitOrClassName +".this.getClass\n\n"+
        (methodsToMock map expectForwarder _).mkString("\n") +"\n\n"+
        (methodsToMock map cachedMockMethod _).mkString("\n") + "\n"+
      "  }"

    lazy val mockMembers = (methodsToMock map mockMember _).mkString("\n")
    
    lazy val mockTraitOrClassDeclaration = "trait "+ mockTraitOrClassName
        
    lazy val packageName = mockSymbol.enclosingPackage.fullName.toString

    lazy val className = mockSymbol.name.toString
    
    lazy val mockTraitOrClassName = getMockTraitOrClassName

    lazy val methodsToMock = getMethodsToMock
      
    lazy val mockMethodNames = methodNames("mock")
    
    lazy val forwarderNames = methodNames("forwarder")
    
    lazy val qualifiedClassName = qualify(className)
    
    lazy val qualifiedMockTraitOrClassName = qualify(mockTraitOrClassName)
    
    def getMethodsToMock = mockSymbol.info.nonPrivateMembers filter { s => 
        s.isMethod && !s.isMemberOf(ObjectClass)
      }
      
    def getMockTraitOrClassName = "Mock$"+ className
      
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
      "      forwardTo = mock\n"+
      "    } else {\n"+
      "      val classLoader = getClass.getClassLoader\n"+
      "      val method = classLoader.getClass.getMethod(\"loadClassNormal\", classOf[String])\n"+
      "      val clazz = method.invoke(classLoader, \""+ qualifiedClassName +"\").asInstanceOf[Class[_]]\n"+
      "      val constructor = clazz.getConstructor("+ constructorParamTypes(params) +")\n"+
      "      forwardTo = constructor.newInstance"+ forwardConstructorParams(params) +".asInstanceOf[AnyRef]\n"+
      "    }\n"+
      "  }"
      
    def mockBodyNormal(method: Symbol, params: Option[List[Symbol]]) = 
      "if (forwardTo != null) "+ forwarderNames(method) + forwardParams(params) +
      " else "+ mockBodySimple(method, params)
        
    def mockBodySimple(method: Symbol, params: Option[List[Symbol]]) = mockMethodName(method) + forwardParams(params)
    
    def mockMethodName(method: Symbol) = mockMethodNames(method)

    def forwardParams(params: Option[List[Symbol]]) = params match {
        case Some(ps) => (ps map (_.name)).mkString("(", ", ", ")")
        case None => "()"
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
      
    def forwarderParam(parameter: Symbol) = parameter.name +": com.borachio."+ mockParameterType(parameter)
    
    def mockParameterType(parameter: Symbol) = {
      val tpe = parameter.tpe
      if (!(parameterTypes contains tpe))
        parameterTypes += (tpe -> parameterTypes.size)
      mockParameterName(tpe)
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
      mockMethodName(method) +" = new "+ mockFunction(method, params, result) +"(factory, Symbol(\""+ method.name.decode +"\"))"

    def mockFunction(method: Symbol, params: List[Symbol], result: Type) =
      mockFunctionType(method) + params.length +"["+ (paramTypes(params) :+ result).mkString(", ") +"]"
      
    def mockFunctionType(method: Symbol) =
      if (method.isConstructor)
        "com.borachio.MockConstructor"
      else
        "com.borachio.MockFunction"

    def paramTypes(params: List[Symbol]) = params map (_.tpe)
    
    def methodNames(prefix: String) = (methodsToMock.zipWithIndex map { case (m, i) => (m -> (prefix +"$"+ i)) }).toMap
    
    def forwardMethod(method: Symbol): String =
      method.info match {
        case MethodType(params, result) => forwardMethod(method, params, result)
        case NullaryMethodType(result) => forwardMethod(method, Nil, result)
        case PolyType(params, result) => "  //"+ method +" // Borachio doesn't (yet) handle type-parameterised methods"
        case _ => sys.error("Borachio plugin: Don't know how to handle "+ method)
      }
      
    def forwardMethod(method: Symbol, params: List[Symbol], result: Type) =
      "  lazy val "+ forwarderNames(method) +" = {\n"+
      "    val method = forwardTo.getClass.getMethod("+ forwarderGetMethodParams(method, params) +")\n"+
      "    ("+ mockParams(Some(params)) +" => method.invoke(forwardTo"+ forwardForwarderParams(params) +").asInstanceOf["+ result +"])\n"+
      "  }"
      
    def forwarderGetMethodParams(method: Symbol, params: List[Symbol]) = 
      (("\""+ method.name.decode +"\"") +: (paramTypes(params) map (p => "classOf["+ p +"]"))).mkString(", ")
      
    def forwardForwarderParams(params: List[Symbol]) = params match {
        case Nil => ""
        case _ => ", "+ (params map (_.name +".asInstanceOf[AnyRef]")).mkString(", ")
      }
  }
  
  class MockClass(mockSymbol: Symbol) extends Mock(mockSymbol)
  
  class MockTrait(mockSymbol: Symbol) extends Mock(mockSymbol) {
    
    override def generateMock() { /* NOOP */ }
    
    override lazy val mockTraitEntries = mockClassEntries

    override lazy val mockTraitOrClassDeclaration = 
      "class "+ mockTraitOrClassName +"(dummy: com.borachio.MockConstructorDummy) extends "+ className

    override def mockBodyNormal(method: Symbol, params: Option[List[Symbol]]) = mockBodySimple(method, params)
  }
  
  class MockObject(mockSymbol: Symbol) extends Mock(mockSymbol) {

    override def getMockTraitOrClassName = super.getMockTraitOrClassName +"$"

    override def recordMapping(mapping: (String, String)) {
      mockObjects += mapping
    }

    override lazy val classOrObject = "object "+ className
    
    override def getMethodsToMock = super.getMethodsToMock filter (!_.isConstructor)
  }
}
