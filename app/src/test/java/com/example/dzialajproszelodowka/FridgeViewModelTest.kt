package com.example.dzialajproszelodowka



import com.example.dzialajproszelodowka.data.repository.ProductRepository
import com.example.dzialajproszelodowka.ui.fridge.FridgeViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class FridgeMainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class FridgeViewModelTest {

    @get:Rule
    val mainDispatcherRule = FridgeMainDispatcherRule()

    private val repository = mockk<ProductRepository>(relaxed = true)

    private lateinit var viewModel: FridgeViewModel

    @Test
    fun `test addProduct`() = runTest {
        viewModel = FridgeViewModel(repository)

        val name = "Marchewka"
        val amount = 5
        val date = Date()

        viewModel.addProduct(name, amount, date)

        coVerify {
            repository.insertProduct(match {
                it.name == name && it.amount == amount && it.expiryDate == date
            })
        }
    }
}