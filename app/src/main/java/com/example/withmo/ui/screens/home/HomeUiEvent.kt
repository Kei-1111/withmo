package com.example.withmo.ui.screens.home

import android.appwidget.AppWidgetProviderInfo
import com.example.withmo.ui.base.UiEvent

sealed interface HomeUiEvent : UiEvent {
    data class SetShowScaleSlider(val isShow: Boolean) : HomeUiEvent
    data class SetPopupExpanded(val isExpand: Boolean) : HomeUiEvent
    data class OnValueChangeAppSearchQuery(val query: String) : HomeUiEvent
    data object OnSelectSortByUsageOrder : HomeUiEvent
    data object OnSelectSortByAlphabeticalOrder : HomeUiEvent
    data object OpenAppListBottomSheet : HomeUiEvent
    data object HideAppListBottomSheet : HomeUiEvent
    data object OpenActionSelectionBottomSheet : HomeUiEvent
    data object HideActionSelectionBottomSheet : HomeUiEvent
    data object OpenWidgetListBottomSheet : HomeUiEvent
    data object HideWidgetListBottomSheet : HomeUiEvent
    data class OnSelectWidget(val widgetInfo: AppWidgetProviderInfo) : HomeUiEvent
    data object EnterEditMode : HomeUiEvent
    data object ExitEditMode : HomeUiEvent
    data object NavigateToSettingsScreen : HomeUiEvent
}
