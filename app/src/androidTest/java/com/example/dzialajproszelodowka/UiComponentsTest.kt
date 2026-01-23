package com.example.dzialajproszelodowka


import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.dzialajproszelodowka.data.model.Product
import com.example.dzialajproszelodowka.data.model.ShoppingItem
import com.example.dzialajproszelodowka.ui.fridge.EditQuantityDialog
import com.example.dzialajproszelodowka.ui.fridge.ProductListItem
import com.example.dzialajproszelodowka.ui.menu.MainMenuScreen
import com.example.dzialajproszelodowka.ui.menu.MenuButton
import com.example.dzialajproszelodowka.ui.recipe.RecipeResultScreen
import com.example.dzialajproszelodowka.ui.shopping.AddListCard
import com.example.dzialajproszelodowka.ui.shopping.ShoppingItemRow
import com.example.dzialajproszelodowka.ui.shopping.ShoppingListCard
import com.example.dzialajproszelodowka.ui.start.StartScreen
import org.junit.Rule
import org.junit.Test
import java.util.Date

class UiComponentsTest {

    // reguła pozwala testować i wyszukiwać elementy
    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun menuButton_displaysText() {
        val testText = "Sprawdź lodówkę"
        composeTestRule.setContent {
            MenuButton(text = testText, imageResId = android.R.drawable.ic_delete, onClick = {})
        }
        composeTestRule.onNodeWithText(testText).assertIsDisplayed()
    }

    @Test
    fun shoppingListCard_displaysListName() {
        val listName = "Zakupy na weekend"
        composeTestRule.setContent {
            ShoppingListCard(listName = listName, onClick = {})
        }
        composeTestRule.onNodeWithText(listName).assertIsDisplayed()
    }

    @Test
    fun addListCard_displaysStaticText() {
        composeTestRule.setContent {
            AddListCard(onClick = {})
        }
        composeTestRule.onNodeWithText("Add new list.").assertIsDisplayed()
    }

    @Test
    fun shoppingItemRow_displaysItemName() {
        val item = ShoppingItem(listId = 1, name = "Mleko 3.2%", isChecked = false)
        composeTestRule.setContent {
            ShoppingItemRow(item = item, onToggle = {})
        }
        composeTestRule.onNodeWithText("Mleko 3.2%").assertIsDisplayed()
    }

    @Test
    fun startScreen_displaysAppTitle() {
        composeTestRule.setContent {
            StartScreen(onNavigateToMenu = {})
        }
        composeTestRule.onNodeWithText("SmartFridge").assertIsDisplayed()
    }


    @Test
    fun startScreen_displaysGetStartedButton() {
        composeTestRule.setContent {
            StartScreen(onNavigateToMenu = {})
        }
        composeTestRule.onNodeWithText("Get started!").assertIsDisplayed()
    }

    @Test
    fun mainMenuScreen_displaysHeader() {
        composeTestRule.setContent {
            MainMenuScreen({}, {}, {}, {})
        }
        composeTestRule.onNodeWithText("How can we help you today?").assertIsDisplayed()
    }

    @Test
    fun productListItem_displaysNameAndAmount() {
        val product = Product(name = "Marchewka", amount = 5, expiryDate = Date())

        composeTestRule.setContent {
            ProductListItem(product = product, onDelete = {}, onClick = {})
        }

        composeTestRule.onNodeWithText("5x Marchewka").assertIsDisplayed()
    }

    @Test
    fun editQuantityDialog_displaysTitle() {
        val product = Product(name = "Jajka", amount = 10, expiryDate = Date())

        composeTestRule.setContent {
            EditQuantityDialog(product = product, onDismiss = {}, onConfirm = {})
        }

        composeTestRule.onNodeWithText("Edit Jajka").assertIsDisplayed()
    }

    @Test
    fun recipeResultScreen_displaysEmptyState() {
        composeTestRule.setContent {
            RecipeResultScreen(jsonResult = "", onNavigateBack = {}, viewModel = null)
        }

        composeTestRule.onNodeWithText("No recipe found.").assertIsDisplayed()
    }
}