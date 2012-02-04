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

class GenerateMocks(plugin: ScalaMockPlugin, val global: Global) extends PluginComponent with Utils {
  import global._
  import definitions._
  
  val runsAfter = List[String]("typer")
  val phaseName = "generatemocks"
  
  val mocks = new ListBuffer[Mock]
  val mockObjects = new ListBuffer[Mock]
  
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
          try {
            atp.typeSymbol match {
              case MockAnnotation => mockType(atp)
              case MockWithCompanionAnnotation => mockWithCompanion(atp)
              case MockObjectAnnotation => mockObject(args)
              case _ =>
            }
          } catch {
            case AlreadyMockedException => // Do nothing - it's already been mocked
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
    (mocks.zipWithIndex map mockFactoryEntry _).mkString("\n")
  
  def mockObjectMethods =
    (mockObjects map mockObjectEntry _).mkString("\n")
  
  def mockFactoryEntry(m: (Mock, Int)) = m match { case (mock, index) =>
    "  implicit def toMock$"+ index + mock.typeParamsString +"(m: "+ mock.fullClassName +
    ") = m.asInstanceOf["+ mock.fullMockTraitOrClassName + mock.typeParamsString +"]"
  }
    
  def mockObjectEntry(mock: Mock) = "  def mockObject(x: "+ mock.fullClassName +
    ".type) = objectToMock["+ mock.fullMockTraitOrClassName +"](x)"
    
  trait Context {
    val topLevel: Boolean
    val fullMockTraitOrClassName: String
  }
  
  object TopLevel extends Context {
    val topLevel = true
    val fullMockTraitOrClassName = null
  }
  
  case class MethodInfo(symbol: Symbol, reflectable: Type, enclosing: Mock) {
    lazy val tpe = symbol.info.asSeenFrom(enclosing.mockSymbol.thisType, symbol.owner)
    lazy val params = tpe.paramss
    lazy val result = fixRepeatedParams(fixedType(tpe.finalResultType))
    lazy val typeParams = tpe.typeParams
    lazy val flatParams = params.flatten
    lazy val paramTypes = flatParams map (t => fixedType(t.tpe))
    lazy val name = symbol.name
    lazy val decoded = name.decode
    lazy val isConstructor = symbol.isConstructor
    lazy val reflectableParams = toReflectableType(reflectable).paramss.flatten map { s =>
        s.info match {
          case t if isScalaRepeatedParamType(t) => "Seq[_]"
          case t if isJavaRepeatedParamType(t) => "Array["+ t.typeArgs.head +"]"
          case t if isByNameParamType(t) => "scala.Function0[_]"
          case t => t.toString
        }
      }
    lazy val expectationParamTypes = paramTypes map {
        case t if isRepeatedParamType(t) => "org.scalamock.MatchRepeated"
        case t if isByNameParamType(t) => "scala.Function0[_]"
        case t => t.toString
      }
    lazy val matcherParamTypes = paramTypes map fixRepeatedParams _
  }
  
  case object AlreadyMockedException extends Exception
  
  def toReflectableType(tpe: Type) = appliedType(tpe, List.fill(tpe.typeParams.length)(AnyClass.tpe))
  
  def fixRepeatedParams(tpe: Type) = tpe match {
      case t if isScalaRepeatedParamType(t) => "Seq["+ t.typeArgs.head +"]"
      case t if isJavaRepeatedParamType(t) => "Array["+ t.typeArgs.head +"]"
      case TypeRef(_, ByNameParamClass, arg :: _) => "scala.Function0["+ arg +"]"
      case t => t.toString
    }
      
  def fixedType(t: Type) = 
    if (t.typeSymbol.isNestedClass)
      erasure.erasure(t)
    else
      t

  abstract class Mock(val mockSymbol: Symbol, val enclosing: Context) extends Context {
    
    recordMock()

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

    def recordMock() {
      if (mocks exists (_.mockSymbol == mockSymbol))
        throw AlreadyMockedException
      mocks += this
    }

    lazy val mockClassEntries = 
      mockMethods +"\n\n"+
      mockMembers
      
    lazy val mockTraitEntries = ""
        
    lazy val packageStatement = "package "+ packageName +";"

    lazy val mockedTypeDeclaration =
      classOrObject +" extends "+ (javaFeaturesParent ++ parents :+ mockTraitInstance).mkString(" with ")
        
    lazy val classOrObject: String = "class "+ className + typeParamsString +
      "(dummy: org.scalamock.MockConstructorDummy)"
      
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
    
    lazy val mockTraitOrClassDeclaration = "trait "+ mockTraitOrClassName + typeParamsString
    
    lazy val mockTraitInstance = mockTraitOrClassName + typeParamsString
        
    lazy val packageName = mockSymbol.enclosingPackage.fullName.toString

    lazy val className = mockSymbol.name.toString
    
    lazy val fullClassName = fixedType(mockSymbol.tpe).toString
    
    lazy val mockTraitOrClassName = getMockTraitOrClassName
    
    lazy val methodsToMock = getMethodsToMock map { case (m, r) =>
      MethodInfo(m, r, this)
    }
      
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
    
      lazy val reflectableType = toReflectableType(mockSymbol.typeConstructor)
  
    def getMethodsToMock = {
      val members = mockSymbol.info.nonPrivateMembers
      val reflectableMemberTypes = reflectableType.nonPrivateMembers map {
        case meth: MethodSymbol => meth.typeAsMemberOf(reflectableType)
        case m => m.info
      }
      assert(members.length == reflectableMemberTypes.length)
      (members zip reflectableMemberTypes) filter { case (m, _) =>
        m.isMethod && !isMemberOfObject(m) && !m.isMixinConstructor
      }
    }
      
    def isMemberOfObject(s: Symbol) = !s.isConstructor && (ObjectClass.info.member(s.name) != NoSymbol)
      
    def getMockTraitOrClassName = "Mock$"+ className
      
    def qualify(name: String) = packageName +"."+ name
    
    def mockMethod(info: MethodInfo) = {
      if (info.isConstructor)
        mockMethodConstructor(info)
      else
        mockMethodNormal(info)
    }
        
    def mockMethodConstructor(info: MethodInfo) =
      "  "+ methodDeclaration(info) +" = "+ mockBodyConstructor(info)

    def mockMethodNormal(info: MethodInfo) =
      "  "+ overrideIfNecessary(info) + methodDeclarationWithReturnType(info) +" = "+ mockBodyNormal(info)
      
    def methodDeclarationWithReturnType(info: MethodInfo) =
      methodDeclaration(info) +" : "+ info.result
        
    def methodDeclaration(info: MethodInfo) = 
      "def "+ info.decoded + typeParamsString(info) + mockParams(info)
      
    def overrideIfNecessary(info: MethodInfo) = if (needsOverride(info)) "override " else ""
    
    def needsOverride(info: MethodInfo) = !info.isConstructor && parents.exists { p => 
      val superMethod = p.typeSymbol.info.member(info.name)
      superMethod != NoSymbol && !superMethod.isDeferred
    }
    
    def typeParamsString: String = typeParamsString(mockSymbol.typeParams)
    
    def typeParamsString(info: MethodInfo): String = typeParamsString(info.typeParams)
    
    def typeParamsString(typeParams: List[Symbol]): String =
      if (!typeParams.isEmpty )
        (typeParams map (_.defString) mkString ("[", ", ", "]"))
      else
        ""
    
    def mockParams(info: MethodInfo) = (info.params map mockParamList _).mkString 
    
    def mockParamList(params: List[Symbol]) = 
      (params map parameterDeclaration _).mkString("(", ", ", ")")
      
    def parameterDeclaration(parameter: Symbol) = parameter.name +" : "+ parameterType(parameter.tpe)
    
    def parameterType(t: Type) = t match {
      case _ if isScalaRepeatedParamType(t) => t.typeArgs.head.toString +"*"
      case _ if isJavaRepeatedParamType(t) => "Array["+ t.typeArgs.head +"]"
      case _ => t.toString
    }
    
    def mockBodyConstructor(info: MethodInfo) = "{\n"+
      "    this(new org.scalamock.MockConstructorDummy)\n"+
      "    val mock = "+ mockBodySimple(info) +"\n"+
      "    if (mock != null) {\n"+
      "      forwardTo$Mocks = mock\n"+
      "    } else {\n"+
      "      val clazz = org.scalamock.ReflectionUtilities.getUnmockedClass(getClass, \""+ qualifiedClassName +"\")\n"+
      "      val constructor = clazz.getConstructor("+ constructorParamTypes(info) +")\n"+
      "      forwardTo$Mocks = constructor.newInstance"+ forwardParamsAsAnyRef(info) +".asInstanceOf[AnyRef]\n"+
      "    }\n"+
      "  }"
      
    def mockBodyNormal(info: MethodInfo) = "if (forwardTo$Mocks != null) "+
      forwarderNames(info.symbol) + forwardParamsAsAnyRef(info) + ".asInstanceOf["+ info.result +"] else "+ mockBodySimple(info)
        
    def mockBodySimple(info: MethodInfo) =
      mockMethodNames(info.symbol) +".handle(Array("+ forwardParams(info) +")).asInstanceOf["+ info.result +"]"

    def forwardParams(info: MethodInfo) = (info.flatParams map forwardParam _).mkString(", ")
    
    def forwardParam(param: Symbol) = param match {
      case p if isByNameParamType(p.tpe) => "() => "+ p.name
      case p => p.name
    }
    
    def forwardParamsAsAnyRef(info: MethodInfo) =
      (info.flatParams map { p => "("+ forwardParam(p) +").asInstanceOf[AnyRef]" }).mkString("(", ", ", ")")
      
    def constructorParamTypes(info: MethodInfo) =
      (info.reflectableParams map (p => "classOf["+ p +"]")).mkString(", ")

    def expectForwarder(info: MethodInfo) = {
      if (info.isConstructor)
        expectForwarderConstructor(info)
      else
        expectForwarderNormal(info)
    }
        
    def expectForwarderConstructor(info: MethodInfo) = forwarderDeclarationConstructor(info) +" = "+ forwarderBody(info) +
        ".returning("+ mockTraitOrClassName +".this.asInstanceOf["+ fullClassName +"])"
      
    def expectForwarderNormal(info: MethodInfo) =
      forwarderDeclarationNormal(info) +" = "+ forwarderBody(info) +"\n"+ matchingForwarder(info)
      
    def forwarderDeclarationConstructor(info: MethodInfo) = "    def newInstance"+ forwarderParams(info)
        
    def forwarderDeclarationNormal(info: MethodInfo) =
      "    def "+ info.decoded + typeParamsString(info) + forwarderParams(info) + overloadDisambiguation(info)
        
    def matchingForwarder(info: MethodInfo) =
      if (info.flatParams.length > 0) {
        "    def "+ info.decoded + typeParamsString(info) + "(matcher: org.scalamock.MockMatcher"+ info.flatParams.length +
        info.matcherParamTypes.mkString("[", ", ", "])") + overloadDisambiguation(info) +" = "+
        mockFunctionToExpectation(info, info.matcherParamTypes) +".expects(matcher)"
      } else {
        ""
      }
    
    def forwarderParams(info: MethodInfo) = (info.params map forwarderParamList _).mkString
    
    def forwarderParamList(params: List[Symbol]): String = 
      (params map forwarderParam _).mkString("("+ implicitIfNecessary(params), ", ", ")") 
      
    def forwarderParam(parameter: Symbol) =
      parameter.name +" : org.scalamock.MockParameter"+ forwarderParamType(parameter.tpe)
    
    def forwarderParamType(t: Type) = t match {
        case t if isRepeatedParamType(t) => "["+ t.typeArgs.head +"]*"
        case t if isByNameParamType(t) => "[scala.Function0[_]]"
        case _ => "["+ t +"]"
      }
    
    def implicitIfNecessary(params: List[Symbol]) = if (params.nonEmpty && params.head.isImplicit) "implicit " else ""
    
    // Add DummyImplicit sentinel parameters to overloaded methods to avoid problems with
    // ambiguity in the face of type erasure. See:
    // http://groups.google.com/group/scala-user/browse_thread/thread/95acee0572cfa407/95d41ac32d36f743#95d41ac32d36f743
    def overloadDisambiguation(info: MethodInfo) = {
      val symbol = info.symbol
      val index = symbol.owner.info.member(symbol.name).alternatives.indexOf(symbol)
      assert(index >= 0)
      if (index > 0)
        ((1 to index) map { i => "sentinel"+ i +" : DummyImplicit" }).mkString("(implicit ", ", ", ")")
      else
        ""
    }
    
    def forwarderBody(info: MethodInfo) = mockFunctionToExpectation(info, info.expectationParamTypes) +
      ".expects("+ forwardExpectationParams(info) +")"
      
    def forwardExpectationParams(info: MethodInfo) = {
        info.flatParams map { p =>
          if (isRepeatedParamType(p.info))
            "new org.scalamock.MockParameter(new org.scalamock.MatchRepeated("+ p.name +" : _*))"
          else
            p.name
        }
      }.mkString(", ")
      
    def mockFunctionToExpectation(info: MethodInfo, paramTypes: Seq[String]) =
      mockMethodNames(info.symbol) +".toTypeSafeExpectation"+ info.flatParams.length + 
        (paramTypes :+ info.result).mkString("[", ", ", "]")
    
    def cachedMockMethod(info: MethodInfo): String = {
      "    private lazy val "+ mockMethodNames(info.symbol) +" = "+ cacheLookup(info)
    }
      
    def cacheLookup(info: MethodInfo) = "clazz.getMethod(\""+ mockMethodNames(info.symbol) +"\").invoke("+
      mockTraitOrClassName +".this).asInstanceOf["+ mockFunction(info) +"]"

    def mockMember(info: MethodInfo): String = {
      "  protected lazy val "+ mockMethodNames(info.symbol) +" = new "+ mockFunction(info) +
      "(factory, Symbol(\""+ info.decoded +"\"))"
    }

    def mockFunction(info: MethodInfo) =
      if (info.isConstructor)
        "org.scalamock.MockConstructor["+ info.result +"]"
      else
        "org.scalamock.MockFunction"
        
    def methodNames(prefix: String) = 
      (methodsToMock.zipWithIndex map { case (m, i) => (m.symbol -> (prefix +"$"+ i)) }).toMap
    
    def forwardMethod(info: MethodInfo): String = {
      "  private lazy val "+ forwarderNames(info.symbol) +" = {\n"+
      "    val method = forwardTo$Mocks.getClass.getMethod("+ forwarderGetMethodParams(info) +")\n"+
      "    "+ paramListAsAnyRef(info) +" => method.invoke(forwardTo$Mocks"+ forwardForwarderParams(info) +")\n"+
      "  }"
    }
      
    def forwarderGetMethodParams(info: MethodInfo) =
      (("\""+ info.name +"\"") +: (info.reflectableParams map (p => "classOf["+ p +"]"))).mkString(", ")
      
    def paramListAsAnyRef(info: MethodInfo) = (info.flatParams map (_.name +" : AnyRef")).mkString("(", ", ", ")")
      
    def forwardForwarderParams(info: MethodInfo) = info.flatParams match {
      case Nil => ""
      case _ => ", "+ (info.flatParams map (_.name)).mkString(", ")
    }
        
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
      "trait "+ className +" {\n\n  "+
        (methodsToMock map methodDeclarationWithReturnType _).mkString("\n  ") +"\n"+
      "}"
    
    override lazy val mockTraitEntries = mockClassEntries

    override lazy val mockTraitOrClassDeclaration = 
      "class "+ mockTraitOrClassName + typeParamsString +
      "(dummy: org.scalamock.MockConstructorDummy) extends "+ className + typeParamsString

    override def mockBodyNormal(info: MethodInfo) = mockBodySimple(info)

    override def getMethodsToMock =
      super.getMethodsToMock filter { case (m, _) => !m.isConstructor }
      
    override def needsOverride(info: MethodInfo) = true
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

    override def recordMock() {
      mockObjects += this
    }

    override lazy val classOrObject = "object "+ className
    
    override def getMethodsToMock =
      super.getMethodsToMock filter { case (m, _) => !m.isConstructor }
  }
  
  class MockWithCompanion(mockSymbol: Symbol, companionSymbol: Symbol) extends Mock(mockSymbol, TopLevel) {
    
    val mockType = mockClassOrTrait(mockSymbol)
    val companionMock = new MockObject(companionSymbol)
    
    override def getMock = mockType.getMock +"\n\n"+ companionMock.getMock
    
    override def getTest = mockType.getTest +"\n\n"+ companionMock.getTest
    
    override def recordMock() { /* NOOP */ }
  }
}
