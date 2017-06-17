package com.mlysiu.nacc.engine

import com.mlysiu.nacc.domain.{Link, Node}


object NACCEngine {

  /**
    * Convert Links to Nodes containing neighbours of neighbour of currently processed node.
    *
    * @return
    */
  def convertLink2Nodes(links: Seq[Link]): Set[Node] = {
    val segregatedLinks = links.groupBy(_.fromId)
    segregatedLinks.map { case (id, neighbourL) =>
      val neighboursIds = neighbourL.map(_.toId)
      val (exists, notExists) = neighboursIds.partition(nId => links.exists(lid => lid.fromId == nId && lid.toId != id))
      val neighboursNLMaybe: Option[Seq[Link]] = links.filter(link => exists.contains(link.fromId))

      val neighbourNodesMaybe = neighboursNLMaybe.map { neighboursNL =>
        neighboursNL
          .groupBy(_.fromId)
          .map { case (_, ls) =>
            ls.map(link => Node(link.fromId, Some(Seq(Node(link.toId)))))
              .reduce { (n1, n2) => Node(n1.id, n1.neighboursMaybe.flatMap(nodes1 => n2.neighboursMaybe.map(nodes2 => nodes1 ++ nodes2))) }
          }
          .toSeq
      }
      Node(id, ?++?(neighbourNodesMaybe, notExists.map(Node(_))))
    }.toSet
  }

  implicit def emptySeqToNoneElseSome[T](seq: Seq[T]): Option[Seq[T]] = seq match {
    case Nil => None
    case any => Some(any)
  }

  def ?++?[T](seqMaybe1: Option[Seq[T]], seqMaybe2: Option[Seq[T]]): Option[Seq[T]] = {
    if (seqMaybe1.isEmpty)
      seqMaybe2
    else if (seqMaybe2.isEmpty)
      seqMaybe1
    else
      seqMaybe1.flatMap(seq1 => seqMaybe2.map(seq1 ++ _))
  }
}
