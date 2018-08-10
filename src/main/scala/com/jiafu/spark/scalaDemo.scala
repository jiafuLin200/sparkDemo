package com.jiafu.spark

import breeze.linalg.{DenseMatrix, DenseVector}
import org.apache.spark.{SparkConf, SparkContext}
import org.elasticsearch.spark.rdd.EsSpark

object scalaDemo {

  def test01(): Unit = {
    val conf = new SparkConf().setMaster("local[3]").setAppName("scala_spark")
    val sc = new SparkContext(conf)

    val readRdd = sc.textFile("/home/jiafu/test/a.txt")
    println(readRdd.count())

    val charArray = readRdd.flatMap(s => s.toCharArray)
    val result = charArray.map(ch => new Tuple2(ch, 1)).reduceByKey((x, y) => (x + y))
    //    result.saveAsTextFile("/home/jiafu/test/scala.wordCount3")
    result.sortByKey(true).collect().foreach((x) => {
      println(x)
    })

    sc.stop()
  }


  def test02(): Unit = {
    val conf = new SparkConf().setMaster("local[3]").setAppName("scala_spark")
    conf.set("es.nodes", "10.1.1.23")
    conf.set("es.port", "9200")
    conf.set("es.index.auto.create", "true")
    val sc = new SparkContext(conf)

    val numbers = Map("one" -> 1, "two" -> 2, "three" -> 3)
    val airports = Map("OTP" -> "Otopeni", "SFO" -> "San Fran")


    EsSpark.saveToEs(sc.makeRDD(Seq(numbers, airports)), "/sparktest/1416")
    //    println(Seq(numbers, airports))


  }


  def test03(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("test03")
    val res = new SparkContext(conf).parallelize(List(1, 2, 4, 5, 7)).filter(_ > 4)
      .saveAsTextFile("/home/jiafu/test.txt")
  }


  def test04():Unit ={
    val zeros = DenseVector.tabulate(4){i => 2*i}
    println(zeros)
  }

  def main(args: Array[String]): Unit = {
    test04()
  }

}
