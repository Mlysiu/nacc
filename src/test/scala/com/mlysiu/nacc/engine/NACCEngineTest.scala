package com.mlysiu.nacc.engine

import com.mlysiu.nacc.domain.{Link, Node}
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

import scala.collection.mutable.ArrayBuffer

class NACCEngineTest extends FlatSpec {

  "NACCEngine" should " convert one link to one node" in {
    NACCEngine.convertLinkToNode(1, ArrayBuffer(Link(1, 2)), ArrayBuffer(Link(1, 2)).groupBy(_.fromId)) should be(Node(1, Some(List(Node(2)))))
  }

  it should "convert two neighbour links to one node with two neighbours" in {
    NACCEngine.convertLinkToNode(1, ArrayBuffer(Link(1, 2), Link(1, 3)), ArrayBuffer(Link(1, 2), Link(1, 3)).groupBy(_.fromId)) should be(Node(1, Some(List(Node(2), Node(3)))))
  }

  it should "convert three links including one between neighbour to one node with two neighbours" in {
    NACCEngine.convertLinkToNode(1, ArrayBuffer(Link(1, 2), Link(1, 3)), ArrayBuffer(Link(1, 2), Link(1, 3), Link(2, 3)).groupBy(_.fromId)) should be(
      Node(1, Some(Seq(Node(2, Some(Seq(Node(3)))), Node(3)))))
  }

  it should "convert five links including two nodes with neighbours having link to each other" in {
    NACCEngine.convertLinkToNode(1, ArrayBuffer(Link(1, 2), Link(1, 3)), ArrayBuffer(Link(1, 2), Link(1, 3), Link(2, 3), Link(2, 4), Link(4, 3)).groupBy(_.fromId)) should be(
      Node(1, Some(List(Node(2, Some(List(Node(3), Node(4)))), Node(3)))))
  }

  it should "convert six links including two nodes with neighbours having link to each other and one not connected node" in {
    NACCEngine.convertLinkToNode(1, ArrayBuffer(Link(1, 2), Link(1, 3)), ArrayBuffer(Link(1, 2), Link(1, 3), Link(2, 3), Link(2, 4), Link(4, 3), Link(2, 6)).groupBy(_.fromId)) should be(
      Node(1, Some(List(Node(2, Some(List(Node(3), Node(4), Node(6)))), Node(3))))
    )
  }
}
