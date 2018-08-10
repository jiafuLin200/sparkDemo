/*
 * Copyright © Since 2018 www.isinonet.com Company
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jiafu.spark.demo;

import com.alibaba.fastjson.JSON;
import com.jiafu.spark.pojo.Person;
import org.apache.spark.Accumulator;
import org.apache.spark.Partitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Example01 {

  public static void test01() {
    SparkConf conf = new SparkConf().setMaster("local").setAppName("my app");
    JavaSparkContext sc = new JavaSparkContext(conf);
    JavaRDD<String> rdd = sc.textFile("/home/jiafu/test/a.txt");
    JavaRDD<String> rdd2 = rdd.filter(new Function<String, Boolean>() {
      public Boolean call(String s) throws Exception {
        return s.contains("hello");
      }
    });
    System.out.println(rdd2.count());
    sc.stop();
  }

  public static void test02() {
    SparkConf conf = new SparkConf().setMaster("local").setAppName("my app");
    JavaSparkContext sc = new JavaSparkContext(conf);
    JavaRDD<String> rdd = sc.textFile("/home/jiafu/test/a.txt");
    JavaPairRDD<Character, Integer> rdd2 =
      rdd.flatMap(new FlatMapFunction<String, Character>() {
        public Iterator<Character> call(String s) throws Exception {
          List<Character> chs = new ArrayList<Character>();
          for (int i = 0; i < s.length(); i++) {
            chs.add(s.charAt(i));
          }
          return chs.iterator();
        }
      }).mapToPair(new PairFunction<Character, Character, Integer>() {
        public Tuple2<Character, Integer> call(Character character) throws Exception {
          return new Tuple2<Character, Integer>(character, 1);
        }
      }).partitionBy(new Partitioner() {
        @Override
        public int getPartition(Object key) {
          return Math.abs(key.hashCode() % 10);
        }

        @Override
        public int numPartitions() {
          return 10;
        }
      })
        .reduceByKey(new Function2<Integer, Integer, Integer>() {
          public Integer call(Integer integer, Integer integer2) throws Exception {
            return integer + integer2;
          }
        }).sortByKey(true);

    for (Tuple2 t : rdd2.take(10)) {
      System.out.println(t);
    }

//    System.out.println(rdd2.count());
//    rdd2.saveAsTextFile("/home/jiafu/test/b1.txt");
    sc.stop();
  }


  /**
   * 写入json文件
   */
  public static void test03() {
    SparkConf conf = new SparkConf().setMaster("local").setAppName("my app");
    JavaSparkContext sc = new JavaSparkContext(conf);

    List<Person> ps = new ArrayList<Person>();
    ps.add(new Person("jf1", "12", 11));
    ps.add(new Person("jf2", "14", 11));
    ps.add(new Person("jf3", "15", 11));
    JavaRDD<Person> rdd1 = sc.parallelize(ps);

    JavaRDD<String> stringJavaRDD =
      rdd1.mapPartitions(new FlatMapFunction<Iterator<Person>, String>() {
        public Iterator<String> call(Iterator<Person> personIterator) throws Exception {
          List<String> ps = new ArrayList<String>();
          while (personIterator.hasNext()) {
            Person person = personIterator.next();
            String jsonString = JSON.toJSONString(person);
            ps.add(jsonString);
          }
          return ps.iterator();
        }
      });

    stringJavaRDD.saveAsTextFile("/home/jiafu/test/person2.json");
    sc.stop();
  }


  /**
   * 读取json文件
   */
  public static void test04() {
    SparkConf conf = new SparkConf().setMaster("local").setAppName("my app");
    JavaSparkContext sc = new JavaSparkContext(conf);

    JavaRDD<String> rdd1 = sc.textFile("/home/jiafu/test/person2.json/part-00000");
    JavaRDD<Person> rdd2 =
      rdd1.mapPartitions(new FlatMapFunction<Iterator<String>, Person>() {
        public Iterator<Person> call(Iterator<String> stringIterator) throws Exception {
          List<Person> ps = new ArrayList<Person>();
          while (stringIterator.hasNext()) {
            String s = stringIterator.next();
            Person person = JSON.parseObject(s, Person.class);
            ps.add(person);
          }
          return ps.iterator();
        }
      });

    System.out.println("读取到人数:" + rdd2.count());
    sc.stop();
  }


  /**
   * 写入json文件
   */
  public static void test05() {
    SparkConf conf = new SparkConf().setMaster("local[3]").setAppName("my app");
    JavaSparkContext sc = new JavaSparkContext(conf);

    List<Person> ps = new ArrayList<Person>();
    ps.add(new Person("jf1", "12", 11));
    ps.add(new Person("jf2", "14", 12));
    ps.add(new Person("jf3", "15", 13));
    JavaRDD<Person> rdd1 = sc.parallelize(ps);

    JavaRDD<String> mapRdd = rdd1.map(new Function<Person, String>() {
      public String call(Person person) throws Exception {
        return String.format("%s,%s,%d", person.getName(), person.getId(), person.getAge());
      }
    });

    mapRdd.saveAsTextFile("/home/jiafu/test/person1.csv");
    sc.stop();
  }


  /**
   * 读取hdfs
   */
  public static void test06() {
    SparkConf conf = new SparkConf().setMaster("local[3]").setAppName("my app");
    JavaSparkContext sc = new JavaSparkContext(conf);

    JavaRDD<String> readRdd = sc.textFile("hdfs://10.1.1.22:9000/test/anaconda-ks.cfg");
    System.out.println(readRdd.count());
    System.out.println(readRdd.first());
    sc.stop();
  }

  /**
   * 对象文件
   */
  public static void test07() {
    SparkConf conf = new SparkConf().setMaster("local[3]").setAppName("my app");
    JavaSparkContext sc = new JavaSparkContext(conf);

    List<Person> ps = new ArrayList<Person>();
    ps.add(new Person("jf1", "12", 11));
    ps.add(new Person("jf2", "14", 12));
    ps.add(new Person("jf3", "15", 13));
    JavaRDD<Person> rdd1 = sc.parallelize(ps);

    rdd1.saveAsObjectFile("/home/jiafu/test/objectFile2");


    sc.stop();
  }

  /**
   * 读取对象文件
   */
  public static void test08() {
    SparkConf conf = new SparkConf().setMaster("local[3]").setAppName("my app");
    JavaSparkContext sc = new JavaSparkContext(conf);

    JavaRDD<Person> objectRdd = sc.objectFile("/home/jiafu/test/objectFile2");
    List<Person> collect = objectRdd.collect();
    System.out.println(collect.get(0).toString());

    sc.stop();
  }


  /**
   * 读取hdfs,并测试累加器
   */
  public static void test09() {
    SparkConf conf = new SparkConf().setMaster("local[3]").setAppName("my app");
    JavaSparkContext sc = new JavaSparkContext(conf);
    final Accumulator<Integer> accumulator = sc.accumulator(0);
    JavaRDD<String> readRdd = sc.textFile("hdfs://10.1.1.22:9000/test/anaconda-ks.cfg");
    JavaRDD<String> filterRdd = readRdd.filter(new Function<String, Boolean>() {
      public Boolean call(String s) throws Exception {
        if(s.isEmpty()){
          accumulator.add(1);
        }
        return s.length() > 0;
      }
    });

    System.out.println(filterRdd.count());
    System.out.println("空行为："+accumulator.value());
    sc.stop();
  }

  public static void main(String[] args) {
//    test01();
//    test02();
//    test03();
//    test04();
//    test05();
    test09();
  }

}
