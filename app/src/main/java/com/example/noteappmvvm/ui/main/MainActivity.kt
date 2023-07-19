package com.example.noteappmvvm.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteappmvvm.R
import com.example.noteappmvvm.databinding.ActivityMainBinding
import com.example.noteappmvvm.ui.main.note.NoteAdapter
import com.example.noteappmvvm.ui.main.note.NoteFragment
import com.example.noteappmvvm.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //Binding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    //Others
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var notesAdapter: NoteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

//        InitViews
        binding?.apply {
            //        support toolBar
            setSupportActionBar(notesToolbar)
//            getAllNotes
            viewModel.getAllNotes()
            viewModel.notesData.observe(this@MainActivity) {
                showEmptyList(it.isEmpty)
                notesAdapter.setData(it.data!!)
                noteList.apply {
                    layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    adapter = notesAdapter
                }
            }

            addNoteBtn.setOnClickListener {
                NoteFragment().show(supportFragmentManager, NoteFragment().tag)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showEmptyList(isShown: Boolean) {
        binding?.apply {
            if (isShown) {
                emptyLay.visibility = View.VISIBLE
                noteList.visibility = View.GONE
            } else {
                emptyLay.visibility = View.GONE
                noteList.visibility = View.VISIBLE
            }
        }
    }
}