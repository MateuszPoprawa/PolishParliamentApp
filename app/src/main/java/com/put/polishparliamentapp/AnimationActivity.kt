package com.put.polishparliamentapp

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class AnimationActivity : AppCompatActivity(), AnimationFragment.Listener {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)

        val fm = supportFragmentManager
        val fragment = createFragment()
        fm.beginTransaction().add(R.id.fragmentContainerAnimation, fragment).commit()
    }

    private fun createFragment() = AnimationFragment.newInstance()

    override fun endOfAnimation() {
        finish()
    }
}