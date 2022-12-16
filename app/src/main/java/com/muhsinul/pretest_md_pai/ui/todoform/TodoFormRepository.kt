package com.muhsinul.pretest_md_pai.ui.todoform

import com.muhsinul.pretest_md_pai.data.local.room.datasource.TodoDataSource
import com.muhsinul.pretest_md_pai.data.model.Todo

class TodoFormRepository(private val dataSource: TodoDataSource) {
    suspend fun addTodo(todo: Todo): Long {
        return dataSource.addTodo(todo)
    }

    suspend fun updateTodo(todo: Todo): Int {
        return dataSource.updateTodo(todo)
    }
}