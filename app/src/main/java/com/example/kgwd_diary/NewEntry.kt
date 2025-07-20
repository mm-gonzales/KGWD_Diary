package com.example.kgwd_diary

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
    private lateinit var titleTextView: TextView

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private var entryId: String? = null  // Will hold the document ID if updating

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_entry)

        // Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // UI
        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        btnSaveEntry = findViewById(R.id.btnSaveEntry)
        titleTextView = findViewById(R.id.tvNewEntry)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.myToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Check if we're editing an entry
        entryId = intent.getStringExtra("ENTRY_ID")
        val titleExtra = intent.getStringExtra("TITLE")
        val contentExtra = intent.getStringExtra("CONTENT")

        if (entryId != null) {
            etTitle.setText(titleExtra)
            etContent.setText(contentExtra)
            btnSaveEntry.text = "Update Entry"
            titleTextView.text = "Diary Entry" // Show 'Edit' title
        } else {
            titleTextView.text = "New Entry" // Default title
        }

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

            val entry = hashMapOf(
                "title" to title,
                "content" to content,
                "email" to email
            )

            if (entryId != null) {
                // Update existing entry
                firestore.collection("diaryEntries")
                    .document(entryId!!)
                    .set(entry)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Entry updated!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MyDiary::class.java))
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to update entry", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // Add new entry
                firestore.collection("diaryEntries")
                    .add(entry)
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
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
