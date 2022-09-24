package com.bsf.moneyTransfer.util

import com.fasterxml.jackson.databind.ObjectMapper

fun asJsonString(obj: Any?): String = ObjectMapper().writeValueAsString(obj)
