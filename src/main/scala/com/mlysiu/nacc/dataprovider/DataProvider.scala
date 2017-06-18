package com.mlysiu.nacc.dataprovider

import scala.util.Try

trait DataProvider {
  def readAll: Try[String]
}

class DataProviderFromFile(path: String) extends DataProvider {
  override def readAll: Try[String] = Try(scala.io.Source.fromFile(path).mkString)
}
