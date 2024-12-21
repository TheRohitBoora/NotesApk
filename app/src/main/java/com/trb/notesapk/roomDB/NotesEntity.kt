package com.trb.notesapk.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.trb.notesapk.Utility.DATE_TIME
import com.trb.notesapk.Utility.DESCRIPTION
import com.trb.notesapk.Utility.NOTES_TABLE
import com.trb.notesapk.Utility.TITLE

@Entity(tableName = NOTES_TABLE)
class NotesEntity (
    @PrimaryKey(autoGenerate = true) val id:Int?,
    @ColumnInfo(name = TITLE) val title:String,
    @ColumnInfo(name = DESCRIPTION) val description: String,
    @ColumnInfo(name = DATE_TIME) val dateTime:Long
)