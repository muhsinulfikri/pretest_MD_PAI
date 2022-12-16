package com.muhsinul.pretest_md_pai.ui.todolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhsinul.pretest_md_pai.base.Resource
import com.muhsinul.pretest_md_pai.data.model.Todo
import com.muhsinul.pretest_md_pai.ui.todoform.TodoFormContract
import com.muhsinul.pretest_md_pai.ui.todoform.TodoFormViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoListViewModel(private val repository: TodoListRepository): ViewModel(), TodoListContract.ViewModel {

    val todoData = MutableLiveData<Resource<List<Todo>>>()
    val deleteResponse = MutableLiveData<Boolean>()

    override fun getTodoComplete(isTodoCompleted: Boolean) {
        todoData.value = Resource.Loading()
        viewModelScope.launch (Dispatchers.IO){
            try {
                val todo = repository.getTodoComplete(isTodoCompleted)
                viewModelScope.launch (Dispatchers.Main){
                    todoData.value = Resource.Success(todo)
                }
            } catch (e: Exception){
                viewModelScope.launch (Dispatchers.Main){
                    todoData.value = Resource.Error(e.message.orEmpty())
                }
            }
        }
    }

    override fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            try {
                val result = repository.deleteTodo(todo)
                viewModelScope.launch (Dispatchers.Main){
                    deleteResponse.value = result == 1
                }
            } catch (e: Exception){
                viewModelScope.launch (Dispatchers.Main){
                    deleteResponse.value = false
                }
            }
        }
    }


}