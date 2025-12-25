package com.example.dzialajproszelodowka.ui.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dzialajproszelodowka.R

@Composable
fun MainMenuScreen(
    onNavigateToFridge: () -> Unit,
    onNavigateToShoppingList: () -> Unit,
    onNavigateToRecipe: () -> Unit,
    onNavigateToProduct: () -> Unit
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
                imageResId = R.drawable.menu_fridge,
                onClick = onNavigateToFridge,
                imageScale = 2f
            )

            Spacer(modifier = Modifier.height(20.dp))

            MenuButton(
                text = "Make a shopping list.",
                imageResId = R.drawable.menu_list,
                onClick = onNavigateToShoppingList,
                imageScale = 2f
            )

            Spacer(modifier = Modifier.height(20.dp))

            MenuButton(
                text = "Find a recipe.",
                imageResId = R.drawable.menu_chef,
                onClick = onNavigateToRecipe
            )

            Spacer(modifier = Modifier.height(20.dp))

            MenuButton(
                text = "Add new product.",
                imageResId = R.drawable.menu_add,
                onClick = onNavigateToProduct,
                imageScale = 2f
            )
        }
    }
}

@Composable
fun MenuButton(
    text: String,
    imageResId: Int,
    onClick: () -> Unit,
    imageScale: Float = 1f
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE6A4B4)
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
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = text,
                    modifier = Modifier
                        .padding(12.dp)
                        .scale(imageScale),
                    contentScale = ContentScale.Fit
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