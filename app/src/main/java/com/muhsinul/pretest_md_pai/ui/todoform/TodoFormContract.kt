package com.muhsinul.pretest_md_pai.ui.todoform

import com.muhsinul.pretest_md_pai.base.BaseContract
import com.muhsinul.pretest_md_pai.data.model.Todo

interface TodoFormContract {
    interface ViewModel {
        fun insertTodo(todo: Todo)
        fun updateTodo(todo: Todo)
    }

    interface View: BaseContract.BaseView {
        fun onSuccess()
        fun onFailed()
        fun getIntentData()
        fun initForm()
    }
}