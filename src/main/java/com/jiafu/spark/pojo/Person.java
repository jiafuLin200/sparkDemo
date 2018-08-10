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

package com.jiafu.spark.pojo;

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
