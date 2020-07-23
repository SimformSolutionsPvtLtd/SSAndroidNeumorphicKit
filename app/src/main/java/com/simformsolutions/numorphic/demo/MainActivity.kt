package com.simformsolutions.numorphic.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.simformsolutions.numorphic.component.NumorphImageView

class MainActivity : AppCompatActivity() {

    private lateinit var v: NumorphImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        v = findViewById(R.id.imageButton)

        loadImage()
    }

    private fun loadImage() {
        Glide.with(applicationContext)
            .asBitmap()
            .load("https://picsum.photos/id/237/400")
            .apply(RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
            )
            .into(v)
    }
}