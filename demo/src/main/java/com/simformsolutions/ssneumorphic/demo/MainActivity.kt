package com.simformsolutions.ssneumorphic.demo

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.simformsolutions.ssneumorphic.component.SSNeumorphicImageButton
import com.simformsolutions.ssneumorphic.component.SSNeumorphicImageView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageButton = findViewById<SSNeumorphicImageButton>(R.id.imageButton)
        imageButton.loadImage()

        val imageView = findViewById<SSNeumorphicImageView>(R.id.imageView)
        imageView.loadImage()
    }

    private fun ImageView.loadImage() {
        Glide.with(applicationContext)
            .asBitmap()
            .load("https://picsum.photos/id/237/400")
            .into(this)
    }
}