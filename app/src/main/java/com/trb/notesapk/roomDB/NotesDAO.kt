package com.trb.notesapk.roomDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.trb.notesapk.Utility.DATE_TIME

@Dao
interface NotesDAO {

    // Retrieve all notes ordered by the DATE_TIME (descending)
    @Query("SELECT * FROM NOTES_TABLE ORDER BY $DATE_TIME DESC")
    suspend fun getAllNotes(): List<NotesEntity>

    // Insert a new note, aborts if there's a conflict
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notesEntity: NotesEntity)

    // Delete a note by ID
    @Query("DELETE FROM NOTES_TABLE WHERE id = :id")
    suspend fun deleteNotes(id: Int)

    // Update an existing note
    @Update
    suspend fun updateNotes(notesEntity: NotesEntity)
}
