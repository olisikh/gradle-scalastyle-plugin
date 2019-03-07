package org.gradle.sample.impl;

/**
 * Immutable implementation of {@link Person}.
 */
class PersonImpl(val names: List[String]) extends Person
{
  private val importedList = new GrowthList();
}
