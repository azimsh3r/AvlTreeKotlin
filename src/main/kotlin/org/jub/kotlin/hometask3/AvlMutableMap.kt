package org.jub.kotlin.hometask3

import java.lang.IllegalStateException

class AvlMutableMap<K : Comparable<K>, V>(
    collection: Iterable<Pair<K, V>>,
    override var size: Int = 0,
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> = mutableSetOf(),
    override val keys: MutableSet<K> = mutableSetOf(),
    override val values: MutableCollection<V> = mutableListOf(),
) : MutableBalancedSearchTreeMap<K, V>, Bst<K, V>(collection) {

    init {
        for (c in collection) {
            size++
        }
    }

    override fun containsKey(key: K): Boolean {
        var currentNode = root
        while (currentNode != null) {
            if (currentNode.key == key) {
                return true
            } else if (currentNode.key < key) {
                currentNode = currentNode.rightChild
            } else {
                currentNode = currentNode.leftChild
            }
        }
        return false
    }

    override fun containsValue(value: V): Boolean = searchRecursively(value, root)

    private fun searchRecursively(value: V, root: Node<K, V>?): Boolean {
        if (root != null) {
            if (root.value == value) {
                return true
            }
            if (searchRecursively(value, root.leftChild)) {
                return true
            }
            if (searchRecursively(value, root.rightChild)) {
                return true
            }
        }
        return false
    }

    override fun get(key: K): V? {
        var currentNode = root
        while (currentNode != null) {
            if (currentNode.key == key) {
                return currentNode.value
            } else if (currentNode.key < key) {
                currentNode = currentNode.rightChild
            } else {
                currentNode = currentNode.leftChild
            }
        }
        return null
    }

    override fun isEmpty(): Boolean {
        size = 0;
        return root == null
    }

    override fun remove(key: K): V? {
        this.size--
        keys.remove(key)
        println(get(key))
        return removeNode(key)?.value
    }

    override fun merge(other: MutableBalancedSearchTreeMap<out K, out V>): MutableBalancedSearchTreeMap<K, V> {
        other.entries.forEach { (key, value) ->
                if (!containsKey(key)) {
                    put(key, value)
                } else {
                    throw Exception("It exists")
                }
        }
        return this
    }

    override fun clear() {
        root = clearRecursive(root)
    }

    private fun clearRecursive(node: Node<K, V>?): Node<K, V>? {
        if (node == null) return null
        node.leftChild = clearRecursive(node.leftChild)
        node.rightChild = clearRecursive(node.rightChild)
        return removeNode(node.key, node)
    }

    override fun putAll(from: Map<out K, V>) = from.forEach { add(Pair(it.key, it.value)) }

    override fun put(key: K, value: V): V? {
        if (!containsKey(key)) {
            size++
            keys.add(key)
            values.add(value)
            this.add(Pair(key, value))
            return value
        }
        return get(key)
    }
}
