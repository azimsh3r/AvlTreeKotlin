package org.jub.kotlin.hometask3

fun <K : Comparable<K>, V> getBst(collection: Iterable<Pair<K, V>>): BalancedSearchTree<K, V> =
    Bst<K, V>(collection)

fun <K : Comparable<K>, V> getBstMap(collection: Iterable<Pair<K, V>>): BalancedSearchTreeMap<K, V> =
    MutableBstMap<K, V>(collection)

fun <K : Comparable<K>, V> getMutableBstMap(collection: Iterable<Pair<K, V>>): MutableBalancedSearchTreeMap<K, V> =
    MutableBstMap<K, V>(collection)

fun <K : Comparable<K>, V : Any> getBstList(collection: Iterable<Pair<K, V>>): BalancedSearchTreeList<K, V> =
    BstList<K, V>(collection, 1, 1)
