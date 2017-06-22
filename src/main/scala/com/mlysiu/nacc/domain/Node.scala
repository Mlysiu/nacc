package com.mlysiu.nacc.domain

import com.typesafe.scalalogging.Logger

/**
  *
  * @param id              id of the node
  * @param neighboursMaybe all nodes that are nearest neighbours
  */
case class Node(id: Long, neighboursMaybe: Option[Seq[Node]] = None) {
  val Log = Logger("com.mlysiu.nacc.domain")

  lazy val neighbours: Seq[Node] = neighboursMaybe.getOrElse(Seq())

  def calculateClusterCoefficient: Double = {
    val nV = neighbours.count(_.neighbours.map(_.id).exists(neighbours.map(_.id).contains))
    val kV = neighbours.length

    val lcc = (2 * nV: Double) / (kV * (kV - 1): Double)
    if (lcc.isNaN) 0.0
    else lcc
  }
}

case class Link(fromId: Long, toId: Long)