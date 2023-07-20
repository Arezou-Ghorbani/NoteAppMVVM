package com.example.noteappmvvm.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteappmvvm.R
import com.example.noteappmvvm.data.model.NoteEntity
import com.example.noteappmvvm.databinding.ActivityMainBinding
import com.example.noteappmvvm.ui.main.note.NoteAdapter
import com.example.noteappmvvm.ui.main.note.NoteFragment
import com.example.noteappmvvm.utils.di.*
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
    private var selectedItem = 0


    @Inject
    lateinit var noteEntity: NoteEntity
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
//            Filter
            notesToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.actionFilter -> {
                        priorityFilter()
                        return@setOnMenuItemClickListener true
                    }
                    else -> {
                        return@setOnMenuItemClickListener false
                    }
                }
            }
            //Clicks
            notesAdapter.setOnItemClickListener { entity, type ->
                when (type) {
                    EDIT -> {
                        val noteFragment = NoteFragment()
                        val bundle = Bundle()
                        bundle.putInt(BUNDLE_ID, entity.id)
                        noteFragment.arguments = bundle
                        noteFragment.show(supportFragmentManager, NoteFragment().tag)
                    }
                    DELETE -> {
                        noteEntity.id = entity.id
                        noteEntity.title = entity.title
                        noteEntity.desc = entity.desc
                        noteEntity.category = entity.category
                        noteEntity.priority = entity.priority
                        viewModel.deleteNote(noteEntity)
                    }
                }
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
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        val search = menu.findItem(R.id.actionSearch)
        val searchView = search.actionView as SearchView
        searchView.queryHint = getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.getSearchNotes(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun priorityFilter() {
        val builder = AlertDialog.Builder(this)

        val priority = arrayOf("All", HIGH, NORMAL, LOW)
        builder.setSingleChoiceItems(priority, selectedItem) { dialog, item ->
            when (item) {
                0 -> {
                    viewModel.getAllNotes()
                }
                in 1..3 -> {
                    viewModel.getFilterNotes(priority[item])
                }
            }
            selectedItem = item
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    jalase akhar 208
}