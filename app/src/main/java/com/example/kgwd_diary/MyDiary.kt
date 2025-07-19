package com.example.kgwd_diary

import android.content.Intent
import android.os.Bundle
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
        //GUYS ETO MOCK ENTRIES
        DiaryEntry("First Entry", "Today I started building a diary app."),
        DiaryEntry("Second Entry", "Learning RecyclerView was easier than I thought!"),
        DiaryEntry("Third Entry", "I'll add Firebase soon.")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_diary)

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
}
