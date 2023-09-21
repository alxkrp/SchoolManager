package ru.ak.schoolmanager.db

import androidx.room.TypeConverter
import java.time.LocalDate

class DateConverter {
    @TypeConverter
    fun toLocalDate(dateLong: Long?): LocalDate? {
        return dateLong?.let { LocalDate.ofEpochDay(dateLong) }
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}