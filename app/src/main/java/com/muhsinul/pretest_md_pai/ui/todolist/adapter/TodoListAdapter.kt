package com.muhsinul.pretest_md_pai.ui.todolist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhsinul.pretest_md_pai.data.model.Todo
import com.muhsinul.pretest_md_pai.databinding.ItemTodoBinding

class TodoListAdapter(val itemClick: (Todo, Int) -> Unit, val longClick: (Todo, Int) -> Unit): RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>() {

    var items: List<Todo> = mutableListOf()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding, itemClick, longClick)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bindView(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    class TodoViewHolder(
        private val binding: ItemTodoBinding,
        val itemClick: (Todo, Int) -> Unit,
        val longClick: (Todo, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root){
        fun bindView(item: Todo, position: Int) {
            with(item) {
                binding.tvTitleItemTask.text = item.title
                itemView.setOnClickListener { itemClick(this, position) }
                itemView.setOnLongClickListener {
                    longClick(this, position)
                    true
                }
            }

        }
    }


}