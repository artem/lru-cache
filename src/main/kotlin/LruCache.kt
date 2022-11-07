class LruCache<K, V>(private val capacity: Int = 16) {
    private val storage: HashMap<K, Node<V>>
    private var head: Node<V>? = null
    private var tail: Node<V>? = null

    private class Node<V>(var value: V) {
        var prev: Node<V>? = null
        var next: Node<V>? = null

        fun unlink() {
            if (prev === null && next === null) {
                return
            }
            assert(prev !== next)
            if (prev !== null) {
                assert(prev!!.next === this)
                prev!!.next = next
            }
            if (next !== null) {
                assert(next!!.prev === this)
                next!!.prev = prev
            }
            prev = null
            next = null
        }
    }

    init {
        if (capacity < 1) {
            throw IllegalArgumentException("capacity must be positive")
        }
        this.storage = HashMap(capacity)
    }

    fun get(key: K): V? {
        return (getNode(key) ?: return null).value
    }

    private fun getNode(key: K): Node<V>? {
        val node = storage.get(key) ?: return null

        unlink(node)
        pushFront(node)

        return node
    }

    fun put(key: K, value: V) {
        val node = getNode(key)
        if (node === null) {
            assert(capacity >= storage.size)
            val newNode = Node(value)
            storage.put(key, newNode)
            pushFront(newNode)
            if (storage.size > capacity) {
                popBack()
                assert(capacity >= storage.size)
            }
        } else {
            node.value = value
        }
    }

    private fun unlink(node: Node<V>) {
        if (node === head) {
            head = node.next
        }
        if (node === tail) {
            tail = node.prev
        }
        node.unlink()
    }

    val size get() = storage.size

    private fun popBack() {
        val oldTail = tail
        assert(oldTail !== null)
        assert(storage.size >= 2)
        assert(head !== oldTail)
        assert(oldTail!!.next === null)
        val removeSuccess = storage.values.remove(oldTail)
        val removeFailure = storage.values.remove(oldTail)

        assert(removeSuccess)
        assert(!removeFailure)

        unlink(oldTail!!)
        assert(tail !== null)
    }

    private fun pushFront(node: Node<V>) {
        assert(node.prev === null)
        assert(node.next === null)
        if (head === null) {
            assert(tail === null)
            head = node
            tail = node
            return
        } else {
            assert(tail !== null)
            assert(head!!.prev === null)
            head!!.prev = node
            node.next = head
            head = node
        }
    }
}