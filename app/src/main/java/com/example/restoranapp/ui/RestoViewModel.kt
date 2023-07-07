package com.example.restoranapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.restoranapp.model.Resto
import com.example.restoranapp.repository.RestoRepository
import kotlinx.coroutines.launch
import kotlin.IllegalArgumentException

class RestoViewModel(private val repository: RestoRepository): ViewModel() {
    val allRestos: LiveData<List<Resto>> = repository.allRestos.asLiveData()

    fun insert(resto: Resto) = viewModelScope.launch {
        repository.insertResto(resto)
    }
    fun delete(resto: Resto) = viewModelScope.launch {
        repository.deleteResto(resto)
    }
    fun update(resto: Resto) = viewModelScope.launch {
        repository.updateResto(resto)
    }
}

class RestoViewModelFactory(private val repository: RestoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((RestoViewModel::class.java))){
            return RestoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}