package leetcode



fun removeVowels(s: String) = s.filter { it !in setOf('a', 'o', 'e', 'i', 'u') }

fun compareVersion(version1: String, version2: String): Int {
    val v1 = version1.split('.').map { it.toInt() }
    val v2 = version2.split('.').map { it.toInt() }
    for (i in 0 until (maxOf(v1.size, v2.size))) {
        val diff = (v1.getOrElse(i) { 0 } - v2.getOrElse(i) { 0 })
        if (diff != 0) return if (diff > 0) 1 else -1
    }
    return 0
}

fun compareVersion1(version1: String, version2: String): Int {
    val list1 = version1.split(".")
    val list2 = version2.split(".")
    var i = 0
    do {
        val v1 = list1.getOrNull(i)?.toInt() ?: 0
        val v2 = list2.getOrNull(i)?.toInt() ?: 0
        if (v1 != v2) {
            return v1.compareTo(v2)
        }
        i++
    } while (i < list1.size || i < list2.size)

    return 0
}

fun compareVersion2(version1: String, version2: String): Int {
    val size1 = version1.length
    val size2 = version2.length
    var i = 0
    var j = 0
    while (i < size1 || j < size2) {
        var x = 0
        while (i < size1 && version1[i] != '.') {
            x = x * 10 + version1[i].toInt() - '0'.toInt()
            i++
        }
        i++
        var y = 0
        while (j < size2 && version2[j] != '.') {
            y = y * 10 + version2[j].toInt() - '0'.toInt()
            j++
        }
        j++

        if (x != y) {
            return x.compareTo(y)
        }
    }
    return 0
}

//fun compareVersion3(version1: String, version2: String) {
//    version1.split(".").zipLongest(version2.split("."), "0")
//        .onEach {
////            with(it){
////                if (first != second){
////                    return@with first.compareTo(second)
////                }
////            }
//        }.run { return@run 0 }
//}

//private fun Iterable<String>.zipLongest(
//    other: Iterable<String>,
//    deflater: String
//): List<Pair<Int, Int>> {
//    val first = iterator()
//    val second = other.iterator()
//    val list = ArrayList<Pair<Int, Int>>(minOf(collectionSizeOrDefault(10), other.collectionSizeOrDefault(10)))
//    while (first.hasNext() || second.hasNext()) {
//        val v1 = (first.nextOrNull() ?: deflater).toInt()
//        val v2=(second.nextOrNull() ?: deflater).toInt()
//        list.add(Pair(v1, v2))
//    }
//    return list
//}
//
//private fun <T> Iterable<T>.collectionSizeOrDefault(deflater: Int): Int =
//    if (this is Collection<*>) this.size else deflater
//
//private fun <T> Iterable<T>.nextOrNull(): T? = if (hasNext()) next() else null
//
//private data class Pair<out A, out B>(val first: A, val second: B) : java.io.Serializable {
//    public override fun toString(): String = "($first,$second)"
//}




//fun solveEquation(equation: String): String {
//
//}



fun main() {
//    val s="a,b,c,d,e,f,j,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z"
//    val vowels = removeVowels(s)
//    print(vowels)

    val version1 = "1.2"
    val version2 = "1.10"
    val compareVersion = compareVersion2(version1, version2)
    println(compareVersion)
}