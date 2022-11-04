package com.d201.domain.usecase.help

import com.d201.domain.repository.HelpRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResponseHelpUseCase @Inject constructor(
    private val helpRepository: HelpRepository
) {
    fun execute() = helpRepository.responseHelp()
}