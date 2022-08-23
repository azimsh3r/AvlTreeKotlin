package org.jub.kotlin.hometask3

import kotlin.test.Test
import kotlin.test.assertEquals

internal class AvlTreeImplTest {

    @Test
    fun getSize() {
        val avl = AvlTreeImpl<Int, String>()
        assertEquals(0, avl.size, "Default size should be 0")
        avl[666] = "Number of the Beast"
        assertEquals(1, avl.size, "Size after first `put` should be 1")
        avl[100] = "100"
        assertEquals(2, avl.size, "Size after second `put` should be 2")
        avl.remove(666)
        assertEquals(1, avl.size, "Size after `remove` should be 1")
        listOf(50, 200, 75, 175, 25, 225).forEach { avl[it] = it.toString() }
        assertEquals(7, avl.size)
        avl.DEBUG_PRINT()
        println("Remove 50")
        avl.remove(50)
        avl.DEBUG_PRINT()
        println("Remove 75")
        avl.remove(75)
        avl.DEBUG_PRINT()
        println("Remove 25")
        avl.remove(25)
        avl.DEBUG_PRINT()
        assertEquals(4, avl.size, "${avl.keys.toList()}")
    }

    @Test
    fun clear() {
    }

    @Test
    fun isEmpty() {
    }
}