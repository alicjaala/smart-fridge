package com.example.dzialajproszelodowka.ui.menu

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dzialajproszelodowka.ui.theme.DzialajProszeLodowkaTheme

@Composable
fun MainMenuScreen(
    onNavigateToFridge: () -> Unit,
    onNavigateToShoppingList: () -> Unit,
    onNavigateToRecipe: () -> Unit,
    onNavigateToAddProduct: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFCEEEE)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "How can we help you today?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 40.dp)
            )

            MenuButton(
                text = "Check what's in your fridge.",
                icon = Icons.Default.Kitchen,
                onClick = onNavigateToFridge
            )

            Spacer(modifier = Modifier.height(20.dp))

            MenuButton(
                text = "Make a shopping list.",
                icon = Icons.Default.ShoppingCart,
                onClick = onNavigateToShoppingList
            )

            Spacer(modifier = Modifier.height(20.dp))

            MenuButton(
                text = "Find a recipe.",
                icon = Icons.Default.RestaurantMenu,
                onClick = onNavigateToRecipe
            )

            Spacer(modifier = Modifier.height(20.dp))

            MenuButton(
                text = "Add new product.",
                icon = Icons.Default.Add,
                onClick = onNavigateToAddProduct
            )
        }
    }
}

@Composable
fun MenuButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color(0xFFE6A4B4)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = Color.White,
                modifier = Modifier.size(70.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = text,
                color = Color.Black,
                fontSize = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuScreenPreview() {
    DzialajProszeLodowkaTheme {
        MainMenuScreen({},{}, {}, {})
    }
}