package com.example.dzialajproszelodowka.ui.fridge

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dzialajproszelodowka.data.model.Product
import com.example.dzialajproszelodowka.data.repository.ProductRepository
import com.example.dzialajproszelodowka.ui.theme.DzialajProszeLodowkaTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FridgeListScreen(
    viewModel: FridgeViewModel,
    onNavigateBack: () -> Unit
) {
    val products by viewModel.allProduct.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your fridge:") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                      }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFCEEEE),
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        containerColor = Color(0xFFFCEEEE)
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            if (products.isEmpty()) {
                item {
                    Text(
                        text = "Your fridge is empty.\nGo to 'Add new product' to add something!",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    )
                }
            } else {
                items(products, key = { it.name }) { product ->
                    ProductListItem(
                        product = product,
                        onDelete = { viewModel.deleteProduct(product) },
                        onClick = {
                            selectedProduct = product
                            showEditDialog = true
                        }
                    )
                }
            }
        }

        if (showEditDialog && selectedProduct != null) {
            EditQuantityDialog(
                product = selectedProduct!!,
                onDismiss = {
                    showEditDialog = false
                    selectedProduct = null
                },
                onConfirm = { newAmount ->
                    viewModel.addProduct(
                        name = selectedProduct!!.name,
                        amount = newAmount,
                        expiryDate = selectedProduct!!.expiryDate
                    )
                    showEditDialog = false
                    selectedProduct = null
                }
            )
        }
    }
}

@Composable
fun EditQuantityDialog(
    product: Product,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var amountText by remember { mutableStateOf(product.amount.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit ${product.name}") },
        text = {
            Column {
                Text("Enter new quantity:")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Zamieniamy tekst na liczbę, jeśli się nie uda, zostaje stara ilość
                    val newAmount = amountText.toIntOrNull() ?: product.amount
                    onConfirm(newAmount)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ProductListItem(
    product: Product,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    val (color, daysUntilExpiry) = getExpiryStatus(product.expiryDate)

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formattedDate = dateFormatter.format(product.expiryDate)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Canvas(modifier = Modifier.size(24.dp)) {
                drawCircle(color = color)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "${product.amount}x ${product.name}",
                modifier = Modifier.weight(1f),
                fontSize = 16.sp
            )

            Canvas(modifier = Modifier
                .height(30.dp)
                .width(1.dp)) {
                drawLine(
                    color = Color.LightGray,
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = 0f, y = size.height),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = formattedDate,
                fontSize = 14.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.width(90.dp)
            )

            IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Gray)
            }
        }
    }
}

private fun getExpiryStatus(expiryDate: Date): Pair<Color, Long> {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val today = calendar.time

    val expiryCal = Calendar.getInstance()
    expiryCal.time = expiryDate
    expiryCal.set(Calendar.HOUR_OF_DAY, 0)
    expiryCal.set(Calendar.MINUTE, 0)
    expiryCal.set(Calendar.SECOND, 0)
    expiryCal.set(Calendar.MILLISECOND, 0)
    val expiryNoTime = expiryCal.time

    val diffInMillis = expiryNoTime.time - today.time
    val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)

    val color = when {
        days < 0 -> Color(0xFFE57373)
        days <= 3 -> Color(0xFFFFEB3B)
        else -> Color(0xFF81C784)
    }

    return Pair(color, days)
}

