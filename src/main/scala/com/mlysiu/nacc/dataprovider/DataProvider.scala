package com.mlysiu.nacc.dataprovider

import com.typesafe.scalalogging.Logger

import scala.util.Try

trait DataProvider {
  def readAll: Try[String]
}

class DataProviderFromFile(path: String) extends DataProvider {
  val Log = Logger("com.mlysiu.nacc.dataprovider")

  override def readAll: Try[String] =
    Try {
      Log.info("Starting reading the file from [{}]", path)
      val str = scala.io.Source.fromFile(path)
      val ret = try str.mkString finally str.close()
      Log.info("Finished reading the file")
      ret
    }
}
