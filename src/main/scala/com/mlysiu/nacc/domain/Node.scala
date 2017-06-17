package com.mlysiu.nacc.domain

import com.typesafe.scalalogging.Logger

/**
  *
  * @param id id of the node
  * @param neighboursMaybe all nodes that are nearest neighbours
  */
case class Node(id: Long, neighboursMaybe: Option[Seq[Node]] = None) {
  val Log = Logger("com.mlysiu.nacc.domain")

  //Check whether each node in neighbours nodes will have a link to parent (this) one (if nonempty).
  require(neighboursMaybe.forall(_.map(_.neighboursMaybe.forall(_.exists(_.id == id))).reduce(_ && _)),
    s"No link to current node [$id] from neighbour nodes.")

  val neighbours: Seq[Node] = neighboursMaybe.getOrElse(Seq())

  def calculateClusterCoefficient: Double = {
    val nV = neighbours.count(_.neighbours.map(_.id).exists(neighbours.map(_.id).contains))
    Log.info("Number of edges between neighbours for node [{}] is: [{}]", id.toString, nV.toString)

    val kV = neighbours.length
    Log.info("Number of neighbours for node [{}] is: [{}]", id.toString, kV.toString)

    (2 * nV: Double) / (kV * (kV - 1): Double)
  }
}
