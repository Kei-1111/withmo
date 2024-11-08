package com.example.withmo.ui.screens.home

import android.content.Context
import android.view.View
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.model.WidgetInfo
import com.example.withmo.domain.model.user_settings.AppIconSettings
import com.example.withmo.domain.model.user_settings.toShape
import com.example.withmo.ui.component.AppItem
import com.example.withmo.ui.component.WithmoClock
import com.example.withmo.ui.component.WithmoIconButton
import com.example.withmo.ui.theme.UiConfig
import com.unity3d.player.UnityPlayer.UnitySendMessage
import kotlinx.collections.immutable.ImmutableList

@Suppress("LongMethod")
@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    createWidgetView: (Context, WidgetInfo, Int, Int) -> View,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        if (uiState.isShowScaleSlider) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                WithmoIconButton(
                    onClick = {
                        UnitySendMessage("Slidermaneger", "HideSlider", "")
                        onEvent(HomeUiEvent.SetShowScaleSlider(false))
                    },
                    icon = Icons.Default.Close,
                    modifier = Modifier.padding(start = UiConfig.MediumPadding),
                )
            }
        } else {
            if (uiState.currentUserSettings.clockSettings.isClockShown) {
                WithmoClock(
                    clockType = uiState.currentUserSettings.clockSettings.clockType,
                    dateTimeInfo = uiState.currentTime,
                    modifier = Modifier.padding(start = UiConfig.MediumPadding),
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { change, dragAmount ->
                                if (dragAmount < UiConfig.BottomSheetShowDragHeight) {
                                    onEvent(HomeUiEvent.OpenAppListBottomSheet)
                                }
                            },
                        )
                    },
            ) {
                PagerContent(
                    isScaleSliderButtonShown = uiState.currentUserSettings.sideButtonSettings.isScaleSliderButtonShown,
                    showScaleSlider = {
                        UnitySendMessage("Slidermaneger", "ShowSlider", "")
                        onEvent(HomeUiEvent.SetShowScaleSlider(true))
                    },
                    openActionSelectionBottomSheet = {
                        onEvent(HomeUiEvent.OpenActionSelectionBottomSheet)
                    },
                    displayedWidgetList = uiState.displayedWidgetList,
                    createWidgetView = createWidgetView,
                    appIconSize = uiState.currentUserSettings.appIconSettings.appIconSize,
                    isEditMode = uiState.isEditMode,
                    exitEditMode = {
                        onEvent(HomeUiEvent.ExitEditMode)
                    },
                    deleteWidget = { widgetInfo ->
                        onEvent(HomeUiEvent.DeleteWidget(widgetInfo))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(UiConfig.DefaultWeight),
                )
                RowAppList(
                    startApp = { onEvent(HomeUiEvent.StartApp(it)) },
                    deleteApp = { onEvent(HomeUiEvent.DeleteApp(it)) },
                    favoriteAppList = uiState.favoriteAppList,
                    appIconSettings = uiState.currentUserSettings.appIconSettings,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun RowAppList(
    startApp: (AppInfo) -> Unit,
    deleteApp: (AppInfo) -> Unit,
    favoriteAppList: ImmutableList<AppInfo>,
    appIconSettings: AppIconSettings,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(vertical = UiConfig.ExtraSmallPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        favoriteAppList.forEach {
            AppItem(
                onClick = { startApp(it) },
                onLongClick = { deleteApp(it) },
                appInfo = it,
                appIconSize = appIconSettings.appIconSize,
                appIconShape = appIconSettings.appIconShape.toShape(
                    roundedCornerPercent = appIconSettings.roundedCornerPercent,
                ),
                isAppNameShown = appIconSettings.isAppNameShown,
                modifier = Modifier.weight(UiConfig.DefaultWeight),
            )
        }
    }
}
