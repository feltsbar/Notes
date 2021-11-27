package com.kuzmin.shoppinglist.fragments

import androidx.appcompat.app.AppCompatActivity
import com.kuzmin.shoppinglist.R

object FragmentManager {
    var currentFrag : BaseFragment? = null

    fun setFragment(newFrag: BaseFragment, activity: AppCompatActivity){
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.place_holder, newFrag)
        transaction.commit()
        currentFrag = newFrag
    }
}