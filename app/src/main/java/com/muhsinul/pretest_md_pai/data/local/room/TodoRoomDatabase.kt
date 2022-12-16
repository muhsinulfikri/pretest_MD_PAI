package com.muhsinul.pretest_md_pai.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.muhsinul.pretest_md_pai.data.local.room.dao.TodoDao
import com.muhsinul.pretest_md_pai.data.model.Todo

@Database(entities = [Todo::class], version = 1)
abstract class TodoRoomDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: TodoRoomDatabase? = null
        fun getInstance(context: Context): TodoRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoRoomDatabase::class.java,
                    "todo_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

    }
}