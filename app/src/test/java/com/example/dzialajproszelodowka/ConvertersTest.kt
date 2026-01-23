package com.example.dzialajproszelodowka


import com.example.dzialajproszelodowka.data.db.Converters
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Date

class ConvertersTest {

    private val converters = Converters()

    @Test
    fun `test dateToTimestamp`() {
        val timestamp = 123456789L
        val date = Date(timestamp)
        assertEquals(timestamp, converters.dateToTimestamp(date))

        assertNull(converters.dateToTimestamp(null))
    }

    @Test
    fun `test fromTimestamp`() {
        val timestamp = 987654321L
        val expectedDate = Date(timestamp)
        assertEquals(expectedDate, converters.fromTimestamp(timestamp))

        assertNull(converters.fromTimestamp(null))
    }
}