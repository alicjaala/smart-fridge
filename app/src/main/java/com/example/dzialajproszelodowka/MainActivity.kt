package com.example.dzialajproszelodowka

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.example.dzialajproszelodowka.data.model.Product
import com.example.dzialajproszelodowka.ui.fridge.FridgeListScreen
import com.example.dzialajproszelodowka.ui.fridge.FridgeViewModel
import com.example.dzialajproszelodowka.ui.fridge.FridgeViewModelFactory
import com.example.dzialajproszelodowka.ui.menu.MainMenuScreen
import com.example.dzialajproszelodowka.ui.shopping.ShoppingListsScreen // <-- Ekran List Zakupów
import com.example.dzialajproszelodowka.ui.shopping.ShoppingViewModel // <-- ViewModel List Zakupów
import com.example.dzialajproszelodowka.ui.shopping.ShoppingViewModelFactory // <-- Factory List Zakupów
import com.example.dzialajproszelodowka.ui.start.StartScreen
import com.example.dzialajproszelodowka.ui.theme.DzialajProszeLodowkaTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date


class MainActivity : ComponentActivity() {

    private val fridgeViewModel: FridgeViewModel by viewModels {
        FridgeViewModelFactory((application as FridgeApplication).repository)
    }

    private val shoppingViewModel: ShoppingViewModel by viewModels {
        ShoppingViewModelFactory((application as FridgeApplication).shoppingRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val application = application as FridgeApplication
            val repository = application.repository
            lifecycleScope.launch {
                val dayInMillis = 1000 * 60 * 60 * 24
                val today = System.currentTimeMillis()

                Log.d("MOJA_BAZA", "Sprawdzanie bazy danych...")
                val produktyWBazie = repository.allProducts.first()

                if (produktyWBazie.isEmpty()) {
                    Log.d("MOJA_BAZA", "Baza pusta. Dodaję produkty testowe...")
                    val testProducts = listOf(
                        Product("Mleko", 1, Date(today + 2 * dayInMillis)),
                        Product("Jajka", 12, Date(today + 7 * dayInMillis)),
                        Product("Ser żółty", 1, Date(today - 1 * dayInMillis)),
                        Product("Jogurt naturalny", 4, Date(today + 4 * dayInMillis)),
                        Product("Masło", 2, Date(today + 10 * dayInMillis)),
                        Product("Szynka", 1, Date(today + 1 * dayInMillis)),
                        Product("Ketchup", 1, Date(today + 60 * dayInMillis)),
                        Product("Sałata", 1, Date(today + 2 * dayInMillis)),
                        Product("Pomidor", 3, Date(today + 3 * dayInMillis)),
                        Product("Ogórek", 2, Date(today + 5 * dayInMillis)),
                        Product("Szpinak", 1, Date(today - 3 * dayInMillis)),
                        Product("Chleb", 1, Date(today + 1 * dayInMillis)),
                        Product("Sok pomarańczowy", 1, Date(today + 15 * dayInMillis)),
                        Product("Kefir", 2, Date(today + 2 * dayInMillis)),
                        Product("Parówki", 1, Date(today - 2 * dayInMillis))
                    )
                    testProducts.forEach { repository.insertProduct(it) }
                    Log.d("MOJA_BAZA", "Dodano ${testProducts.size} produktów.")
                }
            }
        }

        setContent {
            DzialajProszeLodowkaTheme {
                SmartFridgeApp(
                    fridgeViewModel = fridgeViewModel,
                    shoppingViewModel = shoppingViewModel
                )
            }
        }
    }
}

@Composable
fun SmartFridgeApp(
    fridgeViewModel: FridgeViewModel,
    shoppingViewModel: ShoppingViewModel // Parametr dla Listy Zakupów
) {
    var currentScreen by remember { mutableStateOf("Start") }

    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFCEEEE)
    ) {
        when (currentScreen) {
            "Start" -> {
                StartScreen(
                    onNavigateToMenu = { currentScreen = "Menu" }
                )
            }

            "Menu" -> {
                MainMenuScreen(
                    onNavigateToFridge = {
                        currentScreen = "FridgeList"
                    },
                    onNavigateToShoppingList = {
                        currentScreen = "ShoppingLists"
                    },
                    onNavigateToRecipe = {
                        Toast.makeText(context, "TODO: Znajdź Przepis", Toast.LENGTH_SHORT).show()
                    },
                    onNavigateToAddProduct = {
                        Toast.makeText(context, "TODO: Dodaj Produkt", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            "FridgeList" -> {
                FridgeListScreen(
                    viewModel = fridgeViewModel,
                    onNavigateBack = { currentScreen = "Menu" }
                )
            }

            "ShoppingLists" -> {
                ShoppingListsScreen(
                    viewModel = shoppingViewModel,
                    onNavigateBack = { currentScreen = "Menu" }
                )
            }
        }
    }
}