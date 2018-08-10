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

package com.jiafu.other;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

interface A {

  default public void test() {
    System.out.println("test");
  }

  public void test2();
}

interface B {
  public void test();
}

interface C extends A ,B {

  default void test() {
    A.super.test();
  }

  default void test2() {
    System.out.println("test2");
  }
}

public class Example01 implements C{

  public static void iterableTest(Iterable<String> it){
    it.iterator();
  }


  public static void main(String[] args) {
    new Example01().test2();
    List<String> ls = new ArrayList<>();
    iterableTest(ls);
  }
}
