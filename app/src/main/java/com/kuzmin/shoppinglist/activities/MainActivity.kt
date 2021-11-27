package com.kuzmin.shoppinglist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.kuzmin.shoppinglist.R
import com.kuzmin.shoppinglist.databinding.ActivityMainBinding
import com.kuzmin.shoppinglist.fragments.FragmentManager
import com.kuzmin.shoppinglist.fragments.NoteFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setBottomNavListener()
        FragmentManager.setFragment(NoteFragment.newInstance(), this)

    }

    // Заполняем меню с помощью созданной разметки
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.new_item -> {
                FragmentManager.currentFrag?.onClickNew()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}