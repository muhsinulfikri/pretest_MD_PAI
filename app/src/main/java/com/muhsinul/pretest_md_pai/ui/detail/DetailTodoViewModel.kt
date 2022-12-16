package com.muhsinul.pretest_md_pai.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhsinul.pretest_md_pai.base.Resource
import com.muhsinul.pretest_md_pai.data.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailTodoViewModel(private val repository: DetailTodoRepository): ViewModel(), DetailTodoContract.ViewModel {

    val detailTodo = MutableLiveData<Resource<Todo>>()
    val changeStatusTodoResult = MutableLiveData<Pair<Boolean, Todo?>>()

    override fun getDetail(todoId: Int) {
        detailTodo.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val todo = repository.getTodoId(todoId)
                viewModelScope.launch (Dispatchers.Main){
                    detailTodo.value = Resource.Success(todo)
                }
            } catch (e: Exception){
                viewModelScope.launch (Dispatchers.Main){
                    detailTodo.value = Resource.Error(e.message.orEmpty())
                }
            }
        }
    }

    override fun changeStatusTodo(todo: Todo) {
        viewModelScope.launch {
            try {
                val todoStatus = repository.changeTodoStatus(todo)
                viewModelScope.launch (Dispatchers.Main){
                    changeStatusTodoResult.value = Pair(true, todoStatus)
                }
            } catch (e: Exception){
                viewModelScope.launch (Dispatchers.Main){
                    changeStatusTodoResult.value = Pair(false, null)
                }
            }
        }
    }
}