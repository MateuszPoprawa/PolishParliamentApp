package com.put.polishparliamentapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

const val SHOW_ANIMATION = "showAnimation"
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private var showAnimation = true
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SHOW_ANIMATION, showAnimation)
    }
}
