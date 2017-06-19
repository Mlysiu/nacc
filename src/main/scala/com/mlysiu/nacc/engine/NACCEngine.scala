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
  def calculateLCC(links: Map[Long, ArrayBuffer[Link]]): Set[Double] = {
    Log.info("Found unique [{}] nodes to convert", links.size)
    var keyCount = links.keys.size
    val ret = links.map { case (id, neighbourL) =>
      keyCount -= 1
      val ret = convertLinkToNode(id, neighbourL, links).calculateClusterCoefficient
      Log.info("Converted links for id [{}] to node. Left [{}] ids to convert", id.toString, keyCount.toString)
        ret
    }.toSet
    Log.info("Finished converting links to nodes, from [{}] links to [{}] nodes", links.size.toString, ret.size.toString)
    ret
  }

  def convertLinkToNode(linkId: Long, neighbourL: ArrayBuffer[Link], links: Map[Long, ArrayBuffer[Link]]) = {
    val neighboursIds = neighbourL.map(_.toId)
    val faster = neighboursIds.flatMap(nId => links.get(nId)).flatten

    val notExists = neighboursIds.filterNot(nId => faster.exists(lid => lid.fromId == nId && lid.toId != linkId))
    val neighbourNodesMaybe =
      faster.groupBy(_.fromId)
        .map { case (id, ls) =>
          val red = ls.map(_.toId).foldLeft(ArrayBuffer[Long]()) { case (array, toId) => array += toId }
          Node(id, red.map(Node(_)))
        }.toSeq
    Node(linkId, Some(neighbourNodesMaybe ++ notExists.map(Node(_))))
  }

  implicit def emptySeqToNoneElseSome[T](seq: Seq[T]): Option[Seq[T]] = seq match {
    case Nil => None
    case any => Some(any)
  }

  def calculateACC(links: Seq[Link]): Try[Double] = {
    Try(calculateLCC(ArrayBuffer(links: _*).groupBy(_.fromId))).map { lccs =>
      lccs.sum / lccs.size: Double
    }
  }
}
