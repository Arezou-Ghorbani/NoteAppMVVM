package com.example.noteappmvvm.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappmvvm.data.model.NoteEntity
import com.example.noteappmvvm.data.repository.NoteRepository
import com.example.noteappmvvm.utils.DataStatus
import com.example.noteappmvvm.utils.di.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {
    //    Spinners
    val categoriesList = MutableLiveData<MutableList<String>>()
    val priorityList = MutableLiveData<MutableList<String>>()
    var noteData=MutableLiveData<DataStatus<NoteEntity>>()
    fun loadCategoriesData() = viewModelScope.launch(Dispatchers.IO) {
        val data = mutableListOf(WORK, EDUCATION, HOME, HEALTH)
        categoriesList.postValue(data)

    }

    fun loadPriorityData() = viewModelScope.launch(Dispatchers.IO) {
        val data = mutableListOf(HIGH, NORMAL, LOW)
        priorityList.postValue(data)

    }

    //    saveNote
    fun saveEditNote(isEdit: Boolean, noteEntity: NoteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            if (isEdit) {
                repository.update(noteEntity)
            } else
                repository.saveNote(noteEntity)
        }
//    getNote
    fun getNote(id:Int)=
        viewModelScope.launch() {
            repository.getNote(id).collect{
                noteData.postValue(DataStatus.success(it,false))
            }

    }
}