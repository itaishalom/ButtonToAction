package com.androrier.buttontoaction.db

import androidx.room.TypeConverter
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converters {
    @TypeConverter
    fun fromString(value: String?): HashSet<Int>? {
        val listType: Type = object : TypeToken<HashSet<Int>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: HashSet<Int>): String? {
        return Gson().toJson(list)
    }
}