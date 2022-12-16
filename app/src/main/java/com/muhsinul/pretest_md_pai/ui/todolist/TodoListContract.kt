package com.muhsinul.pretest_md_pai.ui.todolist

import com.muhsinul.pretest_md_pai.base.BaseContract
import com.muhsinul.pretest_md_pai.data.model.Todo

interface TodoListContract {
    interface View: BaseContract.BaseView{
        fun getData()
        fun onDataSuccess(todos: List<Todo>)
        fun onDataFailed(msg: String?)
        fun onDataEmpty()
        fun onDeleteSuccess()
        fun onDeleteFailed()
        fun setLoading(isLoading: Boolean)
        fun setEmptyStateVisibility(isDataEmpty: Boolean)
        fun initList()
    }
    interface ViewModel{
        fun getTodoComplete(isTodoCompleted: Boolean)
        fun deleteTodo(todo: Todo)
    }
}