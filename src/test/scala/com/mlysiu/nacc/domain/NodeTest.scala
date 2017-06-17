package com.mlysiu.nacc.domain

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class NodeTest extends FlatSpec {

  "Node" should "calculate Clustering Coefficient equal to zero when graph is a star" in {
    val lG = Node(1, Some(Seq(
      Node(2),
      Node(3),
      Node(4)
    )))
    lG.calculateClusterCoefficient should be(0.0)
  }

  it should "calculate Clustering Coefficient equal to 1 when graph is a clique" in {
    val lG = Node(1, Some(Seq(
      Node(2, Some(Seq(Node(1), Node(3), Node(4)))),
      Node(3, Some(Seq(Node(1), Node(2), Node(4)))),
      Node(4, Some(Seq(Node(1), Node(2), Node(4))))
    )))
    lG.calculateClusterCoefficient should be(1.0)
  }

  it should "calculate Clustering Coefficient equal to 1/6" in {
    val lG = Node(1, Some(Seq(
      Node(2, Some(Seq(Node(1), Node(3)))),
      Node(3, Some(Seq(Node(1)))),
      Node(4, Some(Seq(Node(1)))),
      Node(5, Some(Seq(Node(1))))
    )))
    lG.calculateClusterCoefficient should be(1.0 / 6.0)
  }
}
