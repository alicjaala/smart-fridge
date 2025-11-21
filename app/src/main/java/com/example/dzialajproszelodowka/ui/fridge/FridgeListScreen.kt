package com.example.dzialajproszelodowka.ui.fridge

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
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
    // zbieram aktualną listę produktów z viewModel
    val products by viewModel.allProduct.collectAsState()

    // scaffold - gotowy layout
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your fridge:") },
                navigationIcon = {
                    // przycisk cofania do menu
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFCEEEE)
                )
            )
        },
        containerColor = Color(0xFFFCEEEE)
    ) { padding ->
        // przewijalna lista elementów
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding) // dzięki temu lista jest pod nagłówkiem
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp) // odstępy między elementami
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
                        // przycisk usuwania
                        onDelete = { viewModel.deleteProduct(product) }
                    )
                }
            }
        }
    }
}


// funkcja pokazująca 1 produkt z lodówki
@Composable
fun ProductListItem(
    product: Product,
    onDelete: () -> Unit
) {
    val (color, daysUntilExpiry) = getExpiryStatus(product.expiryDate)

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formattedDate = dateFormatter.format(product.expiryDate)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // kolorowa kropka
            Canvas(modifier = Modifier.size(24.dp)) {
                drawCircle(color = color)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "${product.amount}x ${product.name}",
                modifier = Modifier.weight(1f), // Zajmuje dostępną przestrzeń
                fontSize = 16.sp
            )

            // linia oddzielająca datę
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
                modifier = Modifier.width(90.dp) // Stała szerokość dla wyrównania
            )

            // przycisk usuwania
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