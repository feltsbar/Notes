package com.kuzmin.shoppinglist.database

import androidx.room.*
import androidx.room.Dao
import com.kuzmin.shoppinglist.entities.NoteItem
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Insert
    suspend fun insertNote(note : NoteItem)

    @Update
    suspend fun updateNote(note : NoteItem)

    @Query("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)


}