package com.muhsinul.pretest_md_pai.ui.todolist

import com.muhsinul.pretest_md_pai.data.local.room.datasource.TodoDataSource
import com.muhsinul.pretest_md_pai.data.model.Todo

class TodoListRepository(private val dataSource: TodoDataSource) {
    suspend fun getTodoComplete(isTodoCompleted: Boolean): List<Todo>{
        return dataSource.getTodoByCompleteness(isTodoCompleted)
    }
    suspend fun deleteTodo(todo: Todo): Int {
        return dataSource.deleteTodo(todo)
    }
}