package com.simformsolutions.numorphic.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.simformsolutions.numorphic.component.NumorphImageButton
import com.simformsolutions.numorphic.component.NumorphImageView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageButton = findViewById<NumorphImageButton>(R.id.imageButton)
        imageButton.loadImage()

        val imageView = findViewById<NumorphImageView>(R.id.imageView)
        imageView.loadImage()
    }

    private fun ImageView.loadImage() {
        Glide.with(applicationContext)
            .asBitmap()
            .load("https://picsum.photos/id/237/400")
            .apply(RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
            )
            .into(this)
    }
}