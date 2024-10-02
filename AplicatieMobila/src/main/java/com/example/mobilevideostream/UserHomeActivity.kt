package com.example.mobilevideostream

import android.os.Bundle
import androidx.activity.ComponentActivity

class UserHomeActivity:ComponentActivity() {  //mosteneste clasa Component Activity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // suprascrie metoda onCreate
        setContentView(R.layout.activity_userhome)
        var userID: Int?=null
    }
}