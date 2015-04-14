package com.paulbutcher.test.proxy

import com.paulbutcher.test.{ManyParamsClass, ManyParamsTrait}
import org.scalamock.scalatest.proxy.MockFactory
import org.scalatest.FreeSpec

class ProxyMockManyParamsTest extends FreeSpec with MockFactory {

  autoVerify = false

  "Mocking trait should" - {
    "allow expectations to be set on methods with many parameters" in withExpectations {
      val m = mock[ManyParamsTrait]

      m.expects('methodWith1Ints)(1).returning(99)
      assertResult(99) { m.methodWith1Ints(1) }

      m.expects('methodWith2Ints)(1,1).returning(98)
      assertResult(98) { m.methodWith2Ints(1,1) }

      m.expects('methodWith3Ints)(1,1,1).returning(97)
      assertResult(97) { m.methodWith3Ints(1,1,1) }

      m.expects('methodWith4Ints)(1,1,1,1).returning(96)
      assertResult(96) { m.methodWith4Ints(1,1,1,1) }

      m.expects('methodWith5Ints)(1,1,1,1,1).returning(95)
      assertResult(95) { m.methodWith5Ints(1,1,1,1,1) }

      m.expects('methodWith6Ints)(1,1,1,1,1,1).returning(94)
      assertResult(94) { m.methodWith6Ints(1,1,1,1,1,1) }

      m.expects('methodWith7Ints)(1,1,1,1,1,1,1).returning(93)
      assertResult(93) { m.methodWith7Ints(1,1,1,1,1,1,1) }

      m.expects('methodWith8Ints)(1,1,1,1,1,1,1,1).returning(92)
      assertResult(92) { m.methodWith8Ints(1,1,1,1,1,1,1,1) }

      m.expects('methodWith9Ints)(1,1,1,1,1,1,1,1,1).returning(91)
      assertResult(91) { m.methodWith9Ints(1,1,1,1,1,1,1,1,1) }

      m.expects('methodWith10Ints)(1,1,1,1,1,1,1,1,1,1).returning(90)
      assertResult(90) { m.methodWith10Ints(1,1,1,1,1,1,1,1,1,1) }

      m.expects('methodWith11Ints)(1,1,1,1,1,1,1,1,1,1,1).returning(89)
      assertResult(89) { m.methodWith11Ints(1,1,1,1,1,1,1,1,1,1,1) }

      m.expects('methodWith12Ints)(1,1,1,1,1,1,1,1,1,1,1,1).returning(88)
      assertResult(88) { m.methodWith12Ints(1,1,1,1,1,1,1,1,1,1,1,1) }

      m.expects('methodWith13Ints)(1,1,1,1,1,1,1,1,1,1,1,1,1).returning(87)
      assertResult(87) { m.methodWith13Ints(1,1,1,1,1,1,1,1,1,1,1,1,1) }

      m.expects('methodWith14Ints)(1,1,1,1,1,1,1,1,1,1,1,1,1,1).returning(86)
      assertResult(86) { m.methodWith14Ints(1,1,1,1,1,1,1,1,1,1,1,1,1,1) }

      m.expects('methodWith15Ints)(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1).returning(85)
      assertResult(85) { m.methodWith15Ints(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1) }

      m.expects('methodWith16Ints)(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1).returning(84)
      assertResult(84) { m.methodWith16Ints(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1) }

      m.expects('methodWith17Ints)(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1).returning(83)
      assertResult(83) { m.methodWith17Ints(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1) }

      m.expects('methodWith18Ints)(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1).returning(82)
      assertResult(82) { m.methodWith18Ints(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1) }

      m.expects('methodWith19Ints)(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1).returning(81)
      assertResult(81) { m.methodWith19Ints(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1) }

      m.expects('methodWith20Ints)(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1).returning(80)
      assertResult(80) { m.methodWith20Ints(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1) }

      m.expects('methodWith21Ints)(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1).returning(79)
      assertResult(79) { m.methodWith21Ints(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1) }

      m.expects('methodWith22Ints)(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1).returning(78)
      assertResult(78) { m.methodWith22Ints(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1) }
    }
  }
}
