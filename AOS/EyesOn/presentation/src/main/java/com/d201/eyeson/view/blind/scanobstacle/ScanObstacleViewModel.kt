package com.d201.eyeson.view.blind.scanobstacle

import androidx.lifecycle.ViewModel
import com.d201.domain.usecase.ScanObstacleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private const val TAG ="ScanObstacleViewModel__"
@HiltViewModel
class ScanObstacleViewModel @Inject constructor(): ViewModel(){

    private val _centerX: MutableStateFlow<Float> = MutableStateFlow(0.0f)
    val centerX get() = _centerX.asStateFlow()

    private val _centerY: MutableStateFlow<Float> = MutableStateFlow(0.0f)
    val centerY get() = _centerY.asStateFlow()

    fun getBoxCenter(x:Float, y:Float){
        _centerX.value = x
        _centerY.value = y
    }
}
