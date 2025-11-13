package com.example.dzialajproszelodowka.data.db

import androidx.room.TypeConverter
import java.util.Date


// zamień Date na Long (milisekundy) i tak zapisz w bazie, a przy odczycie zapisz z powrotem na Date
class Converters {

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time;
    }

    // ? - bez tego zmienna nigdy nie mogłaby przyjąć wartości null
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let {Date(it)}; // tworzy nowy obiekt Date
    }
}