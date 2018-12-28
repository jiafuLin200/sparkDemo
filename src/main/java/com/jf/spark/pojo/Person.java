package com.jf.spark.pojo;

import java.io.Serializable;

public class Person implements Serializable {
  private String name;
  private String id;
  private int age;

  public Person(String name, String id, int age) {
    this.name = name;
    this.age = age;
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public int getAge() {
    return age;
  }

  @Override
  public String toString() {
    return "Person{" +
      "name='" + name + '\'' +
      ", id='" + id + '\'' +
      ", age=" + age +
      '}';
  }
}
