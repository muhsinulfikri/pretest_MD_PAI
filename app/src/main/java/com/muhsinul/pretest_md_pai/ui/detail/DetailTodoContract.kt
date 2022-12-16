package com.muhsinul.pretest_md_pai.ui.detail

import com.muhsinul.pretest_md_pai.base.BaseContract
import com.muhsinul.pretest_md_pai.data.model.Todo

interface DetailTodoContract {
    interface View: BaseContract.BaseView {
        fun onFetchDetailSuccess(todo: Todo)
        fun onFetchDetailFailed()
        fun onChangeTodoStatusSuccess(todo: Todo)
        fun onChangeTodoStatusFailed()
        fun bindTodoData(todo: Todo)
        fun getData()
    }
    interface ViewModel {
        fun getDetail(todoId: Int)
        fun changeStatusTodo(todo: Todo)
    }
}