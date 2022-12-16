package com.muhsinul.pretest_md_pai.ui.todoform

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.muhsinul.pretest_md_pai.base.GenericViewModelFactory
import com.muhsinul.pretest_md_pai.data.local.room.TodoRoomDatabase
import com.muhsinul.pretest_md_pai.data.local.room.datasource.TodoDataSource
import com.muhsinul.pretest_md_pai.data.model.Todo
import com.muhsinul.pretest_md_pai.databinding.ActivityFormTodoBinding

class TodoFormActivity: AppCompatActivity(), TodoFormContract.View {

    private lateinit var binding: ActivityFormTodoBinding
    private lateinit var viewModel: TodoFormViewModel
    private var formMode: Int = MODE_INSERT
    private var todo: Todo? = null

    companion object{
        const val MODE_INSERT = 0
        const val MODE_EDIT = 1
        const val ARG_MODE = "ARG_MODE"
        const val ARG_TODO_DATA = "ARG_TODO_DATA"

        fun startActivity(context: Context, formMode: Int, todo: Todo?){
            val intent = Intent(context, TodoFormActivity::class.java)
            intent.putExtra(ARG_MODE, formMode)
            todo?.let {
                intent.putExtra(ARG_TODO_DATA, todo)
            }
            context.startActivity(intent)
        }

        fun startActivity(context: Context, formMode: Int){
            startActivity(context, formMode, null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun setClickListener(){
        binding.btnSaveTask.setOnClickListener {
            saveTodo()
        }
    }

    private fun saveTodo(){
        if (isTodoFormFilled()){
            if (formMode == MODE_EDIT) {
                todo = todo?.copy()?.apply {
                    title = binding.etTaskName.text.toString()
                    desc = binding.etTaskDesc.text.toString()
                }
                todo?.let { viewModel.updateTodo(it) }
            } else {
                todo = Todo(
                    title = binding.etTaskName.text.toString(),
                    desc = binding.etTaskDesc.text.toString()
                )
                todo?.let { viewModel.insertTodo(it) }
            }
        }
    }

    private fun isTodoFormFilled(): Boolean{
        val title = binding.etTaskName.text.toString()
        val desc = binding.etTaskDesc.text.toString()
        var isFormValid = true

        //check title empty
        if (title.isEmpty()){
            isFormValid = false
            binding.tilTaskName.isErrorEnabled = true
            binding.tilTaskName.error = "Title must be filled"
        } else {
            binding.tilTaskName.isErrorEnabled = false
        }
        //check desc empty
        if (desc.isEmpty()){
            isFormValid = false
            binding.tilTaskDesc.isErrorEnabled = true
            binding.tilTaskDesc.error = "Description must be filled"
        } else {
            binding.tilTaskDesc.isErrorEnabled = false
        }
        return isFormValid
    }

    override fun onSuccess() {
        Toast.makeText(this, "Save Todo Success", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onFailed() {
        Toast.makeText(this, "Save Todo Failed", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun getIntentData() {
        formMode = intent.getIntExtra(ARG_MODE, 0)
        todo = intent.getParcelableExtra(ARG_TODO_DATA)
    }

    override fun initForm() {
        initViewModel()
        if (formMode == MODE_EDIT){
            todo?.let {
                binding.etTaskName.setText(it.title)
                binding.etTaskDesc.setText(it.desc)
            }
            supportActionBar?.title = "Edit Todo"
        } else {
            supportActionBar?.title = "Create New Todo"
        }
    }

    override fun initView() {
        binding = ActivityFormTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClickListener()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getIntentData()
        initForm()
    }

    override fun initViewModel() {
        val dataSource = TodoDataSource(TodoRoomDatabase.getInstance(this).todoDao())
        val repository = TodoFormRepository(dataSource)
        viewModel = GenericViewModelFactory(TodoFormViewModel(repository)).create(TodoFormViewModel::class.java)
        viewModel.result.observe(this) { isSuccess ->
            if (isSuccess) {
                onSuccess()
            } else {
                onFailed()
            }
        }
    }
}