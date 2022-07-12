package com.materdei.timeclock.dto

data class SingleRegister(
    var date: String,
    var workedHour: String,
    var punches: MutableList<RegisterDetails>)
