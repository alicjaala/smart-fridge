package com.example.dzialajproszelodowka

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels // <-- WAŻNY IMPORT
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.example.dzialajproszelodowka.data.model.Product
import com.example.dzialajproszelodowka.ui.fridge.FridgeListScreen // <-- NOWY IMPORT
import com.example.dzialajproszelodowka.ui.fridge.FridgeViewModel // <-- NOWY IMPORT
import com.example.dzialajproszelodowka.ui.fridge.FridgeViewModelFactory // <-- NOWY IMPORT
import com.example.dzialajproszelodowka.ui.menu.MainMenuScreen
import com.example.dzialajproszelodowka.ui.start.StartScreen
import com.example.dzialajproszelodowka.ui.theme.DzialajProszeLodowkaTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date


class MainActivity : ComponentActivity() {

    private val fridgeViewModel: FridgeViewModel by viewModels {
        FridgeViewModelFactory((application as FridgeApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = application as FridgeApplication
        val repository = application.repository

        if (savedInstanceState == null) {
            lifecycleScope.launch {
                val dayInMillis = 1000 * 60 * 60 * 24
                val today = System.currentTimeMillis()

                Log.d("MOJA_BAZA", "Zaczynam dodawanie produktów...")

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

                testProducts.forEach { product ->
                    repository.insertProduct(product)
                }

                Log.d("MOJA_BAZA", "Dodano ${testProducts.size} produktów do bazy.")

                Log.d("MOJA_BAZA", "Pobieram produkty z bazy...")
                val produktyWBazie = repository.allProducts.first()

                if (produktyWBazie.isEmpty()) {
                    Log.w("MOJA_BAZA", "Baza jest pusta. Coś poszło nie tak.")
                } else {
                    Log.d("MOJA_BAZA", "--- ZNALEZIONO PRODUKTY ---")
                    produktyWBazie.forEach { product ->
                        Log.d(
                            "MOJA_BAZA",
                            " > ${product.name}, Ilość: ${product.amount}, Wygasa: ${product.expiryDate}"
                        )
                    }
                    Log.d("MOJA_BAZA", "----------------------------")
                }
            }
        }

        setContent {
            DzialajProszeLodowkaTheme {
                SmartFridgeApp(viewModel = fridgeViewModel)
            }
        }
    }
}

@Composable
fun SmartFridgeApp(
    viewModel: FridgeViewModel // <-- Przyjmujemy nasz ViewModel
) {
    var currentScreen by remember { mutableStateOf("Start") }
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFCEEEE)
    ) {
        // "when" działa jak przełącznik. Sprawdza, co jest w 'currentScreen'
        // i na tej podstawie rysuje odpowiedni ekran.
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
                        Toast.makeText(context, "TODO: Otwórz Listę Zakupów", Toast.LENGTH_SHORT).show()
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
                    viewModel = viewModel,
                    onNavigateBack = { currentScreen = "Menu" }
                )
            }
        }
    }
}