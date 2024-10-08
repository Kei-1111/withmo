package com.example.withmo.ui.screens.side_button

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withmo.domain.usecase.user_settings.side_button.GetSideButtonSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.side_button.SaveSideButtonSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SideButtonSettingsViewModel @Inject constructor(
    private val getSideButtonSettingsUseCase: GetSideButtonSettingsUseCase,
    private val saveSideButtonSettingsUseCase: SaveSideButtonSettingsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SideButtonSettingsUiState())
    val uiState: StateFlow<SideButtonSettingsUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<SideButtonSettingsUiEvent>()
    val uiEvent: SharedFlow<SideButtonSettingsUiEvent> = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            getSideButtonSettingsUseCase().collect { sideButtonSettings ->
                _uiState.update {
                    it.copy(
                        sideButtonSettings = sideButtonSettings,
                        initialSideButtonSettings = sideButtonSettings,
                    )
                }
            }
        }
    }

    fun changeIsScaleSliderButtonShown(isScaleSliderButtonShown: Boolean) {
        _uiState.update {
            it.copy(
                sideButtonSettings = it.sideButtonSettings.copy(
                    isScaleSliderButtonShown = isScaleSliderButtonShown,
                ),
                isSaveButtonEnabled = isScaleSliderButtonShown != it.initialSideButtonSettings.isScaleSliderButtonShown,
            )
        }
    }

    fun changeIsSortButtonShown(isSortButtonShown: Boolean) {
        _uiState.update {
            it.copy(
                sideButtonSettings = it.sideButtonSettings.copy(
                    isSortButtonShown = isSortButtonShown,
                ),
                isSaveButtonEnabled = isSortButtonShown != it.initialSideButtonSettings.isSortButtonShown,
            )
        }
    }

    fun saveSideButtonSettings() {
        _uiState.update {
            it.copy(
                isSaveButtonEnabled = false,
            )
        }
        viewModelScope.launch {
            try {
                saveSideButtonSettingsUseCase(_uiState.value.sideButtonSettings)
                _uiEvent.emit(SideButtonSettingsUiEvent.SaveSuccess)
            } catch (e: Exception) {
                Log.e("SideButtonSettingsViewModel", "Failed to save side button settings", e)
                _uiEvent.emit(SideButtonSettingsUiEvent.SaveFailure)
            }
        }
    }

    fun onEvent(event: SideButtonSettingsUiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}