package com.trb.notesapk

import android.content.SharedPreferences
import android.os.Build.ID
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Light
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.trb.notesapk.NotesRoute.EDIT_NOTE
import com.trb.notesapk.ui.theme.BananaMania
import com.trb.notesapk.ui.theme.BlackMetal
import com.trb.notesapk.ui.theme.MintChip
import com.trb.notesapk.ui.theme.QuickFreeze
import com.trb.notesapk.ui.theme.Salmon
import com.trb.notesapk.ui.theme.Sunshine
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.trb.notesapk.SharedPreferencesHelper.PROFILE_NAME
import com.trb.notesapk.Utility.DESCRIPTION
import com.trb.notesapk.Utility.TITLE
import com.trb.notesapk.roomDB.NotesDatabase
import com.trb.notesapk.roomDB.NotesRepo
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Quicknotes(navController: NavController) {

    val notesType = arrayOf("all(20)", "Important", "Bookmark", "Links")
    var userName by remember { mutableStateOf("") }
    var isProfileVisible by remember { mutableStateOf(false) }
    val sharedPrefInstance =
        SharedPreferencesHelper.instanceSharedPref(context = LocalContext.current)
    val notesDatabase = NotesDatabase.getDatabase(LocalContext.current)
    val notesRepo= NotesRepo(notesDatabase.notesDao())
    val notesList by notesRepo.notesList.collectAsState()

    Scaffold(
        Modifier.background(BlackMetal),
        floatingActionButton = {
            FloatingActionButton(modifier = Modifier.size(40.dp),
                onClick = {
                    navController.navigate(EDIT_NOTE)
                }
            )
            {
                Icon(
                    Icons.Default.Add, "New Note Button",
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = QuickFreeze)
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.man),
                            contentDescription = "Profile Icon",
                            Modifier.size(32.dp),
                        )
                        Text(
                            "Hi! $userName ",
                            fontSize = 24.sp,
                            fontWeight = Light,
                            modifier = Modifier.padding(start = 8.dp, top = 2.dp),
                            color = Color.White,

                            )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(BlackMetal),
                actions = {
                    IconButton(
                        onClick = {
                            isProfileVisible = true
                        }
                    ) {
                        Icon(Icons.Default.Person, "My Id", tint = Color.White)
                    }
                }
            )
        },
        containerColor = BlackMetal
    ) { innerPadding ->
        notesRepo.getAllNotes()
        userName = SharedPreferencesHelper.getStringPref(PROFILE_NAME, sharedPrefInstance)
        if (isProfileVisible) {
            ProfileDialog(
                onDismiss = {
                    isProfileVisible = false

                },
                sharedPreferences = sharedPrefInstance
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .background(BlackMetal)
                .fillMaxHeight()
        ) {
            Spacer(
                Modifier
                    .height(56.dp)
                    .padding(innerPadding)
            )

            Text(
                text = "My Notes",
                fontWeight = Light,
                fontSize = 44.sp,
                modifier = Modifier.padding(start = 4.dp, top = 38.dp),
                color = Color.White
            )
            Spacer(Modifier.height(12.dp))
            LazyRow {
                items(notesType) { list ->
                    Card(
                        Modifier.padding(end = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = list,
                            Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            LazyColumn {
                itemsIndexed(notesList) { index, list ->

                    Card(
                        modifier = Modifier
                            .padding(bottom = 14.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor =
                            when (index % 4) {
                                0 -> Sunshine
                                1 -> MintChip
                                2 -> Salmon
                                3 -> BananaMania
                                else -> Salmon
                            }
                        ),
                        onClick = {

                            val encodedTitle = URLEncoder.encode(list.title, "UTF-8")
                            val encodedDescription = URLEncoder.encode(list.description, "UTF-8")
                            navController.navigate (route = "${NotesRoute.EDIT_NOTE}?${TITLE}=${encodedTitle}&${ID}=${list.id}&${DESCRIPTION}=${encodedDescription}")

                        }

                    ) {
                        Text(
                            text = list.title,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp),
                            color = Color.DarkGray
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = list.description,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 12.dp, start = 12.dp, end = 12.dp),
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileDialog(onDismiss: () -> Unit, sharedPreferences: SharedPreferences) {
    var alertDialogTitle by remember { mutableStateOf(TextFieldValue("")) }
    AlertDialog(
        title = {
            Text(
                text = "Update Your Name"
            )
        },

        text = {
            OutlinedTextField(
                value = alertDialogTitle,
                onValueChange = {
                    alertDialogTitle = it
                },
                label = {
                    Text(
                        text = "Profile Name"
                    )
                },
                placeholder = {
                    Text(
                        text = "Profile Name"
                    )
                },
                textStyle = TextStyle.Default.copy(
                    fontSize = 24.sp, fontWeight = Light
                ),
                maxLines = 2,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    SharedPreferencesHelper.saveStringPref(
                        PROFILE_NAME,
                        alertDialogTitle.text,
                        sharedPreferences
                    )
                    onDismiss()
                }
            ) {
                Text(text = "update")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(text = "No")
            }
        },
        onDismissRequest = {
            onDismiss()
        }
    )
}

@Preview
@Composable
fun Prev() {
    Quicknotes(rememberNavController())
}