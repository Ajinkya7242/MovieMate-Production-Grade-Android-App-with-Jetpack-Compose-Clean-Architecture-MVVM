package com.example.moviemate.presentation.genres

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviemate.core.utils.Resource
import com.example.moviemate.domain.model.Genre
import com.example.moviemate.domain.usecase.movies.GetGenresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenresViewModel @Inject constructor(
    private val getGenres: GetGenresUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<Resource<List<Genre>>>(Resource.Loading)
    val state: StateFlow<Resource<List<Genre>>> = _state.asStateFlow()

    init { load() }

    fun retry() = load()

    private fun load() {
        viewModelScope.launch {
            _state.value = Resource.Loading
            try {
                _state.value = Resource.Success(getGenres())
            } catch (e: Exception) {
                _state.value = Resource.Error(e.message ?: "Failed to load genres", e)
            }
        }
    }
}
