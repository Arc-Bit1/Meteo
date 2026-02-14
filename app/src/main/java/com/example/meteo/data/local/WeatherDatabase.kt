package com.example.meteo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WeatherCacheEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
