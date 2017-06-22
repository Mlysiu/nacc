package com.mlysiu.nacc.engine

import com.mlysiu.nacc.domain.{Link, Node}
import com.typesafe.scalalogging.Logger

import scala.collection.mutable.ArrayBuffer
import scala.util.Try


object NACCEngine {
  val Log = Logger("com.mlysiu.nacc.engine")

  /**
    * Convert Links to Nodes containing neighbours of neighbour of currently processed node.
    *
    * @return
    */
  def calculateLCC(linksGroupedByFromId: Map[Long, ArrayBuffer[Link]]): Seq[Double] =
    linksGroupedByFromId.map { case (id, neighbourL) =>
      convertLinkToNode(id, neighbourL, linksGroupedByFromId).calculateClusterCoefficient
    }.toSeq

  def convertLinkToNode(nodeId: Long, fromNodeLinks: ArrayBuffer[Link], linksGroupedByFromId: Map[Long, ArrayBuffer[Link]]): Node = {
    val neighboursIds = fromNodeLinks.map(_.toId)
    val fromNeighbourLinks = neighboursIds.flatMap(nId => linksGroupedByFromId.get(nId)).flatten

    val (neighNodes, isolatedNodes) = neighboursIds.partition(nId => fromNeighbourLinks.exists(lid => lid.fromId == nId && lid.toId != nodeId))
    val neighNodesWithNeigh =
      fromNeighbourLinks.filter(link => neighNodes.contains(link.fromId)).groupBy(_.fromId)
        .map { case (id, ls) =>
          val red = ls.foldLeft(ArrayBuffer[Node]()) { case (array, link) => array += Node(link.toId) }
          Node(id, red)
        }.toSeq

    require((neighNodesWithNeigh ++ isolatedNodes.map(Node(_))).size == neighboursIds.length, s"Number of neighbours must be the same!. failed for nodeId $nodeId")
    Node(nodeId, Some(neighNodesWithNeigh ++ isolatedNodes.map(Node(_))))
  }

  implicit def emptySeqToNoneElseSome[T](seq: Seq[T]): Option[Seq[T]] = seq match {
    case Nil => None
    case any => Some(any)
  }

  def calculateACC(links: Seq[Link]): Try[Double] = {
    val isolatedNodes = links.map(_.toId).toSet.diff(links.map(_.fromId).toSet)
    val isolatedLinks = links.filter(link => isolatedNodes.contains(link.toId))

    Log.info("Found [{}] isolated nodes", isolatedNodes.size.toString)

    val nodeIdToNeighbourLinks = ArrayBuffer(links: _*).groupBy(_.fromId) ++ ArrayBuffer(isolatedLinks: _*).groupBy(_.toId)

    Log.info("Calculating Average Cluster Coefficient for total number of [{}] nodes", nodeIdToNeighbourLinks.keys.size.toString)

    Try(calculateLCC(nodeIdToNeighbourLinks)).map(lccs => lccs.sum / lccs.length: Double)
  }
}
