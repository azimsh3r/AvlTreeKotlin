package org.jub.kotlin.hometask3

class AvlTreeList<K : Comparable<K>, V : Any>(
    collection: Iterable<Pair<K, V>>, override var height: Int,
    override val size: Int
) :
    BalancedSearchTreeList<K, V>, Bst<K, V>(collection) {

    override fun contains(element: V): Boolean = searchRecursively(element, root)
    private fun searchRecursively(value: V, root: Node<K, V>?): Boolean {
        if (root != null) {
            if (root.value == value) {
                return true
            }
            // Search in the left subtree
            if (searchRecursively(value, root.leftChild)) {
                return true
            }
            // If not found in the left subtree, search in the right subtree
            if (searchRecursively(value, root.rightChild)) {
                return true
            }
        }
        return false
    }

    override fun containsAll(elements: Collection<V>): Boolean {
        for (element in elements) {
            if (!searchRecursively(element, root))
                return false
        }
        return true
    }

    override fun get(index: Int): V = getByIndex(index)
    override fun indexOf(element: V): Int {
        this.forEachIndexed { id, el -> if (el == element) return id }
        return -1
    }

    private fun getByIndex(index: Int): V {
        val elements = mutableListOf<Pair<Int, V>>()

        if (index <= 0 || index >= elements.size) {
            throw IndexOutOfBoundsException("Index $index is out of bounds.")
        }

        inOrderTraversalWithIndex(root, elements)


        // Return the value at the specified index
        return elements[index].second
    }

    private fun inOrderTraversalWithIndex(node: Node<K, V>?, elements: MutableList<Pair<Int, V>>, index: Int = 0) {
        if (node != null) {
            inOrderTraversalWithIndex(node.leftChild, elements, index)
            elements.add(Pair(index + elements.size, node.value))
            inOrderTraversalWithIndex(node.rightChild, elements, index + elements.size + 1)
        }
    }

    override fun isEmpty(): Boolean = root == null

    override fun iterator(): Iterator<V> = listIterator()


    override fun lastIndexOf(element: V): Int =
        this.reversed().indexOf(element).let { if (it != -1) size - it - 1 else it }

    override fun listIterator(): ListIterator<V> {
        val elements = mutableListOf<V>()
        inOrderTraversal(root, elements)
        return elements.listIterator()
    }

    private fun inOrderTraversal(node: Node<K, V>?, elements: MutableList<V>) {
        if (node != null) {
            inOrderTraversal(node.leftChild, elements)
            elements.add(node.value)
            inOrderTraversal(node.rightChild, elements)
        }
    }

    override fun listIterator(index: Int) = listIterator().also { iterator -> repeat(index) { iterator.next() } }
    override fun subList(fromIndex: Int, toIndex: Int) = throw UnsupportedOperationException()
    override fun maximumKey(): K = maximumKey()

    override fun minimumKey(): K = minimumKey()
    override fun maximumValue(): V = maximumValue()

    override fun minimumValue(): V = minimumValue()
}
