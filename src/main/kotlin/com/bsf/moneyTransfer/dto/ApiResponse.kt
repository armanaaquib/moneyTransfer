package com.bsf.moneyTransfer.dto

data class ApiResponse<T>(val data: T? = null, val error: ApiError? = null)