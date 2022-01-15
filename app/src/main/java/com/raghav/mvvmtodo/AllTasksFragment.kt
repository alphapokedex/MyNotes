package com.raghav.mvvmtodo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.raghav.mvvmtodo.adapter.TasksAdapter
import com.raghav.mvvmtodo.databinding.FragmentAllTasksBinding
import com.raghav.mvvmtodo.utils.Resource

class AllTasksFragment : Fragment(R.layout.fragment_all_tasks) {
    lateinit var binding: FragmentAllTasksBinding

    private val viewModel by viewModels<AllTasksVM> {
        AllTasksViewModelFactory((activity?.application as MvvmTodo).repository)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAllTasksBinding.bind(view)
        setUpRecyclerView()


        binding.btnAddTasks.setOnClickListener {
            findNavController().navigate(R.id.action_allTasksFragment_to_addTaskFragment)
        }

        viewModel.tasks.observe(viewLifecycleOwner, { data ->
            when (data) {
                is Resource.Loading -> showProgressBar()
                is Resource.Success -> {
                    hideProgressBar()
                    binding.rvTasks.adapter = data.data?.let { TasksAdapter(it) }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(activity, data.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })


    }

    private fun setUpRecyclerView() {
        binding.rvTasks.layoutManager = LinearLayoutManager(activity)
    }

    private fun hideProgressBar() {
        binding.progressbar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressbar.visibility = View.VISIBLE
    }
}
