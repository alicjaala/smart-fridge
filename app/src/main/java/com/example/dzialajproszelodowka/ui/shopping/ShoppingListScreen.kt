package com.example.dzialajproszelodowka.ui.shopping

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dzialajproszelodowka.data.model.ShoppingList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListsScreen(
    viewModel: ShoppingViewModel,
    onNavigateBack: () -> Unit
) {
    val lists by viewModel.allLists.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Choose a list:", fontSize = 24.sp) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFCEEEE)
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Wyświetlamy istniejące listy
            items(lists) { list ->
                ShoppingListCard(listName = list.name, onClick = { /* TODO: Otwórz szczegóły listy */ })
            }

            // Przycisk dodawania nowej listy (na dole listy, jak na zrzucie)
            item {
                AddListCard(onClick = { showAddDialog = true })
            }
        }

        // Okienko dialogowe do wpisania nazwy nowej listy
        if (showAddDialog) {
            var newListName by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("New List") },
                text = {
                    OutlinedTextField(
                        value = newListName,
                        onValueChange = { newListName = it },
                        label = { Text("List Name") }
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        if (newListName.isNotBlank()) {
                            viewModel.addList(newListName)
                            showAddDialog = false
                        }
                    }) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

// Karta dla istniejącej listy (brązowa)
@Composable
fun ShoppingListCard(listName: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDDB0A9)) // Brudny róż/brąz
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Białe kółko z ikoną listy
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ListAlt,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = Color.DarkGray
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            Text(text = listName, fontSize = 20.sp, fontWeight = FontWeight.Medium)
        }
    }
}

// Karta dla przycisku "Add new list" (taka sama, ale z plusem)
@Composable
fun AddListCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDDB0A9))
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            Text(text = "Add new list.", fontSize = 20.sp, fontWeight = FontWeight.Medium)
        }
    }
}