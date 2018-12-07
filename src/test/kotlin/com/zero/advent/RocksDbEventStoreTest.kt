package com.zero.advent

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.rocksdb.Options
import org.rocksdb.RocksDB
import java.util.*

/**
 * @author zero
 * @since 2018-12-03
 */
internal class RocksDbEventStoreTest {
    private lateinit var eventStore: RocksDbEventStore

    companion object {
        private val db: RocksDB
        init {
            RocksDB.loadLibrary()
            val ops = Options().setCreateIfMissing(true)
            db = RocksDB.open(ops, "./data")
        }
    }

    val aggregateId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        eventStore = RocksDbEventStore(db, jacksonObjectMapper())
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun save() {
        eventStore.save("x", aggregateId, 1, listOf(DomainEvent(1), DomainEvent(2)))
        val events =  eventStore.get("x", aggregateId, 1)

    }

    @Test
    fun getDb() {
    }
}