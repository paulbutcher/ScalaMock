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
import nsc.plugins.PluginComponent

import java.io.{File, FileWriter}
import scala.collection.mutable.{ListBuffer, Map}
import scala.util.matching.Regex

class GenerateMocks(plugin: ScalaMockPlugin, val global: Global) extends PluginComponent with VersionSpecific {
  import global._
  import definitions._
  
  val runsAfter = List[String]("typer")
  val phaseName = "generatemocks"
  
  val mocks = new ListBuffer[(String, String)]
  val mockObjects = new ListBuffer[(String, String)]
  
  lazy val MockAnnotation = definitions.getClass("org.scalamock.annotation.mock")
  lazy val MockWithCompanionAnnotation = definitions.getClass("org.scalamock.annotation.mockWithCompanion")
  lazy val MockObjectAnnotation = definitions.getClass("org.scalamock.annotation.mockObject")
  lazy val mockRoot = plugin.mockOutputDirectory.get
  lazy val testRoot = plugin.testOutputDirectory.get
  
  def newPhase(prev: Phase) = new StdPhase(prev) {
    def apply(unit: CompilationUnit) {
      if (plugin.mockOutputDirectory.isDefined || plugin.testOutputDirectory.isDefined) {
        if (plugin.mockOutputDirectory.isDefined && plugin.testOutputDirectory.isDefined) {
          new ForeachTreeTraverser(findMockAnnotations).traverse(unit.body)
          generateMockFactory
        } else {
          error("Both -P:scalamock:generatemocks and -P:scalamock:generatetest must be given")
        }
      }
    }
  }
  
  def findMockAnnotations(tree: Tree) {
    tree match {
      case ClassDef(_, _, _, _) if tree.hasSymbol =>
        for (AnnotationInfo(atp, args, _) <- tree.symbol.annotations)
          atp.typeSymbol match {
            case MockAnnotation => mockType(atp)
            case MockWithCompanionAnnotation => mockWithCompanion(atp)
            case MockObjectAnnotation => mockObject(args)
            case _ =>
          }
                  
      case _ =>
    }
  }
  
  def mockType(atp: Type) {
    assert(atp.typeArgs.length == 1)
    val symbol = atp.typeArgs.head.typeSymbol
    mockClassOrTrait(symbol).generate
  }
  
  def mockClassOrTrait(symbol: Symbol, enclosing: Context = TopLevel): Mock =
    if (symbol.isTrait)
      new MockTrait(symbol, enclosing)
    else
      new MockClass(symbol, enclosing)
  
  def mockWithCompanion(atp: Type) {
    assert(atp.typeArgs.length == 1)
    val symbol = atp.typeArgs.head.typeSymbol
    val companion = symbol.companionModule
    if (companion == NoSymbol || companion.isJavaDefined)
      error("@mockWithCompanion["+ symbol +"] - no companion found")
    else
      new MockWithCompanion(symbol, companion.moduleClass).generate
  }
  
  def mockObject(args: List[Tree]) {
    assert(args.length == 1)
    val symbol = args.head.symbol
    if (symbol.isModule)
      new MockObject(symbol.moduleClass).generate
    else
      error("@mockObject("+ symbol +") parameter must be a singleton object")
  }
  
  def generateMockFactory() {
    val writer = new FileWriter(new File(generatedMockFactoryPath, "GeneratedMockFactory.scala"))
    writer.write(mockFactory)
    writer.close
  }
  
  def generatedMockFactoryPath = {
    val d = new File(testRoot, "org/scalamock/generated")
    d.mkdirs
    d
  }
  
  def mockFactory =
    "package org.scalamock.generated\n\n"+
    "trait GeneratedMockFactory extends org.scalamock.GeneratedMockFactoryBase { self: org.scalamock.MockFactoryBase =>\n"+
      toMockMethods +"\n\n"+
      mockObjectMethods +"\n"+
    "}"
    
  def toMockMethods =
    (mocks map { case (target, mock) => mockFactoryEntry(target, mock) }).mkString("\n")
  
  def mockObjectMethods =
    (mockObjects map { case (target, mock) => mockObjectEntry(target, mock) }).mkString("\n")
  
  def mockFactoryEntry(target: String, mock: String) =
    "  implicit def toMock(m: "+ target +") = m.asInstanceOf["+ mock +"]"
    
  def mockObjectEntry(target: String, mock: String) =
    "  def mockObject(x: "+ target +".type) = objectToMock["+ mock +"](x)"
    
  trait Context {
    val topLevel: Boolean
    val fullMockTraitOrClassName: String
  }
  
  object TopLevel extends Context {
    val topLevel = true
    val fullMockTraitOrClassName = null
  }

  abstract class Mock(mockSymbol: Symbol, enclosing: Context) extends Context {
    
    val topLevel = false
    
    val fullMockTraitOrClassName =
      if (enclosing.topLevel)
        qualify(mockTraitOrClassName)
      else
        enclosing.fullMockTraitOrClassName +"#"+ mockTraitOrClassName
    
    def generate() {
      log("Creating mock for: "+ mockSymbol)

      generateMock()
      generateTest()
      generateJavaFeatures()
    }
    
    def generateMock() {
      generateFile(mockFile, getMock)
    }
    
    def generateTest() {
      generateFile(testFile, getTest)
    }
    
    def generateJavaFeatures() {
      if (hasJavaFeatures)
        generateFile(javaFeaturesFile, getJavaFeatures)
    }
    
    def generateFile(file: File, body: String) {
      val writer = new FileWriter(file)
      writer.write(packageStatement +"\n\n"+ body)
      writer.close
    }
    
    lazy val mockFile = new File(mockOutputDirectory, className +".scala")
    
    lazy val testFile = new File(testOutputDirectory, mockTraitOrClassName +".scala")
    
    lazy val javaFeaturesFile = new File(mockOutputDirectory, javaFeaturesClassName +".java")
    
    lazy val mockOutputDirectory = getOutputDirectory(mockRoot)
    
    lazy val testOutputDirectory = getOutputDirectory(testRoot)
    
    def getOutputDirectory(root: String) = {
      val d = packageDirectory(new File(root))
      d.mkdirs
      d
    }
    
    def packageDirectory(root: File) = packageElements.foldLeft(root) { (f, e) => new File(f, e) }
    
    lazy val packageElements = packageName split '.'
    
    def getMock: String =
      mockedTypeDeclaration +" {\n\n"+
        mockClassEntries +"\n\n"+
        forwarding +"\n\n"+
        indent((nestedMocks map ( _.getMock )).mkString("\n")) +"\n\n"+
      "}"
      
    def getTest: String = {
      val mapping = (fullClassName -> fullMockTraitOrClassName)
      recordMapping(mapping)

      mockTraitOrClassDeclaration +" {\n\n"+
        expectForwarders +"\n\n"+
        (if (hasNestedTypes) nestedMockCreator +"\n\n" else "") +
        mockTraitEntries +"\n\n"+
        factoryReference +"\n\n"+
        indent((nestedMocks map ( _.getTest )).mkString("\n")) +"\n\n"+
      "}\n"
    }
    
    def getJavaFeatures: String =
      javaFeaturesClassDeclaration +" {\n\n"+
        javaValueForwarders +"\n"+
      "}\n"
    
    def indent(s: String) = "  " + new Regex("\n").replaceAllIn(s, "\n  ")

    def recordMapping(mapping: (String, String)) {
      mocks += mapping
    }

    lazy val mockClassEntries = 
      mockMethods +"\n\n"+
      mockMembers
      
    lazy val mockTraitEntries = ""
        
    lazy val packageStatement = "package "+ packageName +";"

    lazy val mockedTypeDeclaration =
      classOrObject +" extends "+ (javaFeaturesParent ++ parents :+ mockTraitOrClassName).mkString(" with ")
        
    lazy val classOrObject: String = "class "+ className +"(dummy: org.scalamock.MockConstructorDummy)"
      
    lazy val factoryReference = 
      "  protected val factory = {\n"+
      "    val classLoader = getClass.getClassLoader\n"+
      "    val method = classLoader.getClass.getMethod(\"getFactory\")\n"+
      "    method.invoke(classLoader).asInstanceOf[org.scalamock.MockFactoryBase]\n"+
      "  }"
      
    lazy val forwarding = forwardTo +"\n\n"+ forwarders
    
    def forwardTo = "  private var forwardTo$Mocks: AnyRef = _"
    
    lazy val forwarders = (methodsToMock map forwardMethod _).mkString("\n")

    lazy val mockMethods = (methodsToMock map mockMethod _).mkString("\n")

    lazy val expectForwarders =
      "  val expects = new {\n"+
      "    private lazy val clazz = "+ mockTraitOrClassName +".this.getClass\n\n"+
           (methodsToMock map expectForwarder _).mkString("\n") +"\n\n"+
           (methodsToMock map cachedMockMethod _).mkString("\n") + "\n"+
      "  }"
      
    lazy val nestedMockCreator = 
      "  def mock[T: ClassManifest] = {\n"+
      "    val erasure = classManifest[T].erasure\n"+
      "    val clazz = Class.forName(erasure.getName)\n"+
      "    val constructor = clazz.getConstructor(classOf["+ fullClassName +"], classOf[org.scalamock.MockConstructorDummy])\n"+
      "    constructor.newInstance(this, new org.scalamock.MockConstructorDummy).asInstanceOf[T]\n"+
      "  }"

    lazy val mockMembers = (methodsToMock map mockMember _).mkString("\n")
    
    lazy val mockTraitOrClassDeclaration = "trait "+ mockTraitOrClassName
        
    lazy val packageName = mockSymbol.enclosingPackage.fullName.toString

    lazy val className = mockSymbol.name.toString
    
    lazy val fullClassName = fixedType(mockSymbol.tpe).toString
    
    lazy val mockTraitOrClassName = getMockTraitOrClassName
    
    lazy val methodsToMock = getMethodsToMock
      
    lazy val mockMethodNames = methodNames("mock")
    
    lazy val forwarderNames = methodNames("forwarder")
    
    lazy val qualifiedClassName = qualify(className)
    
    lazy val javaFeaturesClassName = className +"$JavaFeatures"
    
    lazy val nestedMocks = nestedTypes map { s => mockClassOrTrait(s, this) }
    
    lazy val nestedTypes = mockSymbol.info.nonPrivateMembers filter ( _.isClass )
    
    lazy val hasNestedTypes = nestedTypes.nonEmpty
    
    lazy val parents = mockSymbol.info.parents filter { s => 
        s.typeSymbol != ObjectClass && s.typeSymbol != ScalaObjectClass
      }
      
    lazy val hasJavaFeatures = !javaStatics.isEmpty
    
    lazy val javaStatics = mockSymbol.companionModule match {
        case NoSymbol => Nil
        case companion => companion.info.nonPrivateMembers filter { s => 
            s.isJavaDefined && !isMemberOfObject(s)
          }
      }
      
    lazy val javaValues = javaStatics filter (!_.isMethod)
    
    lazy val javaFeaturesClassDeclaration = "public abstract class "+ javaFeaturesClassName
    
    lazy val javaValueForwarders = 
      "  private static Class clazz = org.scalamock.ReflectionUtilities.getUnmockedClass("+
        qualifiedClassName +".class, \""+ qualifiedClassName +"\");\n\n"+
      (javaValues map javaValueForwarder _).mkString("\n")
      
    lazy val javaFeaturesParent = if (hasJavaFeatures) List(javaFeaturesClassName) else List()
  
    def getMethodsToMock = mockSymbol.info.nonPrivateMembers filter { s => 
        s.isMethod && !isMemberOfObject(s) && !s.isMixinConstructor
      }
      
    def isMemberOfObject(s: Symbol) = !s.isConstructor && (ObjectClass.info.member(s.name) != NoSymbol)
      
    def getMockTraitOrClassName = "Mock$"+ className
      
    def qualify(name: String) = packageName +"."+ name
    
    def mockMethod(method: Symbol) = handleMethod(method) {
      (method, params, result) =>
        if (method.isConstructor)
          mockMethodConstructor(method, params)
        else
          mockMethodNormal(method, params, result)
    }
        
    def mockMethodConstructor(method: Symbol, params: List[List[Symbol]]) =
      methodDeclaration(method, params) +" = "+ mockBodyConstructor(method, params)

    def mockMethodNormal(method: Symbol, params: List[List[Symbol]], result: Type) =
      methodDeclarationWithReturnType(method, params, result) +" = "+ mockBodyNormal(method, params)
      
    def methodDeclarationWithReturnType(method: Symbol, params: List[List[Symbol]], result: Type) =
      methodDeclaration(method, params) +": "+ fixedType(result)
        
    def methodDeclaration(method: Symbol, params: List[List[Symbol]]) = 
      "  "+ overrideIfNecessary(method) +"def "+ method.name.decode + mockParams(params)
      
    def overrideIfNecessary(method: Symbol) = if (needsOverride(method)) "override " else ""
    
    def needsOverride(method: Symbol) = !method.isConstructor && parents.exists { p => 
        val superMethod = p.typeSymbol.info.member(method.name)
        superMethod != NoSymbol && !superMethod.isDeferred
      }
        
    def mockParams(params: List[List[Symbol]]) = (params map mockParamList _).mkString 
    
    def mockParamList(params: List[Symbol]) = 
      (params map parameterDeclaration _).mkString("(", ", ", ")")
      
    def parameterDeclaration(parameter: Symbol) = parameter.name +": "+ parameter.tpe
    
    def mockBodyConstructor(method: Symbol, params: List[List[Symbol]]) = "{\n"+
      "    this(new org.scalamock.MockConstructorDummy)\n"+
      "    val mock = "+ mockBodySimple(method, params) +"\n"+
      "    if (mock != null) {\n"+
      "      forwardTo$Mocks = mock\n"+
      "    } else {\n"+
      "      val clazz = org.scalamock.ReflectionUtilities.getUnmockedClass(getClass, \""+ qualifiedClassName +"\")\n"+
      "      val constructor = clazz.getConstructor("+ constructorParamTypes(params) +")\n"+
      "      forwardTo$Mocks = constructor.newInstance"+ forwardConstructorParams(params) +".asInstanceOf[AnyRef]\n"+
      "    }\n"+
      "  }"
      
    def mockBodyNormal(method: Symbol, params: List[List[Symbol]]) = 
      "if (forwardTo$Mocks != null) "+ forwarderNames(method) + forwardParams(params) +
      " else "+ mockBodySimple(method, params)
        
    def mockBodySimple(method: Symbol, params: List[List[Symbol]]) = mockMethodName(method) + forwardParams(params)
    
    def mockMethodName(method: Symbol) = mockMethodNames(method)

    def forwardParams(params: List[List[Symbol]]) = (params.flatten map (_.name)).mkString("(", ", ", ")") 
      
    def forwardConstructorParams(params: List[List[Symbol]]) =
      (params.flatten map (_.name +".asInstanceOf[AnyRef]")).mkString("(", ", ", ")")
      
    def constructorParamTypes(params: List[List[Symbol]]) =
      (params.flatten map (p => "classOf["+ p.tpe +"]")).mkString(", ")

    def expectForwarder(method: Symbol) = handleMethod(method) {
      (method, params, result) =>
        if (method.isConstructor)
          expectForwarderConstructor(method, params, result)
        else
          expectForwarderNormal(method, params, result)
    }
        
    def expectForwarderConstructor(method: Symbol, params: List[List[Symbol]], result: Type) =
      forwarderDeclarationConstructor(method, params) +" = "+ forwarderBody(method, params) +
        ".returning("+ mockTraitOrClassName +".this.asInstanceOf["+ fullClassName +"])"
      
    def expectForwarderNormal(method: Symbol, params: List[List[Symbol]], result: Type) =
      forwarderDeclarationNormal(method, params, result) +" = "+ forwarderBody(method, params) +"\n"+
      matchingForwarder(method, params)
      
    def forwarderDeclarationConstructor(method: Symbol, params: List[List[Symbol]]) =
      "    def newInstance"+ forwarderParams(params)
        
    def forwarderDeclarationNormal(method: Symbol, params: List[List[Symbol]], result: Type) =
      "    def "+ method.name.decode + forwarderParams(params) + overloadDisambiguation(method) +": "+ 
        expectationType(params.flatten, result)
        
    def matchingForwarder(method: Symbol, params: List[List[Symbol]]) = {
      val ps = params.flatten
      if (ps.length > 0)
        matchingForwarderList(method, ps)
      else
        ""
    }
    
    def matchingForwarderList(method: Symbol, params: List[Symbol]) = 
      "    def "+ method.name.decode + "(matcher: org.scalamock.MockMatcher"+ params.length +"["+ 
        fixedTypes(paramTypes(params)).mkString(", ") +"])"+ overloadDisambiguation(method) +" = "+
        mockFunctionToExpectation(method, params) +".expects(matcher)"
        
    def forwarderParams(params: List[List[Symbol]]) = (params map forwarderParamList _).mkString
    
    def forwarderParamList(params: List[Symbol]): String = (params map forwarderParam _).mkString("(", ", ", ")") 
      
    def forwarderParam(parameter: Symbol) = parameter.name +": org.scalamock.MockParameter["+ parameter.tpe +"]"
    
    // Add DummyImplicit sentinel parameters to overloaded methods to avoid problems with
    // ambiguity in the face of type erasure. See:
    // http://groups.google.com/group/scala-user/browse_thread/thread/95acee0572cfa407/95d41ac32d36f743#95d41ac32d36f743
    def overloadDisambiguation(method: Symbol) = {
      val index = method.owner.info.member(method.name).alternatives.indexOf(method)
      assert(index >= 0)
      if (index > 0)
        ((1 to index) map { i => "sentinel"+ i +": DummyImplicit" }).mkString("(implicit ", ", ", ")")
      else
        ""
    }
    
    def expectationType(params: List[Symbol], result: Type) =
      "org.scalamock.TypeSafeExpectation"+ params.length +"["+ 
        fixedTypes(paramTypes(params) :+ result).mkString(", ") +"]"
        
    def forwarderBody(method: Symbol, params: List[List[Symbol]]) =
      mockFunctionToExpectation(method, params.flatten) +".expects"+ forwardParams(params)
      
    def mockFunctionToExpectation(method: Symbol, params: List[Symbol]) =
      "factory.mockFunction"+ params.length +"ToExpectation("+ mockMethodName(method) +")"
    
    def cachedMockMethod(method: Symbol): String = handleMethod(method) {
      (method, params, result) =>
        "    private lazy val "+ mockMethodName(method) +" = "+ cacheLookup(method, params.flatten, result)
    }
      
    def cacheLookup(method: Symbol, params: List[Symbol], result: Type) =
      "clazz.getMethod(\""+ mockMethodName(method) +"\").invoke("+ mockTraitOrClassName +".this).asInstanceOf["+ 
        mockFunction(method, params, result) +"]"

    def mockMember(method: Symbol): String = handleMethod(method) {
      (method, params, result) =>
        "  protected lazy val "+ mockMethodName(method) +" = new "+ mockFunction(method, params.flatten, result) +
        "(factory, Symbol(\""+ method.name.decode +"\"))"
    }

    def mockFunction(method: Symbol, params: List[Symbol], result: Type) =
      mockFunctionType(method) + params.length +"["+ fixedTypes(paramTypes(params) :+ result).mkString(", ") +"]"
      
    def mockFunctionType(method: Symbol) =
      if (method.isConstructor)
        "org.scalamock.MockConstructor"
      else
        "org.scalamock.MockFunction"
        
    def paramTypes(params: Option[List[Symbol]]): List[Type] = params match {
      case Some(ps) => paramTypes(ps)
      case None => Nil
    }
        
    def paramTypes(params: List[Symbol]) = params map (_.tpe)
    
    def methodNames(prefix: String) = (methodsToMock.zipWithIndex map { case (m, i) => (m -> (prefix +"$"+ i)) }).toMap
    
    def forwardMethod(method: Symbol): String = handleMethod(method) {
      (method, params, result) => {
        val ps = params.flatten
        "  private lazy val "+ forwarderNames(method) +" = {\n"+
        "    val method = forwardTo$Mocks.getClass.getMethod("+ forwarderGetMethodParams(method, ps) +")\n"+
        "    ("+ mockParamList(ps) +" => method.invoke(forwardTo$Mocks"+ forwardForwarderParams(ps) +").asInstanceOf["+ fixedType(result) +"])\n"+
        "  }"
      }
    }
      
    def forwarderGetMethodParams(method: Symbol, params: List[Symbol]) = 
      (("\""+ method.name.decode +"\"") +: (paramTypes(params) map (p => "classOf["+ p +"]"))).mkString(", ")
      
    def forwardForwarderParams(params: List[Symbol]) = params match {
        case Nil => ""
        case _ => ", "+ (params map (_.name +".asInstanceOf[AnyRef]")).mkString(", ")
      }

    def fixedTypes(ts: List[Type]) = ts map fixedType _
      
    def fixedType(t: Type) = 
      if (t.typeSymbol.isNestedClass)
        erasure.erasure(t)
      else
        t
        
    def handleMethod(method: Symbol)(handler: (Symbol, List[List[Symbol]], Type) => String) =
      handleMethodByType(method, method.info.asSeenFrom(mockSymbol.thisType, method.owner))(handler)
        
    def javaValueForwarder(value: Symbol) =
      "  public static "+ javaType(value.info) +" "+ value.name +" = "+ fieldGetter(value)

    def javaType(t: Type) = t.typeSymbol match {
      case BooleanClass => "boolean"
      case ByteClass => "byte"
      case CharClass => "char"
      case DoubleClass => "double"
      case FloatClass => "float"
      case IntClass => "int"
      case LongClass => "long"
      case ShortClass => "short"
      case StringClass => "String"
      case _ => t.toString
    }
    
    def fieldGetter(value: Symbol) = fieldGetterMethod(value.info) +"(clazz, \""+ value.name +"\");"
    
    def fieldGetterMethod(t: Type) = t.typeSymbol match {
      case BooleanClass => fieldGetterName("Boolean")
      case ByteClass => fieldGetterName("Byte")
      case CharClass => fieldGetterName("Char")
      case DoubleClass => fieldGetterName("Double")
      case FloatClass => fieldGetterName("Float")
      case IntClass => fieldGetterName("Int")
      case LongClass => fieldGetterName("Long")
      case ShortClass => fieldGetterName("Short")
      case _ => "("+ javaType(t) +")"+ fieldGetterName("Object")
    }
    
    def fieldGetterName(t: String) = "org.scalamock.ReflectionUtilities.get"+ t +"Field"
  }
  
  class MockClass(mockSymbol: Symbol, enclosing: Context) extends Mock(mockSymbol, enclosing) {
    assert(mockSymbol.isClass && !mockSymbol.isTrait)
  }
  
  class MockTrait(mockSymbol: Symbol, enclosing: Context) extends Mock(mockSymbol, enclosing) {
    assert(mockSymbol.isTrait)
    
    override def generateMock() { /* NOOP */ }
    
    override def getMock =
      "trait "+ className +" {\n\n"+
        (methodsToMock map methodDeclaration _).mkString("\n") +"\n"+
      "}"
    
    override lazy val mockTraitEntries = mockClassEntries

    override lazy val mockTraitOrClassDeclaration = 
      "class "+ mockTraitOrClassName +"(dummy: org.scalamock.MockConstructorDummy) extends "+ className

    override def mockBodyNormal(method: Symbol, params: List[List[Symbol]]) = mockBodySimple(method, params)

    override def getMethodsToMock = super.getMethodsToMock filter (!_.isConstructor)

    def methodDeclaration(method: Symbol) = handleMethod(method){ methodDeclarationWithReturnType _ }
  }
  
  class MockObject(mockSymbol: Symbol) extends Mock(mockSymbol, TopLevel) {
    assert(mockSymbol.isModuleClass)

    override lazy val fullClassName = qualifiedClassName

    override def forwardTo = super.forwardTo +"\n\n"+ resetForwarding
      
    lazy val resetForwarding =
      "  resetForwarding$Mocks\n\n"+
      "  def resetForwarding$Mocks() {\n"+
      "    val clazz = org.scalamock.ReflectionUtilities.getUnmockedClass(getClass, \""+ fullClassName +"$\")\n"+
      "    forwardTo$Mocks = clazz.getField(\"MODULE$\").get(null)\n"+
      "  }\n\n"+
      "  def enableForwarding$Mocks() {\n"+
      "    forwardTo$Mocks = null\n"+
      "  }"

    override def getMockTraitOrClassName = "Mock$$"+ className

    override def recordMapping(mapping: (String, String)) {
      mockObjects += mapping
    }

    override lazy val classOrObject = "object "+ className
    
    override def getMethodsToMock = super.getMethodsToMock filter (!_.isConstructor)
  }
  
  class MockWithCompanion(mockSymbol: Symbol, companionSymbol: Symbol) extends Mock(mockSymbol, TopLevel) {
    
    val mockType = mockClassOrTrait(mockSymbol)
    val companionMock = new MockObject(companionSymbol)
    
    override def getMock = mockType.getMock +"\n\n"+ companionMock.getMock
    
    override def getTest = mockType.getTest +"\n\n"+ companionMock.getTest
  }
}
