package com.example.kgwd_diary

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FirstScreen : AppCompatActivity() {
    //Declare layout object variables
    lateinit var btnLogin: Button
    lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_first_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Bind the object variable to the view in the layout (view binding)
        btnLogin = findViewById<Button>(R.id.btnLogin)
        //Create an action listener
        btnLogin.setOnClickListener {
            // Declare an intent object
            val intent = Intent(this, MyDiary::class.java)
            startActivity(intent)
        }

        //Bind the object variable to the view in the layout (view binding)
        btnSignUp = findViewById<Button>(R.id.btnSignUp)
        //Create an action listener
        btnSignUp.setOnClickListener {
            // Declare an intent object
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
        }
}