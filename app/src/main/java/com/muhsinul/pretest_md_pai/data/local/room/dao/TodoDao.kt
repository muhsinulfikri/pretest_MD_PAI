package com.muhsinul.pretest_md_pai.data.local.room.dao

import androidx.room.*
import com.muhsinul.pretest_md_pai.data.model.Todo

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo WHERE is_task_completed == :isTaskComplete")
    suspend fun getTodoByCompleteness(isTaskComplete : Boolean) : List<Todo>

    @Query("SELECT * FROM todo WHERE id == :id")
    suspend fun getTodoById(id : Int) : Todo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo) : Long

    @Update
    suspend fun updateTodo(todo: Todo) : Int

    @Delete
    suspend fun deleteTodo(todo: Todo) : Int
}