package com.example.kgwd_diary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kgwd_diary.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth

class LogIn : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_log_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Bind UI elements
        usernameField = findViewById(R.id.edLoginUsername)
        passwordField = findViewById(R.id.edLogInPassword)
        loginButton = findViewById(R.id.btnLogin1)
        backButton = findViewById(R.id.btnBackLogin)

        // Login button clicked
        loginButton.setOnClickListener {
            val email = usernameField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            } else {
                logInUser(email, password)
            }
        }

        // Back button clicked
        backButton.setOnClickListener {
            finish() // go back to previous screen
        }
    }

    private fun logInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Success: navigate to diary or home screen
                    val user = auth.currentUser
                    Toast.makeText(this, "Welcome, ${user?.email}", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, MyDiary::class.java))
                    finish()
                } else {
                    // Failure
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    Log.w("LogInActivity", "signInWithEmail:failure", task.exception)
                }
            }
    }
}