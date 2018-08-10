package com.jiafu.spark

import org.apache.spark.{Partition, SparkConf, SparkContext}

class RddOperator {

}

object RddOperator {

  /**
    * 测试RDD map
    * Return a new distributed dataset formed by passing each element of
    * the source through a function func.
    */
  def test01(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("map")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(List(1, 2, 4, 5, 7), 2).map((x) => (x + 1)).collect()
    rdd.foreach(x => println(x))
  }


  /**
    * 测试RDD flatMap
    * Similar to map, but each input item can be mapped to 0 or more output
    * items (so func should return a Seq rather than a single item).
    */
  def test02(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("flatMap")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(List("workhard", "smart", "attitude", "good"), 2)
      .flatMap(x => x.toCharArray)
      .map((ch: Char) => Tuple2(ch, 1))
      .reduceByKey((x, y) => (x + y))
    rdd.foreach((t) => println(t._1 + ":" + t._2))
  }

  /**
    * 测试RDD flatMap
    * Similar to map, but each input item can be mapped to 0 or more output
    * items (so func should return a Seq rather than a single item).
    */
  def test03(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("flatMap")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(List("workhard", "smart", "attitude", "good"), 2)
      .flatMap(x => x.toCharArray)
      .map((ch: Char) => (ch, 1))
      .reduceByKey((x, y) => (x + y))
    rdd.foreach((t) => println(t._1 + ":" + t._2))
  }


  /**
    * 保留还有“a”的字符串
    * 测试RDD filter
    * Return a new dataset formed by selecting those elements of
    * the source on which func returns true.
    */
  def test04(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("filter")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(List("workhard", "smart", "attitude", "good"), 2)
      .filter(s => s.contains("a"))
    rdd.foreach((t) => println(t))
  }


  /**
    * 测试RDD mapPartitions
    *
    * Similar to map, but runs separately on each partition (block) of the RDD,
    * so func must be of type Iterator<T> => Iterator<U> when running on an RDD of type T.
    */
  def test05(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("mapPartitions")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(List("workhard", "smart", "attitude", "good"), 2)
      .mapPartitions((it: Iterator[String]) => {
        it.map(x => (x + "."))
      })

    rdd.foreach((t) => println(t))
  }


  /**
    * 测试RDD mapPartitionsWithIndex
    *
    * Similar to mapPartitions, but also provides func with an integer value representing
    * the index of the partition, so func must be of type (Int, Iterator<T>) => Iterator<U>
    * when running on an RDD of type T.
    *
    */
  def test06(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("mapPartitionsWithIndex")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(List("work", "smart", "attitude", "good", "hard"), 2)
      .mapPartitionsWithIndex((x: Int, it: Iterator[String]) => {
        it.map(y => (x + ":" + y))
      })

    rdd.foreach((t) => println(t))
  }


  /**
    * 测试RDD sample
    *
    * withReplacement:是否放回抽样;
    * fraction:比例,0.1表示10%
    */
  def test07(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("sample")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(1 to 1000, 3)
      .sample(true, 0.5, 0)
    println(rdd.count())
  }

  /**
    * 测试RDD union两个集合合并
    *
    * sortBy:
    * 第一个参数是一个函数，该函数的也有一个带T泛型的参数，返回类型和RDD中元素的类型是一致的；
    * 第二个参数是ascending，从字面的意思大家应该可以猜到，是的，这参数决定排序后RDD中的元素是升序还是降序，默认是true，也就是升序；
    * 第三个参数是numPartitions，该参数决定排序后的RDD的分区个数，默认排序后的分区个数和排序之前的个数相等
    */
  def test08(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("union")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(1 to 5, 2)
    val rdd2 = sc.parallelize(11 to 15, 3)
    val rdd3 = rdd2.union(rdd1).sortBy(x => x, true, 1)
    rdd3.foreach((t) => println(t))
  }


  /**
    * 求两个rdd的交集
    * Return a new RDD that contains the intersection of elements
    * in the source dataset and the argument.
    */
  def test09(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("intersection")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(1 to 13, 2)
    val rdd2 = sc.parallelize(11 to 15, 3)
    val rdd3 = rdd1.intersection(rdd2, 2)
    rdd3.foreach((t) => println(t))
  }

  /**
    * 对集合中的数据去重
    * Return a new dataset that contains the distinct elements of the source dataset.
    */
  def test10(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("distinct")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(1 to 13, 2)
    val rdd2 = sc.parallelize(11 to 15, 3)
    val rdd3 = rdd1.union(rdd2).distinct(2).sortBy(x => x, true, 1)
    rdd3.foreach((t) => println(t))
  }

  /**
    * groupByKey
    */
  def test11(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("groupByKey")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(List(1, 1, 1, 2, 2, 2, 3, 3, 3))
    val rdd2 = sc.parallelize(1 to 9)
    val rdd3 = rdd1.zip(rdd2).groupByKey(1)

    rdd3.foreach((t) => println(t))
  }


  /**
    * groupBy
    */
  def test12(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("groupBy")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(1 to 8)
    val rdd2 = rdd1.groupBy(x => {
      if (x % 2 == 0) "1" else "2"
    })

    rdd2.foreach((t) => println(t))
  }

  /**
    * groupByKey
    */
  def test13(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("groupBy")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(List(1, 1, 1, 2, 2, 2, 3, 3, 3))
    val rdd2 = sc.parallelize(1 to 9)
    val rdd3 = rdd1.zip(rdd2).groupBy(x => x._1)

    rdd3.foreach((t) => println(t))
  }

  /**
    * reduce
    */
  def test14(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("reduce")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(List(1, 1, 1, 2, 2, 2, 3, 3, 3))
    val rdd2 = sc.parallelize(1 to 9)
    val v = rdd1.zip(rdd2).reduce((x, y) => (x._1 + y._2, x._2 + y._2))

    println(v)
  }


  /**
    * reduceByKey
    */
  def test15(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("reduceByKey")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(List(1, 1, 1, 2, 2, 2, 3, 3, 3))
    val rdd2 = sc.parallelize(1 to 9)
    val rdd3 = rdd1.zip(rdd2).reduceByKey((x, y) => x + y).sortBy(x => x._1, true, 1)

    rdd3.foreach(t => println(t))
  }


  /**
    * aggregateByKey
    */
  def test16(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("aggregateByKey")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(List(1, 1, 1, 2, 2, 2, 3, 3, 3), 2)
    val rdd2 = sc.parallelize(1 to 9, 2)
    val rdd3 = rdd1.zip(rdd2).aggregateByKey(0)(Math.max, Math.max)

    rdd3.foreach(t => println(t))
  }

  /**
    * aggregate
    */
  def test17(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("aggregate")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(1 to 9, 2)
    val v = rdd1.aggregate(0)(Math.max, _ + _)

    println(v)
  }


  /**
    * join
    */
  def test18(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("join")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(List((1, 2), (1, 3), (2, 1)), 3)
    val rdd2 = sc.parallelize(List((1, 1), (2, 3), (2, 8)), 3)

    val rdd3 = rdd1.join(rdd2)

    rdd3.foreach(t => println(t))
  }


  /**
    * cogroup
    */
  def test19(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("cogroup")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(List((1, 2), (2, 3), (3, 1)), 3)
    val rdd2 = sc.parallelize(List((1, 1), (2, 2), (2, 8)), 3)

    val rdd3 = rdd1.cogroup(rdd2, 1)

    rdd3.foreach(t => println(t))
  }

  /**
    * cartesian
    */
  def test20(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("cartesian")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(List((1, 2), (2, 3), (3, 1)), 3)
    val rdd2 = sc.parallelize(List((1, 1), (2, 2), (2, 8)), 3)

    val rdd3 = rdd1.cartesian(rdd2)

    rdd3.foreach(t => println(t))
  }

  /**
    * cartesian
    */
  def test21(): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("cartesian")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(1 to 9, 3)
    val rdd2 = sc.parallelize(1 to 9, 3)

    val rdd3 = rdd1.cartesian(rdd2)
      .sortBy(x=>x._1,true,1)
      .sortBy(x=>x._2,true)
        .map(x=>x._1)

    rdd3.foreach(t => println(t))
  }


  def main(args: Array[String]): Unit = {
    test21()
  }

}
