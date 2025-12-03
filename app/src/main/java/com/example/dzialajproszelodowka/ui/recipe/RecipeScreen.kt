package com.example.dzialajproszelodowka.ui.recipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dzialajproszelodowka.R
import com.example.dzialajproszelodowka.data.api.RetrofitClient
import com.example.dzialajproszelodowka.data.repository.RecipeRepository
import com.example.dzialajproszelodowka.ui.theme.DzialajProszeLodowkaTheme

private val BgColor = Color(0xFFFCEEEE)
private val ElementColor = Color(0xFFDDB0A9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    viewModel: RecipeViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToResult: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    val jsonResult by viewModel.searchResultJson.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(jsonResult) {
        if (jsonResult.isNotEmpty() &&
            !jsonResult.startsWith("Enter keywords") &&
            !jsonResult.startsWith("Searching")) {
            onNavigateToResult()
        }
    }

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(
                text = "What would you like to eat?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = ElementColor)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Enter a keyword or dishname..",
                        fontSize = 16.sp,
                        color = Color.Black.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(55.dp)
                            .clip(RoundedCornerShape(50)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        // Obsługa klawiatury
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                viewModel.searchRecipes(searchText)
                                keyboardController?.hide()
                            }
                        )
                    )
                }
            }

            Button(
                onClick = {
                    viewModel.searchRecipes(searchText)
                    keyboardController?.hide()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ElementColor)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Search Recipes", color = Color.Black)
                }
            }

            Image(
                painter = painterResource(id = R.drawable.kot),
                contentDescription = "Cat",
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Fit
            )

            if (jsonResult.isNotEmpty() && !jsonResult.startsWith("Enter keywords") && !jsonResult.startsWith("Searching")) {
                TextButton(onClick = { onNavigateToResult() }) {
                    Text("Show Raw JSON Again", color = Color.DarkGray)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeScreenPreview() {
    DzialajProszeLodowkaTheme {
        val fakeRepo = RecipeRepository(RetrofitClient.api)
        val fakeVM = RecipeViewModel(fakeRepo)
        RecipeScreen(
            viewModel = fakeVM,
            onNavigateBack = {},
            onNavigateToResult = {}
        )
    }
}