package com.example.project.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.project.R


fun ImageView.loadImageFromURL(url: String){
    Glide.with(this).load(url).placeholder(R.drawable.round_button)
        .error(R.drawable.round_button).into(this)
}