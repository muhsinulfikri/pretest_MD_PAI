package com.muhsinul.pretest_md_pai.ui.todolist

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhsinul.pretest_md_pai.base.GenericViewModelFactory
import com.muhsinul.pretest_md_pai.base.Resource
import com.muhsinul.pretest_md_pai.data.constant.Constant
import com.muhsinul.pretest_md_pai.data.local.room.TodoRoomDatabase
import com.muhsinul.pretest_md_pai.data.local.room.datasource.TodoDataSource
import com.muhsinul.pretest_md_pai.data.model.Todo
import com.muhsinul.pretest_md_pai.databinding.FragmentTodoListBinding
import com.muhsinul.pretest_md_pai.ui.detail.DetailActivity
import com.muhsinul.pretest_md_pai.ui.todolist.adapter.TodoListAdapter

class TodoListFragment: Fragment(), TodoListContract.View {

    private var isFilteredByTaskStatus: Boolean = false
    private lateinit var binding: FragmentTodoListBinding
    private lateinit var adapter: TodoListAdapter
    private lateinit var viewModel: TodoListViewModel

    companion object{
        private const val ARG_FILTERED_TASK = "ARG_FILTERED_TASK"
        @JvmStatic
        fun newInstance(isFilterDone: Boolean) =
            TodoListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_FILTERED_TASK, isFilterDone)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isFilteredByTaskStatus = it.getBoolean(ARG_FILTERED_TASK)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun getData() {
       viewModel.getTodoComplete(isFilteredByTaskStatus)
    }

    override fun onDataSuccess(todos: List<Todo>) {
        todos.let {
            adapter.items = it
        }
    }

    override fun onDataFailed(msg: String?) {
        Toast.makeText(context, msg ?: "Get Data Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onDataEmpty() {
        adapter.items = mutableListOf()
    }

    override fun onDeleteSuccess() {
        getData()
    }

    override fun onDeleteFailed() {
        Toast.makeText(context, "Delete Data Failed", Toast.LENGTH_SHORT).show()
    }

    override fun setLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun setEmptyStateVisibility(isDataEmpty: Boolean) {
        binding.tvMessage.text = "No Data"
        binding.tvMessage.visibility = if (isDataEmpty) View.VISIBLE else View.GONE
    }

    override fun initList() {
        adapter = TodoListAdapter({todo, pos ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(Constant.EXTRAS_DATA_TODO, todo.id)
            startActivity(intent)
        }, { todo, pos ->
            showDialogToDeleteTodo(todo)
        })
        binding.rvTask.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@TodoListFragment.adapter
        }
    }

    override fun initView() {
        initViewModel()
        initSwipeRefresh()
        initList()
    }

    override fun initViewModel() {
        context?.let {
            val dataSource = TodoDataSource(TodoRoomDatabase.getInstance(it).todoDao())
            val repository = TodoListRepository(dataSource)
            viewModel = GenericViewModelFactory(TodoListViewModel(repository)).create(TodoListViewModel::class.java)
        }
        viewModel.todoData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    setLoading(true)
                    setEmptyStateVisibility(false)
                }
                is Resource.Success -> {
                    setLoading(false)
                    it.data?.let { data ->
                        if (data.isNullOrEmpty()) {
                            onDataEmpty()
                            setEmptyStateVisibility(true)
                        } else {
                            onDataSuccess(data)
                        }
                    }
                }
                is Resource.Error -> {
                    setLoading(false)
                    setEmptyStateVisibility(false)
                    onDataFailed(it.message.orEmpty())
                }
            }
        }
        viewModel.deleteResponse.observe(viewLifecycleOwner) { isDeleteSuccess ->
            if (isDeleteSuccess) {
                onDeleteSuccess()
            } else {
                onDeleteFailed()
            }
        }
    }

    private fun initSwipeRefresh(){
        binding.srlTask.setOnRefreshListener {
            binding.srlTask.isRefreshing = false
            getData()
        }
    }

    private fun showDialogToDeleteTodo(todo: Todo){
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Are You Sure To Delete \"${todo.title}\" ?")
                setPositiveButton("Yes") {dialog, id ->
                    viewModel.deleteTodo(todo)
                    dialog.dismiss()
                }
                setNegativeButton("No") {dialog, id ->
                    dialog.dismiss()
                }
            }
            builder.create()
        }
        alertDialog?.show()
    }
}