package com.example.dzialajproszelodowka.ui.recipe

import android.media.browse.MediaBrowser
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
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

    // efekt w odpowiedzi na zmianę wartości
    // jeśli json się zmieni to zareaguje
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

                    // pole do wpisania tekstu
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

            Button(
                onClick = {
                    viewModel.searchWithFridgeIngredients()
                    keyboardController?.hide()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ElementColor)
            ) {
                Text(
                    text = "Cook with what's in fridge!",
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black)
            ) {
                VideoPlayer(videoResId = R.raw.eating_cat)
            }



//            Image(
//                painter = painterResource(id = R.drawable.kot),
//                contentDescription = "Cat",
//                modifier = Modifier.size(150.dp),
//                contentScale = ContentScale.Fit
//            )

            if (jsonResult.isNotEmpty() && !jsonResult.startsWith("Enter keywords") && !jsonResult.startsWith("Searching")) {
                TextButton(onClick = { onNavigateToResult() }) {
                    Text("Show Raw JSON Again", color = Color.DarkGray)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(videoResId: Int) {
    val context = LocalContext.current

    val exoPlayer = remember {
        // buduje odtwarzacz video
        ExoPlayer.Builder(context).build().apply {
            val uri = Uri.parse("android.resource://${context.packageName}/$videoResId")
            // zamieniam uri na odtwarzacz i przypisuję do mojego odtwarzacza
            val mediaItem = MediaItem.fromUri(uri)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = false
        }
    }

    // zamyka odtwarzacz żeby nie działał w tle
    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    // dzięki temu odtwarzacz jest traktowany jako fragment UI
    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = true
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

