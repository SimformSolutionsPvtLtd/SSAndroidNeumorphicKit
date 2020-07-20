package com.simformsolutions.numorphic.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.simformsolutions.numorphic.annotation.CornerFamily
import com.simformsolutions.numorphic.component.NumorphImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val v = findViewById<NumorphImageView>(R.id.imageButton)

        v.setCornerFamily(CornerFamily.OVAL)
        v.setCorner(0f)

        Glide.with(v.context)
            .load(R.drawable.profile)
            .circleCrop()
            .into(v)
    }
}