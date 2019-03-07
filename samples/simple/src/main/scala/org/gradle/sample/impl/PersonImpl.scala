package org.gradle.sample.impl;

/**
 * Immutable implementation of {@link Person}.
 */
class PersonImpl(val names: List[String]) extends Person
{
  private val importedList = new GrowthList()

  private def times100(x: Int) = {
    val k = x * 100
    println(s"k = $k")
  }
}
