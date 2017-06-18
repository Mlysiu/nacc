package com.mlysiu.nacc.dataprovider

trait DataProvider {
  def readAll: String
}

class DataProviderFromFile(path: String) extends DataProvider {
  override def readAll: String = ???
}
