package com.kuzmin.shoppinglist.activities

import android.app.Application
import com.kuzmin.shoppinglist.database.MainDatabase

class MainApp: Application() {
    val database by lazy { MainDatabase.getDatabase(this) }
}
