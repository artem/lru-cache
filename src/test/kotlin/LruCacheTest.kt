import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class LruCacheTest {
    @Test
    fun testSingle() {
        val cache = LruCache<Int, Int>()
        cache.put(6, 4)
        assertEquals(4, cache.get(6))
    }

    @Test
    fun testException() {
        assertThrows(IllegalArgumentException::class.java) { LruCache<Int, Int>(0) }
        assertThrows(IllegalArgumentException::class.java) { LruCache<Int, Int>(-3) }
    }

    @Test
    fun testSimple() {
        val cache = LruCache<Int, Int>(2)
        assertEquals(null, cache.get(2))
        cache.put(2, 42)
        assertEquals(42, cache.get(2))
        assertEquals(null, cache.get(1))
        cache.put(1, 1)
        cache.put(1, 2)
        assertEquals(2, cache.get(1))
        assertEquals(42, cache.get(2))
    }

    @Test
    fun testStress() {
        val lruCapacity = 42
        val cache = LruCache<String, String>(lruCapacity)

        // Initial fill
        for (i in 0 until lruCapacity) {
            for (j in 0 until i) {
                assertEquals(j.toString(), cache.get(j.toString()))
            }
            cache.put(i.toString(), i.toString())
        }

        // Once again
        for (i in 0 until lruCapacity) {
            for (j in 0 until lruCapacity) {
                assertEquals(j.toString(), cache.get(j.toString()))
            }
            cache.put(i.toString(), i.toString())
        }

        // evict first `shift` elements
        val shift = 3
        for (curShift in 1..shift) {
            val tgt = lruCapacity + curShift - 1
            cache.put(tgt.toString(), tgt.toString())

            for (j in 0 until curShift) {
                assertEquals(null, cache.get(j.toString()))
            }
            for (j in curShift until lruCapacity + curShift) {
                assertEquals(j.toString(), cache.get(j.toString()))
            }
        }
        assertEquals(shift.toString(), cache.get(shift.toString()))
        // create a hole to evict `shift + 1`th element
        cache.put("hel", "lo")
        assertEquals(shift.toString(), cache.get(shift.toString()))
        assertEquals(null,  cache.get((shift + 1).toString()))
        for (j in shift + 2 until lruCapacity + shift) {
            assertEquals(j.toString(), cache.get(j.toString()))
        }
        assertEquals("lo", cache.get("hel"))
    }

    @Test
    fun testCapacityOne() {
        val cache = LruCache<Int, Int>(1)
        assertEquals(null, cache.get(0))
        assertEquals(null, cache.get(1))
        assertEquals(null, cache.get(2))
        cache.put(1, 1)
        assertEquals(null, cache.get(0))
        assertEquals(1, cache.get(1))
        assertEquals(null, cache.get(2))
        cache.put(2, 2)
        assertEquals(null, cache.get(0))
        assertEquals(null, cache.get(1))
        assertEquals(2, cache.get(2))
    }
}