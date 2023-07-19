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
import com.example.noteappmvvm.viewModel.NoteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //        initViews

        binding?.apply {
//Spinners
//            categorySpinner
            viewModel.loadCategoriesData()
            viewModel.categoriesList.observe(viewLifecycleOwner) {
                categoriesSpinner.setUpListWithAdapter(it) { itItem ->
                    category = itItem
                }
            }
//            prioritySpinner
            viewModel.loadPriorityData()
            viewModel.priorityList.observe(viewLifecycleOwner) {
                prioritySpinner.setUpListWithAdapter(it) { itItem ->
                    priority = itItem
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
                viewModel.saveEditNote(false, entity)
                dismiss()

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}