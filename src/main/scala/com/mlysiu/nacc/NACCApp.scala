package com.mlysiu.nacc

import java.io.File

import com.mlysiu.nacc.dataprovider.DataProviderFromFile
import com.mlysiu.nacc.engine.NACCEngine
import com.mlysiu.nacc.parser.Parser


object NACCApp {

  def main(args: Array[String]): Unit = {
    require(args.length == 1, "Wrong number of input parameters")
    val path: String = args(0)
    require(new File(path).isFile, s"Provided file [$path] does not exist or is not accessible")

    val res = for {
      parsedData <- Parser(new DataProviderFromFile(path)).parse
      ancc <- NACCEngine.calculateACC(parsedData)
    } yield ancc

    println(res)
  }

}
