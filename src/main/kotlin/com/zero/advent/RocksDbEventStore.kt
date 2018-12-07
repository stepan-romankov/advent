package com.zero.advent

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.rocksdb.RocksDB
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

/**
 * @author zero
 * @since 2018-12-03
 */
class RocksDbEventStore(val db: RocksDB, val serializer: ObjectMapper) {
    val buf = ByteBuffer.allocateDirect(1024).order(ByteOrder.BIG_ENDIAN)

    fun save(aggregateType: String, aggregateId: UUID, aggregateVersion: Int, events: List<DomainEvent>) {
        val key = getKey(aggregateType, aggregateId, aggregateVersion)
        val value = serializer.writeValueAsString(events).toByteArray()
        db.put(key, value)
    }

    fun get(aggregateType: String, aggregateId: UUID, aggregateVersion: Int): ArrayList<DomainEvent> {
        val key = getKey(aggregateType, aggregateId, aggregateVersion)
        val value :ArrayList<DomainEvent> = serializer.readValue(db.get(key))
        return value
    }

    private fun getKey(aggregateType: String, aggregateId: UUID, aggregateVersion: Int) : ByteArray {
        buf.clear()
        buf.put(aggregateType.toByteArray())
        buf.putLong(aggregateId.mostSignificantBits)
        buf.putLong(aggregateId.leastSignificantBits)
        buf.putInt(aggregateVersion)
        buf.flip()
        val key = ByteArray(buf.remaining())
        buf.get(key)
        return key
    }
}