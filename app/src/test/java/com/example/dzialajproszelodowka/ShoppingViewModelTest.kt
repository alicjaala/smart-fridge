package com.example.dzialajproszelodowka.ui.shopping

import com.example.dzialajproszelodowka.data.model.ShoppingItem
import com.example.dzialajproszelodowka.data.model.ShoppingList
import com.example.dzialajproszelodowka.data.repository.ShoppingRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.Assert.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
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
class ShoppingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<ShoppingRepository>(relaxed = true)
    private lateinit var viewModel: ShoppingViewModel

    private val testList = ShoppingList(id = 1, name = "Lidl")
    private val testItem = ShoppingItem(id = 10, listId = 1, name = "Mleko", isChecked = false)

    @Before
    fun setUp() {
        every { repository.allLists } returns flowOf(listOf(testList))
        every { repository.getItemsForList(any()) } returns flowOf(listOf(testItem))

        viewModel = ShoppingViewModel(repository)
    }

    @Test
    fun `test selectList`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.allLists.collect()
        }
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.currentListName.collect()
        }

        viewModel.selectList(1)
        assertEquals("Lidl", viewModel.currentListName.value)
    }

    @Test
    fun `test addList`() = runTest {
        viewModel.addList("Biedronka")
        coVerify { repository.insertList("Biedronka") }
    }

    @Test
    fun `test addItemToCurrentList`() = runTest {
        viewModel.selectList(1)
        viewModel.addItemToCurrentList("Chleb")

        coVerify {
            repository.insertItem(match {
                it.listId == 1 && it.name == "Chleb" && !it.isChecked
            })
        }
    }

    @Test
    fun `test toggleItem`() = runTest {
        val item = ShoppingItem(id = 5, listId = 1, name = "Jajka", isChecked = false)

        viewModel.toggleItem(item)

        coVerify {
            repository.updateItem(match {
                it.id == 5 && it.isChecked == true
            })
        }
    }

    @Test
    fun `test deleteCurrentList`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.allLists.collect()
        }

        viewModel.selectList(1)
        viewModel.deleteCurrentList()

        coVerify {
            repository.deleteList(match { it.id == 1 })
        }
    }
}