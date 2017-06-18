package com.mlysiu.nacc.parser

import com.mlysiu.nacc.dataprovider.DataProvider
import com.mlysiu.nacc.domain.Link

import scala.util.Try

//TODO: Improvement: Use typeclass pattern
case class Parser(dataProvider: DataProvider, noOFLinesToDropMaybe: Option[Int] = None) {

  //TODO: Make this configurable
  val lineSeparator: String = "\n"
  val noOfLinesToDrop: Int = noOFLinesToDropMaybe.getOrElse(4)

  //TODO: Add more informative type like Either as a return type
  def parse: Try[Seq[Link]] = Try(dataProvider.readAll.split(lineSeparator).drop(noOfLinesToDrop).map { line =>
    line.trim().split(" ").filterNot(_.isEmpty).map(_.trim()).toList match {
      case el1 :: el2 :: Nil => Link(el1.toLong, el2.toLong)
      case m => throw new NumberFormatException(s"Illegal format of parsing data: [$m] while it should be [number number]")
    }
  })

}
