package com.materdei.timeclock.dto

import android.util.Log
import com.materdei.timeclock.utils.PunchCard
import java.text.DecimalFormat

class Registers {

    var registers: HashMap<String,SingleRegister> = hashMapOf()

    fun toList(): List<SingleRegister> =
        registers.map { it.value }.sortedByDescending { it.date }

    fun update(data: List<RegisterDetails>){
        data.asSequence()
            .groupBy { it.date }
            .map {
                registers[it.key] =
                    SingleRegister(
                        it.key,
                        workedTime(it.value),
                        it.value.toMutableList()
                    )
            }
    }

    private fun checkPunches(data: List<RegisterDetails>, action: (before: Int,current: Int) -> Unit):Boolean{
        if (data.size%2 != 0 || data.size == 0 ) return false
        var result = true

        val lastIndex = data.lastIndex

        data.forEachIndexed { index, data ->
            if((index % 2 == 0 && PunchCard.OUT.value.equals(data.punch)) ||
               (index % 2 == 1 && PunchCard.IN.value.equals(data.punch))) {
                result = false
            }
            if(index <= lastIndex && index % 2 == 1)
                action(index - 1, index)
        }

        return result
    }

    private fun workedTime(data: List<RegisterDetails>):String{
        var minutes = 0
        val isCorrect = checkPunches(data){ before, current ->
            minutes += workedMinutes(data[before].time,data[current].time)
        }

        return if (isCorrect) Int2String(minutes) else "ERROR"
    }

    /*private fun workedTime(list: List<RegisterDetails>): String {
        var current = list[0]
        lateinit var before: RegisterDetails
        var minutes = 0
        list.forEachIndexed { index, data ->
            if (index > 0){

                before = current
                current = data

                if(PunchCard.IN == PunchCard.valueOf(before.punch.uppercase())
                    && PunchCard.OUT == PunchCard.valueOf(current.punch.uppercase()))
                    minutes += workedMinutes(before.time,current.time)
            }
        }
        return Int2String(minutes)
    }*/

    private fun workedMinutes(start: String, end: String): Int {
        val mStart = start.substringBefore("-").toInt().times(60) +
                     start.substringAfter("-").toInt()

        val mEnd = end.substringBefore("-").toInt().times(60) +
                   end.substringAfter("-").toInt()

        return (mEnd - mStart)
    }

    private fun Int2String(time: Int):String {
        val hour = DecimalFormat("00").format(time/60)
        val minutes = DecimalFormat("00").format(time%60)
        Log.i("TIME","$time   $hour   $minutes")
        return "$hour:$minutes"
    }
}