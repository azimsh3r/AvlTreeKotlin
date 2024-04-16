package org.jub.kotlin.hometask3

import kotlin.math.max

open class Bst<K : Comparable<K>, V>(collection: Iterable<Pair<K, V>>) : BalancedSearchTree<K, V> {
    override var height: Int = 1
    var root: Node<K, V>? = null

    init {
        for (pair in collection) {
            root = insert(pair, root)
        }
    }

    override fun maximumKey(): K {
        var cursor = root
        while (cursor?.rightChild != null) {
            cursor = cursor.rightChild
        }

        if (cursor != null) {
            return cursor.key
        }
        throw Exception("THe Tree is empty")
    }

    override fun minimumKey(): K {
        var cursor = root
        while (cursor?.leftChild != null) {
            cursor = cursor.leftChild;
        }
        if (cursor != null) {
            return cursor.key
        }
        throw Exception("The Tree is empty")
    }

    override fun maximumValue(): V {
        var cursor = root
        while (cursor?.rightChild != null) {
            cursor = cursor.rightChild
        }
        if (cursor != null) {
            return cursor.value
        }
        throw Exception("The Tree is empty")
    }

    override fun minimumValue(): V {
        var cursor = root
        while (cursor?.leftChild != null) {
            cursor = cursor.leftChild
        }
        if (cursor != null) {
            return cursor.value
        }
        throw Exception("Tree is empty")
    }

    fun add(pair: Pair<K, V>) = insert(pair, root)
    fun removeNode(key: K, node: Node<K, V>? = root) = delete(key, node)

    /** Additional necessary functions **/
    private fun insert(pair: Pair<K, V>, currentNode: Node<K, V>?): Node<K, V>? {
        val node = Node(pair.first, pair.second, null, null, 1)

        if (currentNode == null) {
            return node
        }

        if (pair.first < currentNode.key) {
            currentNode.leftChild = insert(pair, currentNode.leftChild)
        } else if (pair.first > currentNode.key) {
            currentNode.rightChild = insert(pair, currentNode.rightChild)
        }

        updateHeight(currentNode)

        return balance(currentNode)
    }

    private fun delete(key: K, currentNode: Node<K, V>?): Node<K, V>? {
        if (currentNode == null) {
            throw Exception("Tree is empty")
        }

        when {
            key < currentNode.key -> currentNode.leftChild = delete(key, currentNode.leftChild)
            key > currentNode.key -> currentNode.rightChild = delete(key, currentNode.rightChild)
            else -> {
                if (currentNode.leftChild == null) return currentNode.rightChild
                if (currentNode.rightChild == null) return currentNode.leftChild
                val successor = findSuccessor(currentNode.rightChild!!)

                currentNode.key = successor.key
                currentNode.value = successor.value

                currentNode.rightChild = delete(successor.key, currentNode.rightChild)
            }
        }

        updateHeight(currentNode)
        return balance(currentNode)
    }

    private fun balance(node: Node<K, V>?): Node<K, V>? {
        if (node == null) {
            return null
        }

        val balanceFactor = height(node.leftChild) - height(node.rightChild)

        if (balanceFactor > 1) {
            return if (height(node.leftChild?.leftChild) - height(node.leftChild?.rightChild) >= 0) {
                rightRotate(node)
            } else {
                node.leftChild = leftRotate(node.leftChild!!)
                rightRotate(node)
            }
        } else if (balanceFactor < -1) {
            return if (height(node.rightChild?.leftChild) - height(node.rightChild?.rightChild) <= 0) {
                leftRotate(node)
            } else {
                node.rightChild = rightRotate(node.rightChild!!)
                leftRotate(node)
            }
        }
        return node
    }

    private fun leftRotate(node: Node<K, V>): Node<K, V> {
        val newRoot = node.rightChild
        if (newRoot != null) {
            node.rightChild = newRoot.leftChild ?: node

            newRoot.leftChild = node

            updateHeight(node)

            return newRoot
        }

        throw Exception("Nothing to rotate")
    }

    private fun rightRotate(node: Node<K, V>): Node<K, V> {
        val newRoot = node.leftChild
        if (newRoot != null) {
            node.leftChild = newRoot.rightChild
            newRoot.rightChild = node
            updateHeight(node)
            updateHeight(newRoot)

            return newRoot
        }

        throw Exception("Nothing to rotate")
    }

    private fun findSuccessor(node: Node<K, V>): Node<K, V> {
        var current = node
        while (current.leftChild != null) {
            current = current.leftChild!!
        }
        return current
    }

    private fun updateHeight(node: Node<K, V>) {
        val maxHeight = max(height(node.leftChild), height(node.rightChild))
        node.height = maxHeight
    }

    private fun height(node: Node<K, V>?): Int {
        if (node != null) {
            return node.height
        }
        return 0
    }

}