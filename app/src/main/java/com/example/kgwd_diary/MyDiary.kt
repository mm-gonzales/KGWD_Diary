package com.example.kgwd_diary

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyDiary : AppCompatActivity() {

    private lateinit var diaryRecyclerView: RecyclerView
    private lateinit var diaryAdapter: DiaryAdapter
    private val diaryList = mutableListOf<DiaryEntry>()

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_diary)

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Toolbar setup
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.myToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // RecyclerView setup
        diaryRecyclerView = findViewById(R.id.recyclerView)
        diaryRecyclerView.layoutManager = LinearLayoutManager(this)

        // Adapter with both delete and item click
        diaryAdapter = DiaryAdapter(
            diaryList,
            onDeleteClick = { entry ->
                deleteDiaryEntry(entry)
            },
            onItemClick = { entry ->
                val intent = Intent(this, NewEntry::class.java).apply {
                    putExtra("ENTRY_ID", entry.id)
                    putExtra("TITLE", entry.title)
                    putExtra("CONTENT", entry.content)
                }
                startActivity(intent)
            }
        )

        diaryRecyclerView.adapter = diaryAdapter

        // Load Diary Entries
        loadDiaryEntries()

        // Insets handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // FAB setup
        val fabAddEntry = findViewById<FloatingActionButton>(R.id.fabAddEntry)
        fabAddEntry.setOnClickListener {
            val intent = Intent(this, NewEntry::class.java)
            startActivity(intent)
        }
    }

    // Load diary entries from Firestore
    private fun loadDiaryEntries() {
        val userEmail = auth.currentUser?.email

        if (userEmail == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("diaryEntries")
            .whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { result ->
                diaryList.clear()
                for (document in result) {
                    val entry = document.toObject(DiaryEntry::class.java)
                    entry.id = document.id
                    diaryList.add(entry)
                }
                diaryAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load diary entries.", Toast.LENGTH_SHORT).show()
            }
    }

    // Delete entry from Firestore
    private fun deleteDiaryEntry(entry: DiaryEntry) {
        firestore.collection("diaryEntries").document(entry.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Entry deleted.", Toast.LENGTH_SHORT).show()
                loadDiaryEntries()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete entry.", Toast.LENGTH_SHORT).show()
            }
    }

    // Inflate the 3-dot menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Handle menu item clicks
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
