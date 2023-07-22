package com.example.noteappmvvm.ui.main.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.noteappmvvm.R
import com.example.noteappmvvm.data.model.NoteEntity
import com.example.noteappmvvm.databinding.FragmentNoteBinding
import com.example.noteappmvvm.utils.di.BUNDLE_ID
import com.example.noteappmvvm.utils.di.EDIT
import com.example.noteappmvvm.utils.di.NEW
import com.example.noteappmvvm.viewModel.NoteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import getIndexFromList
import setUpListWithAdapter
import javax.inject.Inject

@AndroidEntryPoint
class NoteFragment : BottomSheetDialogFragment() {
    //    Binding
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding

    //    ViewModel
    private val viewModel: NoteViewModel by viewModels()

    //entity
    @Inject
    lateinit var entity: NoteEntity

    //    Other
    private var category = ""
    private var priority = ""
    private var noteId = 0
    private var type = ""
    private val categoriesList: MutableList<String> = mutableListOf()
    private val prioritiesList: MutableList<String> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Bundle
        noteId = arguments?.getInt(BUNDLE_ID) ?: 0
//        Type
        type = if (noteId > 0) EDIT else NEW
        // initViews
        binding?.apply {
            //Spinners
            //categorySpinner
            viewModel.loadCategoriesData()
            viewModel.categoriesList.observe(viewLifecycleOwner) {
                categoriesSpinner.setUpListWithAdapter(it) { itItem ->
                    categoriesList.addAll(it)
                    category = itItem
                }
            }
            //prioritySpinner
            viewModel.loadPriorityData()
            viewModel.priorityList.observe(viewLifecycleOwner) {
                prioritiesList.addAll(it)
                prioritySpinner.setUpListWithAdapter(it) { itItem ->
                    priority = itItem
                }
            }
//            NoteData
            if (type == EDIT) {
                viewModel.getNote(noteId)
                viewModel.noteData.observe(viewLifecycleOwner) { itData ->
                    itData.data?.let { data ->
                        titleEdt.setText(data.title)
                        descEdt.setText(data.desc)
                        categoriesSpinner.setSelection(categoriesList.getIndexFromList(data.category))
                        prioritySpinner.setSelection(prioritiesList.getIndexFromList(data.priority))
                    }
                }
            }
            saveNote.setOnClickListener {
                var tittle = titleEdt.text.toString()
                var des = descEdt.text.toString()
                entity.id = 0
                entity.title = tittle
                entity.desc = des
                entity.category = category
                entity.priority = priority
                if (tittle.isNotEmpty() && des.isNotEmpty()) {
                    if (type == NEW) {
                        viewModel.saveEditNote(false, entity)
                    } else {
                        viewModel.saveEditNote(true, entity)

                    }
                } else {

                }


                dismiss()

            }
            closeImg.setOnClickListener {
                dismiss()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}