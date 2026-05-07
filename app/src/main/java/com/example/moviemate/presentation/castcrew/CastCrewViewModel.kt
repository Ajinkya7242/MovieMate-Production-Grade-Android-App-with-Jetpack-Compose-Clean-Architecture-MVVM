package com.example.moviemate.presentation.castcrew

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviemate.core.navigation.Screen
import com.example.moviemate.core.utils.Resource
import com.example.moviemate.domain.model.MovieCredits
import com.example.moviemate.domain.usecase.details.GetMovieCreditsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CastCrewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMovieCredits: GetMovieCreditsUseCase
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle[Screen.CastCrew.ARG_MOVIE_ID])

    private val _state = MutableStateFlow<Resource<MovieCredits>>(Resource.Loading)
    val state: StateFlow<Resource<MovieCredits>> = _state.asStateFlow()

    init { load() }

    fun retry() = load()

    private fun load() {
        viewModelScope.launch {
            _state.value = Resource.Loading
            try {
                _state.value = Resource.Success(getMovieCredits(movieId))
            } catch (e: Exception) {
                _state.value = Resource.Error(e.message ?: "Failed to load credits", e)
            }
        }
    }
}
