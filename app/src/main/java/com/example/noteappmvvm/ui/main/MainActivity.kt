package com.example.noteappmvvm.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.noteappmvvm.R
import com.example.noteappmvvm.databinding.ActivityMainBinding
import com.example.noteappmvvm.ui.main.note.NoteFragment

class MainActivity : AppCompatActivity() {
    //Binding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
//        InitViews
        binding?.apply {
            addNoteBtn.setOnClickListener {
                NoteFragment().show(supportFragmentManager, NoteFragment().tag)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}