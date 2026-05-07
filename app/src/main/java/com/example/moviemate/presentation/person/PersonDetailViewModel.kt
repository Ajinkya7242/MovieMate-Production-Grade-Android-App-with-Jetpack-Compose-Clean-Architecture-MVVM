package com.example.moviemate.presentation.person

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviemate.core.navigation.Screen
import com.example.moviemate.core.utils.Resource
import com.example.moviemate.domain.model.PersonDetail
import com.example.moviemate.domain.model.PersonMovieCredit
import com.example.moviemate.domain.usecase.details.GetPersonDetailsUseCase
import com.example.moviemate.domain.usecase.details.GetPersonMovieCreditsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PersonDetailUiState(
    val person: Resource<PersonDetail> = Resource.Loading,
    val credits: Resource<List<PersonMovieCredit>> = Resource.Loading
)

@HiltViewModel
class PersonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPersonDetails: GetPersonDetailsUseCase,
    private val getPersonCredits: GetPersonMovieCreditsUseCase
) : ViewModel() {

    private val personId: Int = checkNotNull(savedStateHandle[Screen.PersonDetail.ARG_PERSON_ID])

    private val _uiState = MutableStateFlow(PersonDetailUiState())
    val uiState: StateFlow<PersonDetailUiState> = _uiState.asStateFlow()

    init { loadAll() }

    fun retry() = loadAll()

    private fun loadAll() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(person = Resource.Loading, credits = Resource.Loading)
            }
            coroutineScope {
                async {
                    runCatching { getPersonDetails(personId) }
                        .onSuccess { _uiState.update { s -> s.copy(person = Resource.Success(it)) } }
                        .onFailure {
                            _uiState.update { s ->
                                s.copy(person = Resource.Error(it.message ?: "Failed", it))
                            }
                        }
                }
                async {
                    runCatching { getPersonCredits(personId) }
                        .onSuccess { _uiState.update { s -> s.copy(credits = Resource.Success(it)) } }
                        .onFailure {
                            _uiState.update { s ->
                                s.copy(credits = Resource.Error(it.message ?: "Failed", it))
                            }
                        }
                }
            }
        }
    }
}
