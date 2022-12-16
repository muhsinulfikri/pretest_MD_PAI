package com.muhsinul.pretest_md_pai.ui.detail

import com.muhsinul.pretest_md_pai.data.local.room.datasource.TodoDataSource
import com.muhsinul.pretest_md_pai.data.model.Todo

class DetailTodoRepository(private val dataSource: TodoDataSource) {
    suspend fun getTodoId(todoId: Int): Todo{
        return dataSource.getTodoById(todoId)
    }
    suspend fun changeTodoStatus(todo: Todo): Todo {
        val updatedTodo = todo.copy().apply {
            this.isTaskCompleted = isTaskCompleted.not()
        }
        dataSource.updateTodo(updatedTodo)
        return getTodoId(todo.id)
    }
}