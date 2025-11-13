package com.example.dzialajproszelodowka

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.* // <-- WAŻNY IMPORT
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.example.dzialajproszelodowka.data.model.Product
import com.example.dzialajproszelodowka.ui.menu.MainMenuScreen
import com.example.dzialajproszelodowka.ui.start.StartScreen
import com.example.dzialajproszelodowka.ui.theme.DzialajProszeLodowkaTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- Twój kod testowy bazy danych (ZOSTAJE BEZ ZMIAN) ---
        val application = application as FridgeApplication
        val repository = application.repository

        lifecycleScope.launch {
            val dayInMillis = 1000 * 60 * 60 * 24
            val today = System.currentTimeMillis()

            Log.d("MOJA_BAZA", "Zaczynam dodawanie produktów...")
            repository.insertProduct(Product("Mleko", 1, Date(today + 2 * dayInMillis)))
            repository.insertProduct(Product("Jajka", 12, Date(today + 7 * dayInMillis)))
            repository.insertProduct(Product("Ser", 1, Date(today - 1 * dayInMillis)))
            Log.d("MOJA_BAZA", "Produkty dodane.")

            Log.d("MOJA_BAZA", "Pobieram produkty z bazy...")
            val produktyWBazie = repository.allProducts.first()

            if (produktyWBazie.isEmpty()) {
                Log.w("MOJA_BAZA", "Baza jest pusta. Coś poszło nie tak.")
            } else {
                Log.d("MOJA_BAZA", "--- ZNALEZIONO PRODUKTY ---")
                produktyWBazie.forEach { product ->
                    Log.d("MOJA_BAZA", " > ${product.name}, Ilość: ${product.amount}, Wygasa: ${product.expiryDate}")
                }
                Log.d("MOJA_BAZA", "----------------------------")
            }
        }
        // --- KONIEC KODU TESTOWEGO ---

        setContent {
            DzialajProszeLodowkaTheme {
                // Cała nawigacja dzieje się tutaj
                SmartFridgeApp()
            }
        }
    }
}

/**
 * Główny komponent zarządzający nawigacją.
 * Decyduje, który ekran pokazać.
 */
@Composable
fun SmartFridgeApp() {
    // Prosty stan do zarządzania nawigacją. Przechowuje "nazwę" ekranu.
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
                // Pokazujemy ekran startowy i mówimy mu:
                // "Gdy ktoś cię kliknie, zmień 'currentScreen' na 'Menu'"
                StartScreen(
                    onNavigateToMenu = { currentScreen = "Menu" }
                )
            }
            "Menu" -> {
                // Pokazujemy ekran menu.
                // Na razie przyciski będą pokazywać Toast.
                MainMenuScreen(
                    onNavigateToFridge = {
                        Toast.makeText(context, "TODO: Otwórz Lodówkę", Toast.LENGTH_SHORT).show()
                        // W przyszłości zmienisz to na:
                        // currentScreen = "FridgeList"
                    },
                    onNavigateToShoppingList = {
                        Toast.makeText(context, "TODO: Otwórz Listę Zakupów", Toast.LENGTH_SHORT).show()
                        // currentScreen = "ShoppingList"
                    },
                    onNavigateToRecipe = {
                        Toast.makeText(context, "TODO: Znajdź Przepis", Toast.LENGTH_SHORT).show()
                        // currentScreen = "RecipeFinder"
                    },
                    onNavigateToAddProduct = {
                        Toast.makeText(context, "TODO: Dodaj Produkt", Toast.LENGTH_SHORT).show()
                        // currentScreen = "AddProduct"
                    }
                )
            }
            // Tutaj w przyszłości dodasz kolejne ekrany:
            // "FridgeList" -> { FridgeListScreen(...) }
            // "ShoppingList" -> { ShoppingListScreen(...) }
        }
    }
}