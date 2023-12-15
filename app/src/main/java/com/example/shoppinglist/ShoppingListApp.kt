package com.example.shoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)

@Composable
fun ShoppingListApp() {
    var sItems by remember {
        mutableStateOf(listOf<ShoppingItem>())
    }
    var showDialogue by remember {
        mutableStateOf(false)
    }
    var itemName by remember {
        mutableStateOf("")
    }
    var itemQuantity by remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            showDialogue = true
        }) {
            Text(text = "Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(sItems) { item ->
                if (item.isEditing) {
                    ShoppingItemEditor(item = item, onEditingComplete = { nameEdit, quantityEdit ->
                        sItems = sItems.map { it.copy(isEditing = false) }
                        val editingItem = sItems.findLast {
                            it.id == item.id
                        }
                        editingItem?.let {
                            it.name = nameEdit
                            it.quantity = quantityEdit
                        }

                    })
                } else {
                    ShoppingListItem(item = item, onEditingClick = {
                        sItems = sItems.map { it.copy(isEditing = it.id == item.id) }
                    }, onDeleteClick = {
                        sItems = sItems - item
                    })
                }
            }
        }
        if (showDialogue) {

            AlertDialog(
                title = { Text(text = "Add Item!!") },
                onDismissRequest = {
                    showDialogue = false

                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = itemName,
                            onValueChange = {
                                itemName = it
                            },
                            modifier = Modifier.padding(8.dp), label = { Text(text = "Item Name") }
                        )
                        OutlinedTextField(
                            value = itemQuantity,
                            onValueChange = { itemQuantity = it },
                            modifier = Modifier.padding(8.dp), label = { Text(text = "Quantity") }
                        )
                    }

                },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = {
                            if (itemName.isNotBlank()) {
                                val newItem = ShoppingItem(
                                    id = sItems.size + 1,
                                    name = itemName,
                                    quantity = itemQuantity.toInt()
                                )
                                sItems = sItems + newItem
                            }
                            itemName = ""
                            itemQuantity = ""
                            showDialogue = false
                        }) {
                            Text(text = "Add")
                        }
                        Button(onClick = {
                            showDialogue = false
                        }) {
                            Text(text = "Cancel")
                        }
                    }

                },

                )
        }
    }
}

@Composable
fun ShoppingItemEditor(item: ShoppingItem, onEditingComplete: (String, Int) -> Unit) {
    var editedName by remember {
        mutableStateOf(item.name)
    }
    var editedQuantity by remember {
        mutableStateOf(item.quantity.toString())
    }
    var isEditing by remember {
        mutableStateOf(item.isEditing)
    }
    Row {
        Column {
            BasicTextField(
                value = editedName, onValueChange = { editedName = it },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(
                value = editedQuantity, onValueChange = { editedQuantity = it },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )

        }
        Button(onClick = {
            isEditing = false
            onEditingComplete(editedName, editedQuantity.toIntOrNull() ?: item.quantity)
        }) {
            Text(text = "Save")
        }
    }
}


@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditingClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(
                    width = 2.dp,
                    Color.Cyan
                ),
                shape = RoundedCornerShape(percent = 20)
            ), horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = item.name)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = item.quantity.toString())
        IconButton(onClick = onEditingClick) {
            Icon(Icons.Default.Create, contentDescription = "Edit")
        }
        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}