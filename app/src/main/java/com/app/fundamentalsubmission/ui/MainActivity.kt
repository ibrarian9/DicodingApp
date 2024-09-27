package com.app.fundamentalsubmission.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.app.fundamentalsubmission.R
import com.app.fundamentalsubmission.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            loadFragments(HomeFragment())
        }

       bind.apply {
           botNavbar.setOnItemSelectedListener {
               when(it.itemId) {
                   R.id.home -> {
                       loadFragments(HomeFragment())
                       true
                   }
                   R.id.upcoming -> {
                       loadFragments(UpcomingFragment())
                       true
                   }
                   R.id.finished -> {
                       loadFragments(FinishedFragment())
                       true
                   }
                   else -> false
               }
           }

           if (savedInstanceState != null){
               botNavbar.selectedItemId = savedInstanceState.getInt(SELECTED_NAV_ITEM, R.id.home)
           } else {
               botNavbar.selectedItemId = R.id.home
           }
       }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_NAV_ITEM, bind.botNavbar.selectedItemId)
    }

    private fun loadFragments(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        private const val SELECTED_NAV_ITEM = "selected_nav_item"
    }
}