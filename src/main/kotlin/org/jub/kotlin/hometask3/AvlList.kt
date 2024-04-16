package org.jub.kotlin.hometask3

class AvlList<K : Comparable<K> , V : Any>(collection: Iterable<Pair<K, V>>, override var height: Int,
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
    override fun indexOf(element: V): Int = TODO()//keyOfValue(root, element)

    private fun getByIndex(index: Int): V {
        val elements = mutableListOf<Pair<Int, V>>()

        if (index < 0 || index >= elements.size) {
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

    override fun iterator(): Iterator<V> {
        return object : Iterator<V> {
            private val stack = mutableListOf<Node<K, V>>()
            private var current: Node<K, V>? = root

            init {
                pushLeftPath(root)
            }

            override fun hasNext(): Boolean {
                return stack.isNotEmpty()
            }

            override fun next(): V {
                if (!hasNext()) throw NoSuchElementException("No more elements to iterate over.")
                val node = stack.removeAt(stack.size - 1)
                pushLeftPath(node.rightChild) // Push the left path of the right child
                return node.value
            }

            private fun pushLeftPath(node: Node<K, V>?) {
                var current = node
                while (current != null) {
                    stack.add(current)
                    current = current.leftChild
                }
            }
        }
    }

    override fun lastIndexOf(element: V): Int = TODO()//
    // lastKeyOfValue(root, element)
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

    override fun listIterator(index: Int): ListIterator<V> {
        return listIterator(0)
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<V> {
        return emptyList()
    }

    override fun maximumKey(): K = maximumKey()

    override fun minimumKey(): K = minimumKey()
    override fun maximumValue(): V = maximumValue()

    override fun minimumValue(): V = minimumValue()
    fun lastKeyOfValue(node: Node<K, V>?, value: V): K? {
        if (node == null) {
            return null
        }

        val rightResult = lastKeyOfValue(node.rightChild, value)
        if (rightResult != null) {
            return rightResult // Key found in the right subtree, return it
        }

        val leftResult = lastKeyOfValue(node.leftChild, value)
        if (leftResult != null) {
            return leftResult // Key found in the left subtree, return it
        }

        // Check if the current node's value matches the target value
        return if (node.value == value) node.key else null
    }

    fun keyOfValue(node: Node<K, V>?, value: V): K? {
        if (node == null) {
            return null
        }

        val leftResult = keyOfValue(node.leftChild, value)
        if (leftResult != null) {
            return leftResult // Key found in the left subtree, return it
        }

        if (node.value == value) {
            return node.key // Key found in the current node
        }

        val rightResult = keyOfValue(node.rightChild, value)
        if (rightResult != null) {
            return rightResult // Key found in the right subtree, return it
        }

        return null // Key not found in the entire subtree
    }
}
