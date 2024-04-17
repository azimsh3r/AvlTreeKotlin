package org.jub.kotlin.hometask3

class MutableBstMap<K : Comparable<K>, V>(
    collection: Iterable<Pair<K, V>>,
    override var size: Int = 0,
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> = mutableSetOf(),
    override val keys: MutableSet<K> = mutableSetOf(),
    override val values: MutableCollection<V> = mutableListOf(),
) : MutableBalancedSearchTreeMap<K, V>, Bst<K, V>(collection) {
    init {
        collection.forEach { _ -> size++ }
        collection.forEach { addEntry(it.first, it.second) }
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

    override fun containsValue(value: V): Boolean = AddMapFun.searchRecursively<K, V>(value, root)

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
        size = 0
        return root == null
    }

    override fun remove(key: K): V? {
        size--
        keys.remove(key)
        println(get(key))
        return removeNode(key)?.value
    }

    override fun merge(other: MutableBalancedSearchTreeMap<out K, out V>): MutableBalancedSearchTreeMap<K, V> {
        other.entries.forEach { (key, value) ->
            if (!containsKey(key)) {
                put(key, value)
            }
        }
        return this
    }

    override fun clear() {
        entries.clear()
        keys.clear()
        values.clear()
        root = clearRecursive(root)
    }

    private fun clearRecursive(node: Node<K, V>?): Node<K, V>? = node?.let {
        it.leftChild = clearRecursive(it.leftChild)
        it.rightChild = clearRecursive(it.rightChild)
        removeNode(it.key, it)
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

    private fun addEntry(key: K, value: V) {
        val entry = MapEntry(key, value)
        entries.add(entry)
        keys.add(key)
        values.add(value)
    }

    private inner class MapEntry(override val key: K, override var value: V) : MutableMap.MutableEntry<K, V> {
        override fun setValue(newValue: V): V {
            val oldValue = value
            value = newValue
            return oldValue
        }
    }
}

object AddMapFun {
    fun <K : Comparable<K>, V> searchRecursively(value: V, root: Node<K, V>?): Boolean {
        root?.let {
            if (it.value == value || searchRecursively(value, it.leftChild) || searchRecursively(
                    value,
                    it.rightChild
                )
            ) {
                return true
            }
        }
        return false
    }
}
