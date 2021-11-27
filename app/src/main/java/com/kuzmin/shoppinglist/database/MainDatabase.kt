package com.kuzmin.shoppinglist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kuzmin.shoppinglist.entities.LibraryItem
import com.kuzmin.shoppinglist.entities.NoteItem
import com.kuzmin.shoppinglist.entities.ShoppingListItem
import com.kuzmin.shoppinglist.entities.ShoppingListNames

@Database(entities = [LibraryItem::class, NoteItem::class,
    ShoppingListItem::class, ShoppingListNames::class], version = 1)
abstract class MainDatabase : RoomDatabase() {

    abstract fun getDao(): Dao

    companion object{
        @Volatile
        private var INSTANCE : MainDatabase? = null
        fun getDatabase(context: Context): MainDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    MainDatabase::class.java,
                    "shopping_list.db"
                ).build()
                instance
            }
        }
    }


}