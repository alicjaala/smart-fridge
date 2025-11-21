package com.example.dzialajproszelodowka.ui.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dzialajproszelodowka.R
import com.example.dzialajproszelodowka.ui.theme.DzialajProszeLodowkaTheme


private val poppinsFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

@Composable
fun StartScreen(
    onNavigateToMenu: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SmartFridge",
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 38.sp
            )

            Text(
                text = "Save Food & Eat Smarter",
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 22.sp
            )
        }

        Image(
            painter = painterResource(id = R.drawable.kot),
            contentDescription = "Kotek",
            modifier = Modifier.size(250.dp)
        )

        Button(
            onClick = {
                onNavigateToMenu()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE6A4B4),
                contentColor = Color.Black
            )
        ) {
            Text(text = "Get started!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    DzialajProszeLodowkaTheme {
        StartScreen(onNavigateToMenu = {})
    }
}