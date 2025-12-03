package com.example.dzialajproszelodowka.data.db

import androidx.room.TypeConverter
import java.util.Date


class Converters {

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time;
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let {Date(it)};
    }
}