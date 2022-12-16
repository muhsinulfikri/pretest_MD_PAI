package com.muhsinul.pretest_md_pai.ui.todoform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhsinul.pretest_md_pai.data.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoFormViewModel(private val repository: TodoFormRepository): ViewModel(), TodoFormContract.ViewModel {

    val result = MutableLiveData<Boolean>()

    override fun insertTodo(todo: Todo) {
        viewModelScope.launch {
            try {
                val todoId = repository.addTodo(todo)
                viewModelScope.launch (Dispatchers.Main){
                    result.value = todoId > 0
                }
            } catch (e: Exception){
                viewModelScope.launch (Dispatchers.Main){
                    result.value = false
                }
            }
        }
    }

    override fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            try {
                val todoId = repository.updateTodo(todo)
                viewModelScope.launch (Dispatchers.Main){
                    result.value = todoId > 0
                }
            } catch (e: Exception){
                viewModelScope.launch (Dispatchers.Main){
                    result.value = false
                }
            }
        }
    }
}