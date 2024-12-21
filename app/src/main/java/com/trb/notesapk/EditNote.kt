package com.trb.notesapk

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.trb.notesapk.ui.theme.MintChip
import com.trb.notesapk.ui.theme.QuickFreeze
import androidx.compose.material3.Text as Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trb.notesapk.roomDB.NotesDatabase
import com.trb.notesapk.roomDB.NotesRepo
import com.trb.notesapk.ui.theme.BlackMetal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNote(navController: NavController, id: Int, title: String, description: String) {
    var inputTitle by remember { mutableStateOf(TextFieldValue("")) }
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var isvalidTitle by remember { mutableStateOf(true) }
    var errorTitle by remember { mutableStateOf("") }
    var isvalidDescription by remember { mutableStateOf(true) }
    var errorDescription by remember { mutableStateOf("") }
    val notesDatabase = NotesDatabase.getDatabase(LocalContext.current)
    val notesRepo = NotesRepo(notesDatabase.notesDao())
    val isNewNote = (id == -1)

    LaunchedEffect(Unit) {
        inputTitle = TextFieldValue(title)
        inputText = TextFieldValue(description)
    }
    Log.d("TopAppBar", "Rendering delete button for note with ID: $id")

    Scaffold(
        Modifier.background(BlackMetal),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(40.dp),
                onClick = {
                    if (isNewNote) {
                        notesRepo.insertNotes(
                            title = inputTitle.text,
                            description = inputText.text
                        )
                    } else {
                        notesRepo.updateNotes(
                            id = id,
                            title = inputTitle.text,
                            description = inputText.text
                        )
                    }
                    navController.popBackStack() // Navigate back after saving
                }
            ) {
                Icon(Icons.Default.ThumbUp, "Save Note", tint = Color.Black)
            }


        },
        containerColor = MintChip,
        topBar = {
            TopAppBar(
                title = { /* Optional title */ },
                navigationIcon = {
                    Card(
                        modifier = Modifier.padding(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = QuickFreeze
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                            contentDescription = "BackArrow",
                            modifier = Modifier
                                .size(36.dp) // Icon size adjusted
                                .clip(CircleShape)
                                .clickable {
                                    navController.popBackStack() // Navigate back
                                }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MintChip
                ),
                actions = {
                    if (id != -1) { // Show delete icon only if it's an existing note (id != -1)
                        IconButton(onClick = {
                            try {
                                notesRepo.deleteNotes(id)
                                navController.popBackStack() // Navigate back after deleting
                            } catch (e: Exception) {
                                Log.e("Delete", "Error deleting note", e)
                            }
                        }) {
                            Icon(Icons.Default.Delete, "delete", tint = BlackMetal)
                        }
                    }
                }
            )
        },
        content = { innerPadding ->

            Column(
                Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxHeight()
            ) {
                Spacer(
                    Modifier
                        .height(56.dp)
                        .padding(innerPadding))


                Spacer(Modifier.height(38.dp))

                // Title TextField
                TextField(
                    value = inputTitle,
                    onValueChange = {
                        errorTitle = isvalidTitleCondition(it.text)
                        inputTitle = it
                        isvalidTitle = errorTitle.isEmpty()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Title",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold, color = Color.DarkGray
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MintChip,
                        unfocusedContainerColor = MintChip,
                        errorContainerColor = MintChip,
                        unfocusedIndicatorColor = MintChip
                    ),
                    textStyle = TextStyle.Default.copy(
                        fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.Black
                    ),
                    maxLines = 2,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    isError = !isvalidTitle
                )

                if (!isvalidTitle) {
                    Text(
                        text = errorTitle,
                        color = Color.Red,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp)
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Description TextField
                TextField(
                    value = inputText,
                    onValueChange = {
                        if (inputText.text.isNotEmpty() && errorTitle.isNotEmpty()) {
                            errorTitle = ""
                            isvalidTitle = true
                        }
                        inputText = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Description",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light,
                            color = Color.DarkGray
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MintChip,
                        unfocusedContainerColor = MintChip,
                        errorContainerColor = Color.Red,
                        unfocusedIndicatorColor = MintChip
                    ),
                    textStyle = TextStyle.Default.copy(
                        fontSize = 20.sp, fontWeight = FontWeight.Light,
                        color = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    isError = !isvalidDescription
                )

                if (!isvalidDescription) {
                    Text(
                        text = errorDescription,
                        color = Color.Red,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp)
                    )
                }
            }

        }
    )
}


fun isvalidTitleCondition(input: String): String {
    return when {
        input.length < 2 -> "Please add a valid title"
        input.contains("%") -> "Not this one"
        else -> ""
    }
}
