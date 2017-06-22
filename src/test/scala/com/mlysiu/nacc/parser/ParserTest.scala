package com.mlysiu.nacc.parser

import com.mlysiu.nacc.dataprovider.DataProvider
import com.mlysiu.nacc.domain.Link
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

import scala.util.Try

class ParserTest extends FlatSpec {

  "Parser" should "properly parse data with only one line of data" in {
    class TestDataProvider extends DataProvider {
      override def readAll: Try[String] = Try("1\t2")
    }
    Parser(new TestDataProvider, Some(0)).parse should be(Try(Seq(Link(1, 2))))
  }

  it should "properly parse data with five valid lines of data" in {
    class TestDataProvider extends DataProvider {
      override def readAll: Try[String] =
        Try("1\t2\r\n" +
          "3\t4\r\n" +
          "5\t6\r\n" +
          "34563456\t21\r\n" +
          "1222\t4\r\n")
    }
    Parser(new TestDataProvider, Some(0)).parse should be(Try(Seq(Link(1, 2), Link(3, 4), Link(5, 6), Link(34563456, 21), Link(1222, 4))))
  }

  it should "properly parse data with a some initial lines of not needed data in the string" in {
    class TestDataProvider extends DataProvider {
      override def readAll: Try[String] =
        Try("This is a header\r\n" +
          "this is a header\r\n" +
          "1\t2\r\n" +
          "3\t4\r\n")
    }
    Parser(new TestDataProvider, Some(2)).parse should be(Try(Seq(Link(1, 2), Link(3, 4))))
  }

}
