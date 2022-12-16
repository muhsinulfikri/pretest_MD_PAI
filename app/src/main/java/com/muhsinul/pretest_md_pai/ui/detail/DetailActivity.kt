package com.muhsinul.pretest_md_pai.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.muhsinul.pretest_md_pai.R
import com.muhsinul.pretest_md_pai.base.GenericViewModelFactory
import com.muhsinul.pretest_md_pai.base.Resource
import com.muhsinul.pretest_md_pai.data.constant.Constant
import com.muhsinul.pretest_md_pai.data.local.room.TodoRoomDatabase
import com.muhsinul.pretest_md_pai.data.local.room.datasource.TodoDataSource
import com.muhsinul.pretest_md_pai.data.model.Todo
import com.muhsinul.pretest_md_pai.databinding.ActivityDetailBinding
import com.muhsinul.pretest_md_pai.ui.todoform.TodoFormActivity

class DetailActivity: AppCompatActivity(), DetailTodoContract.View {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailTodoViewModel
    private var todoId: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun getIntentData(){
        todoId = intent?.getIntExtra(Constant.EXTRAS_DATA_TODO, -1)
    }

    private fun bindView(todo: Todo){
        supportActionBar?.hide()
        binding.tvTitleTodo.text = todo?.title
        binding.tvDescTodo.text = todo?.desc
        setFabIcon(todo)
        binding.fabCheck.setOnClickListener {
            todo?.let {
                viewModel.changeStatusTodo(it)
            }
        }
        binding.fabEdit.setOnClickListener {
            TodoFormActivity.startActivity(this, TodoFormActivity.MODE_EDIT, todo)
        }
    }

    private fun setFabIcon(todo: Todo?){
        binding.fabCheck.setImageResource(if (todo?.isTaskCompleted == true) R.drawable.ic_done else R.drawable.ic_check)
    }

    override fun onFetchDetailSuccess(todo: Todo) {
        bindTodoData(todo)
    }

    override fun onFetchDetailFailed() {
        Toast.makeText(this, "Get Data Failed for id $todoId", Toast.LENGTH_SHORT).show()
    }

    override fun onChangeTodoStatusSuccess(todo: Todo) {
        getData()
        if (todo.isTaskCompleted){
            Snackbar.make(binding.root, "Success Set Todo to Done", Snackbar.LENGTH_SHORT)
                .show()
        } else {
            Snackbar.make(binding.root, "Success Set Todo to Undone", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    override fun onChangeTodoStatusFailed() {
        Toast.makeText(this, "Change todo status failed", Toast.LENGTH_SHORT).show()
    }

    override fun bindTodoData(todo: Todo) {
        bindView(todo)
    }

    override fun getData() {
        todoId?.let { viewModel.getDetail(it) }
    }

    override fun initView() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        getIntentData()
        initViewModel()
    }

    override fun initViewModel() {
        val dataSource = TodoDataSource(TodoRoomDatabase.getInstance(this).todoDao())
        val repository = DetailTodoRepository(dataSource)
        viewModel = GenericViewModelFactory(DetailTodoViewModel(repository)).create(DetailTodoViewModel::class.java)
        viewModel.detailTodo.observe(this){
            when (it){
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    it.data?.let { data ->
                        onFetchDetailSuccess(data)
                    }
                }
                is Resource.Error -> {
                    onFetchDetailFailed()
                }
            }
        }
        viewModel.changeStatusTodoResult.observe(this){
            if (it.first){
                it.second?.let { todo ->
                    onChangeTodoStatusSuccess(todo)
                }
            } else {
                onChangeTodoStatusFailed()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }
}