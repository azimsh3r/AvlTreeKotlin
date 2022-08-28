package org.jub.kotlin.hometask3

import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal class AvlTreeListTest {

    @RepeatedTest(testIterations)
    fun contains() {
        val values = getSetOfRandomValues()
        val avl: AvlTreeList<Int, Double> = AvlTreeImpl(values.zip(values.map { it.toDouble() }))
        values.forEach {
            assertTrue(avl.contains(it.toDouble()))
        }
        val otherValues = getSetOfRandomValues()
        otherValues.filter { it !in values }.forEach {
            assertFalse(avl.contains(it.toDouble()))
        }
    }

    @RepeatedTest(testIterations)
    fun containsAll() {
        val values = getSetOfRandomValues().toList()
        val doubleValues = values.map { it.toDouble() }
        val avl: AvlTreeList<Int, Double> = AvlTreeImpl(values.zip(doubleValues))
        assertTrue(avl.containsAll(doubleValues + doubleValues))
        val otherValues = getSetOfRandomValues()
        otherValues.filter { it !in values }.forEach {
            assertFalse(avl.containsAll(doubleValues + it.toDouble() + doubleValues))
        }
    }

    @RepeatedTest(testIterations)
    fun get() {
        val values = getSetOfRandomValues().toList()
        val doubleValues = values.map { it.toDouble() }
        val avl: AvlTreeList<Int, Double> = AvlTreeImpl(values.zip(doubleValues))
        values.sorted().forEachIndexed { index, value ->
            assertEquals(value.toDouble(), avl[index])
        }
    }

    @Suppress("KotlinConstantConditions")
    @RepeatedTest(testIterations)
    fun getThrows() {
        val values = getSetOfRandomValues()
        val avl: AvlTreeList<Int, Double> = AvlTreeImpl(values.zip(values.map { it.toDouble() }))
        for (i in 0..testSetSize) {
            assertThrows<IndexOutOfBoundsException> {
                avl[avl.size + i]
            }
        }
        for (i in -1 downTo -avl.size) {
            assertThrows<IllegalArgumentException> {
                avl[i]
            }
        }
    }

    @RepeatedTest(testIterations)
    fun listIterator() {
        val values = getSetOfRandomValues().toList().sorted()
        val avl: AvlTreeList<Int, Double> = AvlTreeImpl(values.zip(values.map { it.toDouble() }))
        val valuesIterator = values.listIterator()
        val avlIterator = avl.listIterator()
        for (i in values.indices) {
            assertTrue(avlIterator.hasNext())
            assertEquals(valuesIterator.nextIndex(), avlIterator.nextIndex())
            assertEquals(valuesIterator.next().toDouble(), avlIterator.next())
        }
        for (i in values.size downTo values.size.div(2)) {
            assertTrue(avlIterator.hasPrevious())
            assertEquals(valuesIterator.previousIndex(), avlIterator.previousIndex())
            assertEquals(valuesIterator.previous().toDouble(), avlIterator.previous())
        }
        for (i in values.size.div(2)..values.size.div(4)) {
            assertTrue(avlIterator.hasNext())
            assertEquals(valuesIterator.nextIndex(), avlIterator.nextIndex())
            assertEquals(valuesIterator.next().toDouble(), avlIterator.next())
        }
    }

//    @Test
//    fun subList() {
//    }

    @RepeatedTest(testIterations)
    fun lastIndexOf() {
        val valuesSet = getSetOfRandomValues()
        val values = valuesSet.toList()
        val entries = values.flatMap { value -> List(3) { index -> "$value #$index" to value } }.sortedBy { it.first }
        val avl: AvlTreeList<String, Int> = AvlTreeImpl(entries.shuffled())
        val encounteredCount = mutableMapOf<Int, Int>()
        entries.forEachIndexed { index, (_, value) ->
            when(val count = encounteredCount.getOrDefault(value, 0)) {
                0, 1 -> {
                    encounteredCount[value] = count + 1
                    assertNotEquals(index, avl.lastIndexOf(value))
                }
                else -> { // 2
                    assertEquals(index, avl.lastIndexOf(value))
                }
            }
        }

        getSetOfRandomValues().filter { it !in values }.forEach {
            assertEquals(-1, avl.lastIndexOf(it))
        }
    }

    @RepeatedTest(testIterations)
    fun indexOf() {
        val valuesSet = getSetOfRandomValues()
        val values = valuesSet.toList()
        val entries = values.flatMap { value -> List(3) { index -> "$value #$index" to value } }.sortedBy { it.first }
        val avl: AvlTreeList<String, Int> = AvlTreeImpl(entries)
        val encountered = mutableSetOf<Int>()
        entries.forEachIndexed { index, (_, value) ->
            if (value !in encountered) {
                encountered.add(value)
                assertEquals(index, avl.indexOf(value))
            } else {
                assertNotEquals(index, avl.indexOf(value))
            }
        }

        getSetOfRandomValues().filter { it !in values }.forEach {
            assertEquals(-1, avl.indexOf(it))
        }
    }
}