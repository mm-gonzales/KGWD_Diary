package com.example.kgwd_diary

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NewEntry : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var btnSaveEntry: Button

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_entry)

        // Firebase setup
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // UI components
        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        btnSaveEntry = findViewById(R.id.btnSaveEntry)

        // ✅ Set up the toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.myToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Save Entry logic
        btnSaveEntry.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val content = etContent.text.toString().trim()
            val email = auth.currentUser?.email

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newEntry = hashMapOf(
                "title" to title,
                "content" to content,
                "email" to email
            )

            firestore.collection("diaryEntries")
                .add(newEntry)
                .addOnSuccessListener {
                    Toast.makeText(this, "Entry saved!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MyDiary::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save entry", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Back button action
                onBackPressedDispatcher.onBackPressed()
                true
            }
            R.id.action_sign_out -> {
                startActivity(Intent(this, FirstScreen::class.java))
                finish()
                true
            }
            R.id.action_about_us -> {
                startActivity(Intent(this, AboutUs::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
