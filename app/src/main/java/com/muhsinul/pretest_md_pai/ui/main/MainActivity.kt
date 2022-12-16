package com.muhsinul.pretest_md_pai.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.muhsinul.pretest_md_pai.R
import com.muhsinul.pretest_md_pai.databinding.ActivityMainBinding
import com.muhsinul.pretest_md_pai.ui.todoform.TodoFormActivity
import com.muhsinul.pretest_md_pai.ui.todolist.TodoListFragment
import com.muhsinul.pretest_md_pai.utils.ViewPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewPager()
        setClick()
    }

    private fun setClick(){
        binding.fabAdd.setOnClickListener {
            navigateToTodoForm()
        }
    }

    private fun initViewPager(){
        val fragmentAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        fragmentAdapter.addFragment(TodoListFragment.newInstance(false), "Ongoing Task")
        fragmentAdapter.addFragment(TodoListFragment.newInstance(true), "Done Task")
        binding.viewPager.apply {
            adapter = fragmentAdapter
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager, true){ tab, position ->
            tab.text = fragmentAdapter.getPageTitle(position)
        }.attach()
    }

    private fun navigateToTodoForm(){
        TodoFormActivity.startActivity(this, TodoFormActivity.MODE_INSERT)
    }
}