package com.mlysiu.nacc.domain

import com.typesafe.scalalogging.Logger

/**
  *
  * @param id id of the node
  * @param neighboursMaybe all nodes that are nearest neighbours
  */
case class Node(id: Long, neighboursMaybe: Option[Seq[Node]] = None) {
  val Log = Logger("com.mlysiu.nacc.domain")

  val neighbours: Seq[Node] = neighboursMaybe.getOrElse(Seq())

  def calculateClusterCoefficient: Double = {
    val nV = neighbours.count(_.neighbours.map(_.id).exists(neighbours.map(_.id).contains))
    Log.info("Number of edges between neighbours for node [{}] is: [{}]", id.toString, nV.toString)

    val kV = neighbours.length
    Log.info("Number of neighbours for node [{}] is: [{}]", id.toString, kV.toString)

    (2 * nV: Double) / (kV * (kV - 1): Double)
  }
}

case class Link(fromId: Long, toId: Long)