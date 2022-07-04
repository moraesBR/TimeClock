package com.materdei.timeclock.dto

data class Register(
    val workerName: String,
    val punch: String,
    val year: String,
    val month: String,
    val day: String,
    val hour: String,
    val min: String
)
