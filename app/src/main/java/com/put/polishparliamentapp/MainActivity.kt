package com.put.polishparliamentapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.NavHostFragment

const val SHOW_ANIMATION = "showAnimation"
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private var showAnimation = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            showAnimation = it.getBoolean(SHOW_ANIMATION)
        }

        if (showAnimation) {
            val intent = Intent(this, AnimationActivity::class.java)
            startActivity(intent)
            showAnimation = false
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SHOW_ANIMATION, showAnimation)
    }
}
