package com.example.dzialajproszelodowka

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.dzialajproszelodowka.data.model.Product
import com.example.dzialajproszelodowka.data.repository.BarcodeRepository
import com.example.dzialajproszelodowka.ui.fridge.AddProductScreen
import com.example.dzialajproszelodowka.ui.fridge.FridgeListScreen
import com.example.dzialajproszelodowka.ui.fridge.FridgeViewModel
import com.example.dzialajproszelodowka.ui.fridge.FridgeViewModelFactory
import com.example.dzialajproszelodowka.ui.menu.MainMenuScreen
import com.example.dzialajproszelodowka.ui.recipe.RecipeResultScreen
import com.example.dzialajproszelodowka.ui.recipe.RecipeScreen
import com.example.dzialajproszelodowka.ui.recipe.RecipeViewModel
import com.example.dzialajproszelodowka.ui.recipe.RecipeViewModelFactory
import com.example.dzialajproszelodowka.ui.scanner.BarcodeScannerScreen
import com.example.dzialajproszelodowka.ui.shopping.ShoppingListDetailsScreen
import com.example.dzialajproszelodowka.ui.shopping.ShoppingListsScreen
import com.example.dzialajproszelodowka.ui.shopping.ShoppingViewModel
import com.example.dzialajproszelodowka.ui.shopping.ShoppingViewModelFactory
import com.example.dzialajproszelodowka.ui.start.StartScreen
import com.example.dzialajproszelodowka.ui.theme.DzialajProszeLodowkaTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date


class MainActivity : ComponentActivity() {

    private val fridgeViewModel: FridgeViewModel by viewModels {
        FridgeViewModelFactory((application as FridgeApplication).repository)
    }

    private val shoppingViewModel: ShoppingViewModel by viewModels {
        ShoppingViewModelFactory((application as FridgeApplication).shoppingRepository)
    }

    private val recipeViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory((application as FridgeApplication).recipeRepository)
    }

    private val barcodeRepository = BarcodeRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (true) {
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
                        Product("Mleko", 1, Date(today + 1 * dayInMillis)),
                        Product("Jajka", 12, Date(today * dayInMillis)),
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
                RequestNotificationPermission()

                SmartFridgeApp(
                    fridgeViewModel = fridgeViewModel,
                    shoppingViewModel = shoppingViewModel,
                    recipeViewModel = recipeViewModel,
                    barcodeRepository = barcodeRepository
                )
            }
        }
    }
}

@Composable
fun RequestNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
            }
        )

        LaunchedEffect(Unit) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

@Composable
fun SmartFridgeApp(
    fridgeViewModel: FridgeViewModel,
    shoppingViewModel: ShoppingViewModel,
    recipeViewModel: RecipeViewModel,
    barcodeRepository: BarcodeRepository
) {
    var currentScreen by remember { mutableStateOf("Start") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var newProductName by remember { mutableStateOf("") }
    var showAddOptionsDialog by remember { mutableStateOf(false) }
    var showManualInputDialog by remember { mutableStateOf(false) }
    var isFetchingProductInfo by remember { mutableStateOf(false) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                currentScreen = "BarcodeScanner"
            } else {
                Toast.makeText(context, "Camera permission needed to scan", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFCEEEE)
    ) {
        when (currentScreen) {
            "Start" -> {
                StartScreen(onNavigateToMenu = { currentScreen = "Menu" })
            }

            "Menu" -> {
                MainMenuScreen(
                    onNavigateToFridge = { currentScreen = "FridgeList" },
                    onNavigateToShoppingList = { currentScreen = "ShoppingLists" },
                    onNavigateToRecipe = { currentScreen = "RecipeScreen" },
                    onNavigateToProduct = { showAddOptionsDialog = true }
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
                    onNavigateBack = { currentScreen = "Menu" },
                    onListClick = { listId ->
                        shoppingViewModel.selectList(listId)
                        currentScreen = "ShoppingDetails"
                    }
                )
            }

            "ShoppingDetails" -> {
                ShoppingListDetailsScreen(
                    viewModel = shoppingViewModel,
                    onNavigateBack = { currentScreen = "ShoppingLists" }
                )
            }

            "RecipeScreen" -> {
                RecipeScreen(
                    viewModel = recipeViewModel,
                    onNavigateBack = { currentScreen = "Menu" },
                    onNavigateToResult = { currentScreen = "RecipeResult" }
                )
            }

            "RecipeResult" -> {
                val resultJson = recipeViewModel.searchResultJson.collectAsState().value

                RecipeResultScreen(
                    jsonResult = resultJson,
                    onNavigateBack = {
                        recipeViewModel.resetSearchState()
                        currentScreen = "RecipeScreen"
                    }
                )
            }

            "BarcodeScanner" -> {
                BarcodeScannerScreen(
                    onBarcodeDetected = { barcode ->
                        isFetchingProductInfo = true
                        currentScreen = "Menu"

                        scope.launch {
                            val name = withContext(Dispatchers.IO) {
                                barcodeRepository.getProductName(barcode)
                            }

                            isFetchingProductInfo = false

                            if (name != null) {
                                newProductName = name as String
                                currentScreen = "AddProduct"
                            } else {
                                Toast.makeText(context, "Product not found. Try manually.", Toast.LENGTH_SHORT).show()
                                showManualInputDialog = true
                            }
                        }
                    },
                    onCancel = { currentScreen = "Menu" }
                )
            }

            "AddProduct" -> {
                AddProductScreen(
                    productName = newProductName,
                    viewModel = fridgeViewModel,
                    onNavigateBack = { currentScreen = "Menu" },
                    onSaveSuccess = {
                        currentScreen = "FridgeList"
                    }
                )
            }
        }


        if (isFetchingProductInfo) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Fetching product info...") },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator()
                    }
                },
                confirmButton = {}
            )
        }

        if (showAddOptionsDialog) {
            AlertDialog(
                onDismissRequest = { showAddOptionsDialog = false },
                title = { Text("Add Product") },
                text = { Text("Choose how you want to add the product:") },
                confirmButton = {
                    Button(onClick = {
                        showAddOptionsDialog = false
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }) {
                        Icon(Icons.Filled.CameraAlt, contentDescription = null)
                        Text(" Scan Barcode", modifier = Modifier.padding(start = 8.dp))
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showAddOptionsDialog = false
                        showManualInputDialog = true
                    }) {
                        Icon(Icons.Filled.Edit, contentDescription = null)
                        Text(" Type Manually", modifier = Modifier.padding(start = 8.dp))
                    }
                }
            )
        }

        if (showManualInputDialog) {
            var tempName by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showManualInputDialog = false },
                title = { Text("Enter Name") },
                text = {
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("Product Name") }
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        if (tempName.isNotBlank()) {
                            newProductName = tempName
                            showManualInputDialog = false
                            currentScreen = "AddProduct"
                        }
                    }) {
                        Text("Next")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showManualInputDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}