package com.trb.notesapk

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.trb.notesapk.NotesRoute.QUICK_NOTES
import com.trb.notesapk.ui.theme.MintChip
import com.trb.notesapk.ui.theme.QuickFreeze

@Composable
fun DetailNotes(navController: NavController) {




    Box(
        Modifier.background(MintChip)
    ) {
        Column(
            Modifier.padding(horizontal = 12.dp).fillMaxHeight()
        ) {
            Spacer(Modifier.height(56.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.background(QuickFreeze, CircleShape).size(44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Rounded.KeyboardArrowLeft, "BackArrow",
                        modifier = Modifier.size(44.dp).clip(CircleShape)
                            .clickable {
                        navController.popBackStack()

                    })

                }

                Box(
                    modifier = Modifier.background(QuickFreeze, CircleShape).size(44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Rounded.List, "Menu", Modifier.size(36.dp))
                }
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Hello, I'm Rohit",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(24.dp))

            Text(
                text = "It is a long established fact that a reader will be distracted by the readable content.",
                fontSize = 20.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}


@Composable
@Preview
fun Review (){
    DetailNotes(rememberNavController())
}
