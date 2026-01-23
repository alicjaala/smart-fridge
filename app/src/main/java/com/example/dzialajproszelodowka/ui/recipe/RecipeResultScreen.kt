package com.example.dzialajproszelodowka.ui.recipe

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dzialajproszelodowka.data.model.InstructionDto
import com.example.dzialajproszelodowka.data.model.RecipeDto
import com.example.dzialajproszelodowka.data.model.RecipeListResponse
import com.example.dzialajproszelodowka.ui.theme.DzialajProszeLodowkaTheme
import com.google.gson.Gson

private val BgColor = Color(0xFFFCEEEE)
private val ElementColor = Color(0xFFDDB0A9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeResultScreen(
    jsonResult: String,
    onNavigateBack: () -> Unit,
    viewModel: RecipeViewModel? = null
) {
    val context = LocalContext.current


    // toast - powiadomienie, które pojawia się na krótko i znika
    val message by viewModel?.message?.collectAsState() ?: remember { mutableStateOf(null) }

    // to wykonuje się za każdym razem gdy message się zmieni
    // tworzy toast z tekstem
    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel?.clearMessage()
        }
    }

    // parsowanie jsonu na obiekt
    val recipe = remember(jsonResult) {
        try {
            val response = Gson().fromJson(jsonResult, RecipeListResponse::class.java)
            val results = response?.results

            if (results.isNullOrEmpty()) {
                null
            } else {
                val bestRecipe = results.find {
                    !it.analyzedInstructions.isNullOrEmpty() || !it.instructions.isNullOrBlank()
                }
                bestRecipe ?: results.first()
            }
        } catch (e: Exception) {
            null
        }
    }

    var showIngredientsSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BgColor,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        containerColor = BgColor
    ) { padding ->

        if (recipe == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No recipe found.", textAlign = TextAlign.Center)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = recipe.title ?: "Unknown Recipe",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Servings: ${recipe.servings ?: "?"}", fontSize = 18.sp, color = Color.Black)
                    Text("Ready in: ${recipe.readyInMinutes ?: "?"} minutes", fontSize = 18.sp, color = Color.Black)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { showIngredientsSheet = true },
                    modifier = Modifier.fillMaxWidth(0.8f).height(55.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElementColor)
                ) {
                    Text("Check ingredients.", fontSize = 18.sp, color = Color.Black)
                }

                Spacer(modifier = Modifier.height(30.dp))

                val instructionsList = recipe.analyzedInstructions
                val steps = instructionsList?.firstOrNull()?.steps ?: emptyList()

                if (steps.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        steps.forEach { step ->
                            Row(verticalAlignment = Alignment.Top) {
                                Text("${step.number}. ", fontWeight = FontWeight.Bold, color = Color.Black)
                                Text(step.step ?: "", color = Color.Black, lineHeight = 22.sp)
                            }
                        }
                    }
                } else if (!recipe.instructions.isNullOrBlank()) {
                    Text(recipe.instructions.cleanHtml(), color = Color.Black, lineHeight = 24.sp)
                } else if (!recipe.summary.isNullOrBlank()) {
                    Text("Recipe Summary:", fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(recipe.summary.cleanHtml(), color = Color.Black, lineHeight = 24.sp)
                } else {
                    Text("No instructions available.", color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        if (showIngredientsSheet && recipe != null) {
            // panel wysuwany z dołu
            ModalBottomSheet(
                onDismissRequest = { showIngredientsSheet = false },
                containerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .padding(bottom = 30.dp)
                ) {
                    Text(
                        text = "Ingredients",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (recipe.extendedIngredients.isEmpty()) {
                            Text("No ingredients info.")
                        } else {
                            recipe.extendedIngredients.forEach { ingredient ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(ElementColor, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = ingredient.original,
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                }
                                Divider(color = Color.LightGray.copy(alpha = 0.3f))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            viewModel?.generateShoppingList(recipe)
                            showIngredientsSheet = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElementColor)
                    ) {
                        Icon(Icons.Filled.AddShoppingCart, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate Shopping List", fontSize = 18.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}

private fun String.cleanHtml(): String {
    return this.replace(Regex("<.*?>"), "").replace("&nbsp;", " ")
}

@Preview(showBackground = true)
@Composable
fun RecipeResultScreenPreview() {
    DzialajProszeLodowkaTheme {
        RecipeResultScreen(jsonResult = "", onNavigateBack = {})
    }
}