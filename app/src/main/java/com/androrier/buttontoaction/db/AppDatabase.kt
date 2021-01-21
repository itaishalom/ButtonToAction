package com.androrier.buttontoaction.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androrier.buttontoaction.model.MyAction

@Database(entities = [MyAction::class], version = 1)
@TypeConverters(Converters::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun myActionDao(): MyActionDao
}