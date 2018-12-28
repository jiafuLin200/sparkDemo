package com.jf.other;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口多重继承测试
 */
interface A {

  default void test() {
    System.out.println("test");
  }

  void test2();
}

interface B {

  void test();
}

interface C extends A, B {
  @Override
  default void test() {
    A.super.test();
  }

  @Override
  default void test2() {
    System.out.println("test2");
  }
}

public class Example01 implements C {

  public static void iterableTest(Iterable<String> it) {
    it.iterator();
  }

  public static void main(String[] args) {
    new Example01().test2();
    List<String> ls = new ArrayList<>();
    iterableTest(ls);
  }
}
