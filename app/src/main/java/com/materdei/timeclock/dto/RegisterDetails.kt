package com.materdei.timeclock.dto

data class RegisterDetails(
    var date: String,
    var time: String,
    var punch: String
){
    constructor() : this("","","")
    fun getKey() = "$date@$time"
}

/*
{
    fun workedTimeUpdate(){
        if (PunchCard.valueOf(list.last().punch) == PunchCard.OUT) {
            val beforelast = list.lastIndex - 1
            val last = list.lastIndex
            if (PunchCard.valueOf(list[beforelast].punch) == PunchCard.OUT ){
                val start = list[beforelast].hour.toInt()*60 + list[beforelast].min.toInt()
                val end = list[last].hour.toInt()*60 + list[last].min.toInt()
                val duration =  end - start + workedMin
                workedHour+= duration/60
                workedMin  = duration%60
            }
        }
    }
}*/
