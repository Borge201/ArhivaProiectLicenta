package com.example.mobilevideostream

import android.os.Bundle
import androidx.activity.ComponentActivity

class UserForgPassActivity:ComponentActivity() {  //mosteneste clasa Component Activity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // suprascrie metoda onCreate
        setContentView(R.layout.activity_userforgpass)
    }
}