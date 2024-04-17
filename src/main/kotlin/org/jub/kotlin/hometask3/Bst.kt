package org.jub.kotlin.hometask3

import org.jub.kotlin.hometask3.AddBstFun.balance
import org.jub.kotlin.hometask3.AddBstFun.findMax
import org.jub.kotlin.hometask3.AddBstFun.findMin
import org.jub.kotlin.hometask3.AddBstFun.updateHeight
import kotlin.math.max

open class Bst<K : Comparable<K>, V>(collection: Iterable<Pair<K, V>>) : BalancedSearchTree<K, V> {
    override var height: Int = 0
    var root: Node<K, V>? = null

    init {
        for (pair in collection) {
            root = insert(pair, root)
        }
    }

    override fun maximumKey(): K =
        root?.let { AddBstFun.findMax<K, V>(it).key } ?: throw NoSuchElementException("The Tree is empty")

    override fun minimumKey(): K =
        root?.let { AddBstFun.findMin<K, V>(it).key } ?: throw NoSuchElementException("The Tree is empty")

    override fun maximumValue(): V =
        root?.let { findMax(it).value } ?: throw NoSuchElementException("The Tree is empty")

    override fun minimumValue(): V =
        root?.let { findMin(it).value } ?: throw NoSuchElementException("The Tree is empty")

    fun add(pair: Pair<K, V>) {
        root = insert(pair, root)
    }

    fun removeNode(key: K, node: Node<K, V>? = root) = delete(key, node)

    private fun insert(pair: Pair<K, V>, currentNode: Node<K, V>?): Node<K, V>? {
        currentNode?.let {
            val node = Node(pair.first, pair.second, null, null, 1)

            if (pair.first < it.key) {
                it.leftChild = insert(pair, it.leftChild)
            } else if (pair.first > it.key) {
                it.rightChild = insert(pair, it.rightChild)
            }

            updateHeight(it)

            return balance(it)
        }

        // If currentNode is null, return a new node
        return Node(pair.first, pair.second, null, null, 1)
    }

    private fun delete(key: K, currentNode: Node<K, V>?): Node<K, V>? {
        val node = currentNode ?: return null

        return when {
            key < node.key -> {
                node.leftChild = delete(key, node.leftChild)
                updateHeight(node)
                balance(node)
            }

            key > node.key -> {
                node.rightChild = delete(key, node.rightChild)
                updateHeight(node)
                balance(node)
            }

            else -> {
                val updatedNode = node.leftChild ?: node.rightChild ?: return null
                val successor = AddBstFun.findSuccessor(node.rightChild!!)
                node.key = successor.key
                node.value = successor.value
                node.rightChild = delete(successor.key, node.rightChild)
                updateHeight(node)
                balance(node)
                updatedNode
            }
        }
    }
}

object AddBstFun {
    fun <K : Comparable<K>, V> balance(node: Node<K, V>?): Node<K, V>? {
        node ?: return null

        val balanceFactor = height(node.leftChild) - height(node.rightChild)

        return when {
            balanceFactor > 1 -> {
                if (height(node.leftChild?.leftChild) - height(node.leftChild?.rightChild) >= 0) {
                    rightRotate(node)
                } else {
                    node.leftChild = leftRotate(node.leftChild!!)
                    rightRotate(node)
                }
            }

            balanceFactor < -1 -> {
                if (height(node.rightChild?.leftChild) - height(node.rightChild?.rightChild) <= 0) {
                    leftRotate(node)
                } else {
                    node.rightChild = rightRotate(node.rightChild!!)
                    leftRotate(node)
                }
            }

            else -> node
        }
    }

    private fun <K : Comparable<K>, V> leftRotate(node: Node<K, V>): Node<K, V> {
        val newRoot = node.rightChild
        return newRoot?.let {
            node.rightChild = it.leftChild ?: node
            it.leftChild = node
            updateHeight(node)
            it
        } ?: throw NoSuchElementException("Nothing to rotate")
    }

    private fun <K : Comparable<K>, V> rightRotate(node: Node<K, V>): Node<K, V> {
        val newRoot = node.leftChild
        return newRoot?.let {
            node.leftChild = it.rightChild
            it.rightChild = node
            updateHeight(node)
            updateHeight(it)
            it
        } ?: throw NoSuchElementException("Nothing to rotate")
    }

    fun <K : Comparable<K>, V> findSuccessor(node: Node<K, V>): Node<K, V> {
        var current = node
        while (current.leftChild != null) {
            current = current.leftChild!!
        }
        return current
    }

    fun <K : Comparable<K>, V> updateHeight(node: Node<K, V>) {
        val maxHeight = max(height(node.leftChild), height(node.rightChild))
        node.height = maxHeight
    }

    private fun <K : Comparable<K>, V> height(node: Node<K, V>?): Int = node?.height ?: 0

    fun <K : Comparable<K>, V> findMax(node: Node<K, V>): Node<K, V> {
        var cursor = node
        while (cursor.rightChild != null) {
            cursor = cursor.rightChild!!
        }
        return cursor
    }

    fun <K : Comparable<K>, V> findMin(node: Node<K, V>): Node<K, V> {
        var cursor = node
        while (cursor.leftChild != null) {
            cursor = cursor.leftChild!!
        }
        return cursor
    }
}
