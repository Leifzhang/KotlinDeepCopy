package com.bennyhuo.kotlin.deepcopy.runtime

/**
 * Created by benny.
 */
fun <T> Collection<T>.copy(): Collection<T> = toList()

fun <T> List<T>.copy() = toList()

fun <T> Set<T>.copy() = toSet()

fun <K, V> Map<K, V>.copy() = toMap()

@JvmName("mutableCopy")
fun <T> MutableCollection<T>.copy() = toMutableList()

@JvmName("mutableCopy")
fun <T> MutableList<T>.copy() = toMutableList()

@JvmName("mutableCopy")
fun <T> MutableSet<T>.copy() = toMutableSet()

@JvmName("mutableCopy")
fun <K, V> MutableMap<K, V>.copy() = toMutableMap()

inline fun <T> Collection<T>.deepCopy(deepCopyOfElement: (T) -> T) = map(deepCopyOfElement)

inline fun <T> List<T>.deepCopy(deepCopyOfElement: (T) -> T) = map(deepCopyOfElement)

inline fun <T> Set<T>.deepCopy(deepCopyOfElement: (T) -> T) = map(deepCopyOfElement)

inline fun <K, V> Map<K, V>.deepCopy(deepCopyOfKey: (K) -> (K), deepCopyOfValue: (V) -> V) = map {
    deepCopyOfKey(it.key) to deepCopyOfValue(it.value)
}.toMap()

@JvmName("mutableCopy")
inline fun <T> MutableCollection<T>.deepCopy(deepCopyOfElement: (T) -> T): MutableCollection<T> =
    mapTo(ArrayList(), deepCopyOfElement)

@JvmName("mutableCopy")
inline fun <T> MutableList<T>.deepCopy(deepCopyOfElement: (T) -> T): MutableList<T> =
    mapTo(ArrayList(), deepCopyOfElement)

@JvmName("mutableCopy")
inline fun <T> MutableSet<T>.deepCopy(deepCopyOfElement: (T) -> T) =
    mapTo(HashSet(), deepCopyOfElement)

@JvmName("mutableCopy")
inline fun <K, V> MutableMap<K, V>.deepCopy(
    deepCopyOfKey: (K) -> (K),
    deepCopyOfValue: (V) -> V
): MutableMap<K, V> = map {
    deepCopyOfKey(it.key) to deepCopyOfValue(it.value)
}.toMap(HashMap())
