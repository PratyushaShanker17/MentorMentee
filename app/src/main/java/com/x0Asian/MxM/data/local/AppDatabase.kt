package com.x0Asian.MxM.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.x0Asian.MxM.data.converters.DateConverter
import com.x0Asian.MxM.data.local.dao.ChatMessageDao
import com.x0Asian.MxM.data.local.dao.UserProfileDao
import com.x0Asian.MxM.data.models.ChatMessage
import com.x0Asian.MxM.data.models.UserProfile

@Database(entities = [UserProfile::class, ChatMessage::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao
    abstract fun chatMessageDao(): ChatMessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mentor_mentee_database"
                )
                // Add migrations here if/when you increment the version number
                .fallbackToDestructiveMigration() // Not recommended for production, use proper migrations
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
