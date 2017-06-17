package com.mlysiu.nacc.engine

import com.mlysiu.nacc.domain.{Link, Node}
import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class NACCEngineTest extends FlatSpec {

  "NACCEngine" should " not convert one link to one node" in {
    NACCEngine.convertLink2Nodes(Seq(Link(1, 2))) should be (Set(Node(1, Some(List(Node(2))))))
  }

  it should "not convert two neighbour links to one node with two neighbours" in {
    NACCEngine.convertLink2Nodes(Seq(Link(1, 2), Link(1, 3))) should be (Set(Node(1, Some(List(Node(2), Node(3))))))
  }

  it should "convert three links including one between neighbour to one node with two neighbours" in {
    NACCEngine.convertLink2Nodes(Seq(Link(1, 2), Link(1, 3), Link(2, 3))) should be (Set(
      Node(2, Some(Seq(Node(3)))),
      Node(1, Some(Seq(Node(2, Some(Seq(Node(3)))), Node(3))))))
  }

  it should "convert five links including two nodes with neighbours having link to each other" in {
    NACCEngine.convertLink2Nodes(Seq(Link(1, 2), Link(1, 3), Link(2, 3), Link(2, 4), Link(4, 3))) should be (Set(
      Node(2,Some(List(Node(4,Some(List(Node(3,None)))), Node(3,None)))),
      Node(4,Some(List(Node(3,None)))),
      Node(1,Some(List(Node(2,Some(List(Node(3,None), Node(4,None)))), Node(3,None))))))
  }
}
