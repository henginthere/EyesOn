package com.d201.domain.usecase

import com.d201.domain.repository.ScanObstacleRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScanObstacleUseCase @Inject constructor(
    private val scanObStacleRepository: ScanObstacleRepository
){
}