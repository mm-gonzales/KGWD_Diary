package com.example.kgwd_diary

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MyDiary : AppCompatActivity() {

    private lateinit var diaryRecyclerView: RecyclerView
    private lateinit var diaryAdapter: DiaryAdapter
    private val diaryList = listOf(
        DiaryEntry("First Entry", "Today I started building a diary app."),
        DiaryEntry("Second Entry", "Learning RecyclerView was easier than I thought!"),
        DiaryEntry("Third Entry", "I'll add Firebase soon.")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_diary)

        // Toolbar setup
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.myToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // RecyclerView setup
        diaryRecyclerView = findViewById(R.id.recyclerView)
        diaryRecyclerView.layoutManager = LinearLayoutManager(this)
        diaryAdapter = DiaryAdapter(diaryList)
        diaryRecyclerView.adapter = diaryAdapter

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
                // Handle sign out logic here
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
