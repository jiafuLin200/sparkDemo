package com.jf.spark

import breeze.linalg.{Axis, DenseMatrix, DenseVector, accumulate, all, any, argmax, det, diag,
  inv, lowerTriangular, max, pinv, sum, trace, upperTriangular}
import breeze.numerics._


/**
  * 矩阵向量等操作
  */
object ScalaDemo02 {
  /**
    * 创建全0矩阵
    * 0.0  0.0  0.0
    * 0.0  0.0  0.0
    */
  def test01(): Unit = {
    val m1 = DenseMatrix.zeros[Double](2, 3)
    println(m1)
  }


  /**
    * 创建全0向量
    * DenseVector(0.0, 0.0)
    */
  def test02(): Unit = {
    val v = DenseVector.zeros[Double](2)
    println(v)
  }


  /**
    * 创建全0向量
    * DenseVector(1.0, 1.0)
    */
  def test03(): Unit = {
    val o = DenseVector.ones[Double](2)
    println(o)
  }


  /**
    * 创建向量(根据给定值)
    * DenseVector(4.0, 4.0, 4.0)
    */
  def test04(): Unit = {
    val o = DenseVector.fill[Double](3) {
      4
    }
    println(o)
  }


  /**
    * 创建向量（根据range和step）
    * DenseVector(1, 4, 7)
    */
  def test05(): Unit = {
    val o = DenseVector.range(1, 10, 3)
    println(o)
  }

  /**
    * 创建单位矩阵
    * 1.0  0.0  0.0  0.0
    * 0.0  1.0  0.0  0.0
    * 0.0  0.0  1.0  0.0
    * 0.0  0.0  0.0  1.0
    */
  def test06(): Unit = {
    val o = DenseMatrix.eye[Double](4)
    println(o)
  }


  /**
    * 根据向量创建对角矩阵
    * 1.0  0.0  0.0
    * 0.0  2.0  0.0
    * 0.0  0.0  3.3
    */
  def test07(): Unit = {
    val o = diag(DenseVector(1.0, 2.0, 3.3))
    println(o)
  }


  /**
    * 按照行创建矩阵
    * 1.0  2.0
    * 3.0  4.0
    */
  def test08(): Unit = {
    val o = DenseMatrix((1.0, 2.0), (3.0, 4.0))
    println(o)
  }


  /**
    * 函数创建向量
    * DenseVector(0, 2, 4)
    */
  def test09(): Unit = {
    val o = DenseVector.tabulate(3) { i => 2 * i }
    println(o)
  }


  /**
    * 函数创建矩阵
    * 0  0  0
    * 0  1  2
    * 0  2  4
    */
  def test10(): Unit = {
    val o = DenseMatrix.tabulate(3, 3) { case (i, j) => i * j }
    println(o)
  }


  /**
    * 从数组创建向量
    * DenseVector(1, 2, 3, 4)
    */
  def test11(): Unit = {
    val o = new DenseVector(Array(1, 2, 3, 4))
    println(o)
  }


  /**
    * 从数组创建矩阵
    * 11  13  22
    * 12  21  12
    */
  def test12(): Unit = {
    val o = new DenseMatrix(2, 3, Array(11, 12, 13, 21, 22, 12))
    println(o)
  }


  /**
    * 创建随机向量
    * DenseVector(0.8172011851694525, 0.1295047203017068, 0.841108289620901)
    */
  def test13(): Unit = {
    val o = DenseVector.rand(3)
    println(o)
  }


  /**
    * 创建随机矩阵
    * 0.7518856160117908  0.1394099600069849  0.49228408495140674
    * 0.6628369668339975  0.619643457188195   0.5799737052370557
    */
  def test14(): Unit = {
    val o = DenseMatrix.rand(2, 3)
    println(o)
  }


  /**
    * 元素访问
    * 0  1  2
    * 1  2  3
    * 2  3  4
    * 3
    */
  def test15(): Unit = {
    val o = DenseMatrix.tabulate(3, 3) { case (i, j) => i + j }
    println(o)
    println(o(1, 2))
  }


  /**
    * 元素访问->向量子集
    * DenseVector(0, 2, 4)
    * DenseVector(2, 4)
    * DenseVector(4, 2, 0)
    * DenseVector(2, 4)
    */
  def test16(): Unit = {
    val o = DenseVector.tabulate(3) { i => 2 * i }
    println(o)
    println(o(1 to 2))
    println(o(2 to 0 by -1))
    println(o(1 to -1))
  }


  /**
    * 元素访问->矩阵指定行列
    * 0  1  2
    * 1  2  3
    * 2  3  4
    * Transpose(DenseVector(2, 3, 4))
    * DenseVector(2, 3, 4)
    */
  def test17(): Unit = {
    val o = DenseMatrix.tabulate(3, 3) { case (i, j) => i + j }
    println(o)
    println(o(2, ::))
    println(o(::, 2))
  }


  /**
    * 元素操作
    * 1.0  2.0  3.0
    * 3.0  4.0  5.0
    * -----------------
    * 1.0  4.0
    * 3.0  3.0
    * 2.0  5.0
    * -----------------
    * DenseVector(1.0, 3.0, 2.0, 4.0, 3.0, 5.0)
    */
  def test18(): Unit = {
    val m = DenseMatrix((1.0, 2.0, 3.0), (3.0, 4.0, 5.0))
    println(m)
    println("-----------------")
    println(m.reshape(3, 2))
    println("-----------------")
    println(m.toDenseVector)
  }


  /**
    * 元素操作
    * 1.0  2.0  3.0
    * 4.0  5.0  6.0
    * 7.0  8.0  9.0
    * -----------------
    * 1.0  0.0  0.0
    * 4.0  5.0  0.0
    * 7.0  8.0  9.0
    * -----------------
    * 1.0  2.0  3.0
    * 0.0  5.0  6.0
    * 0.0  0.0  9.0
    */
  def test19(): Unit = {
    val m = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0), (7.0, 8.0, 9.0))
    println(m)
    println("-----------------")
    println(lowerTriangular(m))
    println("-----------------")
    println(upperTriangular(m))
  }


  /**
    * 元素操作
    * 1.0  2.0  3.0
    * 4.0  5.0  6.0
    * 7.0  8.0  9.0
    * -----------------
    * 1.0  2.0  3.0
    * 4.0  5.0  6.0
    * 7.0  8.0  9.0
    * -----------------
    * DenseVector(1.0, 5.0, 9.0)
    */
  def test20(): Unit = {
    val m = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0), (7.0, 8.0, 9.0))
    println(m)
    println("-----------------")
    println(m.copy)
    println("-----------------")
    println(diag(m))
  }


  /**
    * 元素操作
    * 1.0  2.0  3.0
    * 4.0  5.0  6.0
    * 7.0  8.0  9.0
    * -----------------
    * 1.0  2.0  5.0
    * 4.0  5.0  5.0
    * 7.0  8.0  5.0
    * -----------------
    * 1.0  2.0  5.0
    * 2.1  2.1  5.0
    * 2.1  2.1  5.0
    * -----------------
    */
  def test21(): Unit = {
    val m = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0), (7.0, 8.0, 9.0))
    println(m)
    println("-----------------")
    m(::, 2) := 5.0
    println(m)
    println("-----------------")
    m(1 to 2, 0 to 1) := 2.1
    println(m)
    println("-----------------")
  }


  /**
    * 元素操作
    * DenseVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    * -----------------
    * DenseVector(100, 100, 100)
    * -----------------
    * DenseVector(1, 100, 100, 100, 5, 6, 7, 8, 9, 10)
    * -----------------
    */
  def test22(): Unit = {
    val v = DenseVector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    println(v)
    println("-----------------")
    println(v(1 to 3) := 100)
    println("-----------------")
    println(v)
    println("-----------------")
  }


  /**
    * 元素操作
    * 1.0  2.0  3.0
    * 4.0  5.0  6.0
    * 1.0  1.0  1.0
    * 2.0  2.0  2.0
    * -----------------
    * 1.0  2.0  3.0  1.0  1.0  1.0
    * 4.0  5.0  6.0  2.0  2.0  2.0
    * -----------------
    */
  def test23(): Unit = {
    val m1 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0))
    val m2 = DenseMatrix((1.0, 1.0, 1.0), (2.0, 2.0, 2.0))
    println(DenseMatrix.vertcat(m1, m2))
    println("-----------------")
    println(DenseMatrix.horzcat(m1, m2))
    println("-----------------")
  }


  /**
    * 元素操作
    * DenseVector(1, 2, 3, 4, 1, 1, 5, 6)
    * -----------------
    * 1  1
    * 2  1
    * 3  5
    * 4  6
    * -----------------
    */
  def test24(): Unit = {
    val v1 = DenseVector(1, 2, 3, 4)
    val v2 = DenseVector(1, 1, 5, 6)
    println(DenseVector.vertcat(v1, v2))
    println("-----------------")
    println(DenseVector.horzcat(v1, v2))
    println("-----------------")
  }


  /**
    * 数值计算
    * -------m1----------
    * 1.0  2.0  3.0
    * 4.0  5.0  6.0
    * -------m2----------
    * 1.0  1.0  1.0
    * 2.0  2.0  2.0
    * -------m1+m2----------
    * 2.0  3.0  4.0
    * 6.0  7.0  8.0
    * -------m1-m2----------
    * 0.0  1.0  2.0
    * 2.0  3.0  4.0
    * -------m1:*m2----------
    * 1.0  2.0   3.0
    * 8.0  10.0  12.0
    * -------m1:/m2----------
    * 1.0  2.0  3.0
    * 2.0  2.5  3.0
    * -----------------
    */
  def test25(): Unit = {
    val m1 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0))
    val m2 = DenseMatrix((1.0, 1.0, 1.0), (2.0, 2.0, 2.0))
    println("-------m1----------")
    println(m1)
    println("-------m2----------")
    println(m2)
    println("-------m1+m2----------")
    println(m1 + m2)
    println("-------m1-m2----------")
    println(m1 - m2)
    println("-------m1:*m2----------")
    println(m1 :* m2)
    println("-------m1:/m2----------")
    println(m1 :/ m2)
    println("-----------------")
  }


  /**
    * 数值计算
    * -------m1----------
    * 1.0  2.0  3.0
    * 4.0  5.0  6.0
    * -------m2----------
    * 1.0  1.0  1.0
    * 2.0  2.0  2.0
    * -------m1:<m2----------
    * false  false  false
    * false  false  false
    * -------m1:==m2----------
    * true   false  false
    * false  false  false
    */
  def test26(): Unit = {
    val m1 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0))
    val m2 = DenseMatrix((1.0, 1.0, 1.0), (2.0, 2.0, 2.0))
    println("-------m1----------")
    println(m1)
    println("-------m2----------")
    println(m2)
    println("-------m1:<m2----------")
    println(m1 :< m2)
    println("-------m1:==m2----------")
    println(m1 :== m2)
  }


  /**
    * 数值计算
    * -------m1----------
    * 1.0  2.0  3.0
    * 4.0  5.0  6.0
    * -------m1:+=1.0----------
    * 2.0  3.0  4.0
    * 5.0  6.0  7.0
    * -------m1:*=2.0----------
    * 4.0   6.0   8.0
    * 10.0  12.0  14.0
    * -------max(m1)----------
    * 14.0
    * -------argmax(m1)-------//元素最大值位置
    * (1,2)
    */
  def test27(): Unit = {
    val m1 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0))
    println("-------m1----------")
    println(m1)
    println("-------m1:+=1.0----------")
    println(m1 :+= 1.0)
    println("-------m1:*=2.0----------")
    println(m1 :*= 2.0)
    println("-------max(m1)----------")
    println(max(m1))
    println("-------argmax(m1)----------")
    println(argmax(m1))
  }


  /**
    * 求和函数
    * 1.0  2.0  3.0
    * 4.0  5.0  6.0
    * -------sum(m1)----------
    * 21.0
    * -------sum(m1, Axis._0)----------
    * Transpose(DenseVector(5.0, 7.0, 9.0))
    * -------sum(m1, Axis._1)----------
    * DenseVector(6.0, 15.0)
    */
  def test28(): Unit = {
    val m1 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0))
    println(m1)
    println("-------sum(m1)----------")
    println(sum(m1))
    println("-------sum(m1, Axis._0)----------")
    println(sum(m1, Axis._0))
    println("-------sum(m1, Axis._1)----------")
    println(sum(m1, Axis._1))

  }


  /**
    * 求和函数
    * 1.0  2.0  3.0
    * 4.0  5.0  6.0
    * 7.0  8.0  9.0
    * -------trace(m1)--------对角线元素和
    * 15.0
    * -------accumulate-------累积和
    * DenseVector(1, 3, 6, 10)
    */
  def test29(): Unit = {
    val m1 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0), (7.0, 8.0, 9.0))
    println(m1)
    println("-------trace(m1)--------对角线元素和")
    println(trace(m1))
    println("-------accumulate-------累积和")
    println(accumulate(DenseVector(1, 2, 3, 4)))
  }


  /**
    * 布尔函数
    * ----------b1 :&b2---------
    * DenseVector(false, false, true)
    * ----------b1 :|b2---------
    * DenseVector(true, false, true)
    * ----------!b1---------
    * DenseVector(false, true, false)
    */
  def test30(): Unit = {
    val b1 = DenseVector(true, false, true)
    val b2 = DenseVector(false, false, true)
    println("----------b1 :&b2---------")
    println(b1 :& b2)
    println("----------b1 :|b2---------")
    println(b1 :| b2)
    println("----------!b1---------")
    println(!b1)
  }


  /**
    * 布尔函数
    * ----------any(v1)---------有元素非零
    * true
    * ----------all(v1)---------所有元素非零
    * false
    */
  def test31(): Unit = {
    val v1 = DenseVector(1.0, 0.0, -2.0)
    println("----------any(v1)---------有元素非零")
    println(any(v1))
    println("----------all(v1)---------所有元素非零")
    println(all(v1))
  }


  /**
    * 线性代数函数
    * ----------v1\v2----------线性求解
    * -2.5  -2.5  -2.5
    * 4.0   4.0   4.0
    * -1.5  -1.5  -1.5
    * ----------v1.t-----------转置
    * 1.0  4.0  7.0
    * 2.0  5.0  8.0
    * 3.0  6.0  9.0
    */
  def test32(): Unit = {
    val v1 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0), (7.0, 8.0, 9.0))
    val v2 = DenseMatrix((1.0, 1.0, 1.0), (1.0, 1.0, 1.0), (1.0, 1.0, 1.0))
    println("----------v1\\v2---------线性求解")
    println(v1 \ v2)
    println("----------v1.t-----------转置")
    println(v1.t)
  }


  /**
    * 线性代数函数
    * ----------det(v1)---------求特征值
    * 6.661338147750939E-16
    * ----------inv(v1)-----------求逆
    * -4.503599627370499E15  9.007199254740992E15    -4.503599627370495E15
    * 9.007199254740998E15   -1.8014398509481984E16  9.007199254740991E15
    * -4.503599627370498E15  9.007199254740992E15    -4.5035996273704955E15
    * ----------pinv(v1)-----------求伪逆
    * -3.7720834019330525E14  7.544166803866101E14    -3.77208340193305E14
    * 7.544166803866094E14    -1.5088333607732208E15  7.544166803866108E14
    * -3.772083401933041E14   7.544166803866104E14    -3.772083401933055E14
    */
  def test33(): Unit = {
    val v1 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0), (7.0, 8.0, 9.0))
    val v2 = DenseMatrix((1.0, 1.0, 1.0), (1.0, 1.0, 1.0), (1.0, 1.0, 1.0))
    println("----------det(v1)---------求特征值")
    println(det(v1))
    println("----------inv(v1)-----------求逆")
    println(inv(v1))
    println("----------pinv(v1)-----------求伪逆")
    println(pinv(v1))
  }


  /**
    * 线性代数函数
    * ----------v1.rows-----------
    * 3
    * ----------v1.cols-----------
    * 3
    */
  def test34(): Unit = {
    val v1 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0), (7.0, 8.0, 9.0))
    println("----------v1.rows-----------")
    println(v1.rows)
    println("----------v1.cols-----------")
    println(v1.cols)
  }


  /**
    * 取整函数
    * ----------round(v1)-----------
    * 1  2  3
    * 4  6  7
    * 8  9  10
    * ----------ceil(v1)-----------
    * 2.0  3.0  4.0
    * 5.0  6.0  7.0
    * 8.0  9.0  10.0
    * ----------floor(v1)-----------
    * 1.0  2.0  3.0
    * 4.0  5.0  6.0
    * 7.0  8.0  9.0
    */
  def test35(): Unit = {
    val v1 = DenseMatrix((1.1, 2.2, 3.3), (4.4, 5.5, 6.6), (7.7, 8.8, 9.9))
    println("----------round(v1)-----------")
    println(round(v1))
    println("----------ceil(v1)-----------")
    println(ceil(v1))
    println("----------floor(v1)-----------")
    println(floor(v1))
  }


  /**
    * 取整函数
    * ----------signum(v1)--------符号函数
    * 1.0  -1.0  1.0
    * 1.0  -1.0  1.0
    * 1.0  -1.0  1.0
    * ----------abs(v1)-----------取正函数
    * 1.1  2.2  3.3
    * 4.4  5.5  6.6
    * 7.7  8.8  9.9
    */
  def test36(): Unit = {
    val v1 = DenseMatrix((1.1, -2.2, 3.3), (4.4, -5.5, 6.6), (7.7, -8.8, 9.9))
    println("----------signum(v1)--------符号函数")
    println(signum(v1))
    println("----------abs(v1)-----------取正函数")
    println(abs(v1))
  }


  def main(args: Array[String]): Unit = {
    test36()
  }

}
