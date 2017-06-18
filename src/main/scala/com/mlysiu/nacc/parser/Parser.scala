package com.mlysiu.nacc.parser

import com.mlysiu.nacc.dataprovider.DataProvider
import com.mlysiu.nacc.domain.Link
import com.typesafe.scalalogging.Logger

import scala.util.Try

//TODO: Improvement: Use typeclass pattern
case class Parser(dataProvider: DataProvider, noOFLinesToDropMaybe: Option[Int] = None) {

  val Log = Logger("com.mlysiu.nacc.parser")

  //TODO: Make this configurable
  val lineSeparator: String = "\r\n"
  val noOfLinesToDrop: Int = noOFLinesToDropMaybe.getOrElse(4)

  //TODO: Add more informative type like Either as a return type
  def parse: Try[Seq[Link]] =
    for {
      data <- dataProvider.readAll
      parsedData <- parseData(data)
    } yield parsedData

  private def parseData(data: String): Try[Seq[Link]] = Try {
    val parsedData = data.split(lineSeparator).drop(noOfLinesToDrop).map { line =>
      line.trim().split("\t").filterNot(_.isEmpty).map(_.trim()).toList match {
        case el1 :: el2 :: Nil => Link(el1.toLong, el2.toLong)
        case m => throw new NumberFormatException(s"Illegal format of parsing data: [$m] while it should be [number number]")
      }
    }
    Log.info("Finished parsing the data with [{}] Links", parsedData.size)
    parsedData
  }

}
