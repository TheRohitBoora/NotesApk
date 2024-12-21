package com.trb.notesapk

import android.net.Uri
import android.os.Build.ID
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.trb.notesapk.NotesRoute.DETAIL_NOTES
import com.trb.notesapk.NotesRoute.EDIT_NOTE
import com.trb.notesapk.NotesRoute.QUICK_NOTES
import com.trb.notesapk.Utility.DESCRIPTION
import com.trb.notesapk.Utility.TITLE
import java.net.URLDecoder


@Composable
fun NotesNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = QUICK_NOTES){
        composable(route = QUICK_NOTES) {
            Quicknotes(navController)
        }
        composable(
            route = "$EDIT_NOTE?$TITLE={$TITLE}&$ID={$ID}&$DESCRIPTION={$DESCRIPTION}",
            arguments = listOf(
                navArgument(
                    name = ID
                ){
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(
                    name = TITLE
                ){
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(
                    name = DESCRIPTION
                ){
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ){

            val id = it.arguments?.getInt(ID, -1) ?: -1
            val encodedTitle = it.arguments?.getString(TITLE, "") ?: ""
            val encodedDescription = it.arguments?.getString(DESCRIPTION, "") ?: ""

            val decodedTitle = Uri.decode(encodedTitle)
            val decodedDescription = URLDecoder.decode(encodedDescription, "UTF-8")
            EditNote(navController = navController, id=id, title = decodedTitle, description =decodedDescription)
        }

        composable(DETAIL_NOTES) {
            DetailNotes(navController)
        }
    }
}
object NotesRoute {
    const val QUICK_NOTES = "Quick_Notes"
    const val DETAIL_NOTES = "Detail_Notes"
    const val EDIT_NOTE = "Edit_Note"
}