package com.zero.advent

import org.rocksdb.Options
import org.rocksdb.ReadOptions
import org.rocksdb.RocksDB
import org.rocksdb.WriteOptions
import java.nio.ByteBuffer

fun main(args : Array<String>) {
    val generate = false
    val buffer = ByteBuffer.allocate(1000);
    RocksDB.loadLibrary()
    Options().setCreateIfMissing(true).use {
        RocksDB.open(it, "./data").use { db ->
            if (generate) {
                println("start")
                val groups = 10
                val items = 10
                val keys = Array(groups * items) { i ->
                    val id = i / items + 1
                    val ver = i % items + 2
                    buffer.put("key".toByteArray())
                    buffer.putInt(id)
                    buffer.putInt(ver)
                    buffer.flip()

                    val value = ByteArray(buffer.limit())
                    buffer.get(value)
                    buffer.clear()
                    value
                }
                val values = Array(keys.size) { i -> "${i + 1}".toByteArray() }
                val wo = WriteOptions()
                for (i in keys.indices.reversed()) {
                    db.put(keys[i], values[i])
                }
                println("written")
            }
            db.delete("x".toByteArray())
            println("Version: " + db.latestSequenceNumber)
            val ro = ReadOptions()

            db.newIterator(ro).use { iter ->
                buffer.put("key".toByteArray())
                buffer.putInt(4)
                buffer.put(0)
                buffer.put(0)
                buffer.put(0)
                buffer.put(0)
                buffer.flip()
                val prefix = ByteArray(buffer.limit())
                buffer.get(prefix)
                buffer.clear()
                iter.seek(prefix)
                //iter.seekToFirst()

                while (iter.isValid) {
                    val key1 = iter.key()
                    val key2 = iter.key()
                    buffer.clear()
                    buffer.put(iter.key())
                    buffer.flip()
                    val key = String(iter.key().copyOfRange(0, 3)) + '/' + buffer.getInt(3) + '/' + buffer.getInt(7)
                    val value = String(iter.value())
                    System.out.println("$key -> $value")
                    iter.next()
                }
            }
        }
    }
}