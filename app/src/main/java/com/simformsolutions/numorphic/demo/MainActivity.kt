package com.simformsolutions.numorphic.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.simformsolutions.numorphic.component.NumorphImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val v = findViewById<NumorphImageButton>(R.id.imageButton)
        Glide.with(v.context)
            .load(R.drawable.profile)
            .circleCrop()
            .into(v)
    }
}