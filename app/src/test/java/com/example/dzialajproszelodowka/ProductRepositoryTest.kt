package com.example.dzialajproszelodowka

import com.example.dzialajproszelodowka.data.db.ProductDao
import com.example.dzialajproszelodowka.data.model.Product
import com.example.dzialajproszelodowka.data.repository.ProductRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.Date

class ProductRepositoryTest {

    private val productDao: ProductDao = mockk(relaxed = true)

    private val repository = ProductRepository(productDao)

    @Test
    fun `test getProductsExpiringTomorrow`() = runTest {
        val expectedProducts = listOf(mockk<Product>(), mockk<Product>())

        val startDateSlot = slot<Date>()
        val endDateSlot = slot<Date>()

        coEvery {
            productDao.getProductsBetweenDates(capture(startDateSlot), capture(endDateSlot))
        } returns expectedProducts

        val result = repository.getProductsExpiringTomorrow()


        assertEquals(expectedProducts, result)

        coVerify(exactly = 1) { productDao.getProductsBetweenDates(any(), any()) }
        val capturedStart = startDateSlot.captured
        val capturedEnd = endDateSlot.captured

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)

        val checkStart = Calendar.getInstance().apply { time = capturedStart }
        assertEquals(calendar.get(Calendar.DAY_OF_YEAR), checkStart.get(Calendar.DAY_OF_YEAR))
        assertEquals(0, checkStart.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, checkStart.get(Calendar.MINUTE))
        assertEquals(0, checkStart.get(Calendar.SECOND))

        val checkEnd = Calendar.getInstance().apply { time = capturedEnd }
        assertEquals(calendar.get(Calendar.DAY_OF_YEAR), checkEnd.get(Calendar.DAY_OF_YEAR))
        assertEquals(23, checkEnd.get(Calendar.HOUR_OF_DAY))
        assertEquals(59, checkEnd.get(Calendar.MINUTE))
        assertEquals(59, checkEnd.get(Calendar.SECOND))
    }
}