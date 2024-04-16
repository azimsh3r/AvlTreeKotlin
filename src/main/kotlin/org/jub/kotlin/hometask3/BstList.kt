package org.jub.kotlin.hometask3

class BstList<K : Comparable<K>, V : Any>(
    collection: Iterable<Pair<K, V>>,
    override var height: Int,
    override val size: Int
) :
    BalancedSearchTreeList<K, V>, Bst<K, V>(collection) {
    override fun contains(element: V): Boolean = AddListFun.searchRecursively(element, root)

    override fun containsAll(elements: Collection<V>): Boolean {
        for (element in elements) {
            if (!AddListFun.searchRecursively(element, root))
                return false
        }
        return true
    }

    override fun get(index: Int): V = AddListFun.getByIndex(index, root)

    override fun indexOf(element: V): Int {
        this.forEachIndexed { id, el -> if (el == element) return id }
        return -1
    }

    override fun isEmpty(): Boolean = root == null

    override fun iterator(): Iterator<V> = listIterator()

    override fun lastIndexOf(element: V): Int =
        this.reversed().indexOf(element).let { if (it != -1) size - it - 1 else it }

    override fun listIterator(): ListIterator<V> {
        val elements = mutableListOf<V>()
        AddListFun.inOrderTraversal<K, V>(root, elements)
        return elements.listIterator()
    }

    override fun listIterator(index: Int) = listIterator().also { iterator -> repeat(index) { iterator.next() } }

    override fun subList(fromIndex: Int, toIndex: Int) = throw UnsupportedOperationException()
}

object AddListFun {
    public fun <K : Comparable<K>, V> inOrderTraversal(node: Node<K, V>?, elements: MutableList<V>) {
        node?.let {
            inOrderTraversal(it.leftChild, elements)
            elements.add(it.value)
            inOrderTraversal(it.rightChild, elements)
        }
    }

    public fun <K : Comparable<K>, V> inOrderTraversalWithIndex(
        node: Node<K, V>?,
        elements: MutableList<Pair<Int, V>>,
        index: Int = 0
    ) {
        node?.let {
            inOrderTraversalWithIndex(it.leftChild, elements, index)
            elements.add(Pair(index + elements.size, it.value))
            inOrderTraversalWithIndex(it.rightChild, elements, index + elements.size + 1)
        }
    }

    public fun <K : Comparable<K>, V> searchRecursively(value: V, root: Node<K, V>?): Boolean {
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

    public fun <K : Comparable<K>, V> getByIndex(index: Int, root: Node<K, V>?): V {
        val elements = mutableListOf<Pair<Int, V>>()

        if (index <= 0 || index >= elements.size) {
            throw IndexOutOfBoundsException("Index $index is out of bounds.")
        }

        inOrderTraversalWithIndex<K, V>(root, elements)

        // Return the value at the specified index
        return elements[index].second
    }
}
