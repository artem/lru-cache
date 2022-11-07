fun main(args: Array<String>) {
    val size = 7
    val cache = LruCache<String, String>(size)

    for (i in 0 until size + 3) {
        cache.put(i.toString(), i.toString())
        for (j in 0 until size + 3) {
            println("$j: ${cache.get(j.toString())}")
        }
        println("-----------------------")
    }
    println("3 = ${cache.get("3")}")
    cache.put("hel", "lo")
    for (j in 0 until size + 3) {
        println("$j: ${cache.get(j.toString())}")
    }
    println("hel = ${cache.get("hel")}")
    println("-----------------------")
}