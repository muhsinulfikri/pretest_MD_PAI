package com.muhsinul.pretest_md_pai.data.local.room.datasource

import com.muhsinul.pretest_md_pai.data.local.room.dao.TodoDao
import com.muhsinul.pretest_md_pai.data.model.Todo

class TodoDataSource(private val todoDao: TodoDao) {

    suspend fun getTodoById(todoID : Int) : Todo {
        return todoDao.getTodoById(todoID)
    }

    suspend fun addTodo(todo: Todo): Long {
        return todoDao.insertTodo(todo)
    }

    suspend fun deleteTodo(todo: Todo) : Int {
        return todoDao.deleteTodo(todo)
    }

    suspend fun updateTodo(todo: Todo): Int {
        return todoDao.updateTodo(todo)
    }

    suspend fun getTodoByCompleteness(isTaskCompleted: Boolean): List<Todo> {
        return todoDao.getTodoByCompleteness(isTaskCompleted)
    }


}