package com.example.noteappmvvm.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappmvvm.data.model.NoteEntity
import com.example.noteappmvvm.data.repository.MainRepository
import com.example.noteappmvvm.data.repository.NoteRepository
import com.example.noteappmvvm.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository):ViewModel() {
  var notesData=MutableLiveData<DataStatus<List<NoteEntity>>>()
  fun getAllNotes()=viewModelScope.launch {
    repository.allNotes().collect{
      notesData.postValue(DataStatus.success(it,it.isEmpty()))
    }
  }

}