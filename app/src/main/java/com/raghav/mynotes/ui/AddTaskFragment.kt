package com.raghav.mynotes.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.raghav.mynotes.R
import com.raghav.mynotes.databinding.FragmentAddTaskBinding
import com.raghav.mynotes.models.TaskEntity
import com.raghav.mynotes.utils.MyNotesApplication
import com.raghav.mynotes.viewmodelfactories.AddTaskViewModelFactory

class AddTaskFragment : Fragment(R.layout.fragment_add_task) {
    private lateinit var binding: FragmentAddTaskBinding
    private val args: AddTaskFragmentArgs by navArgs()
    private var key: Int? = null

    private val viewModel by viewModels<AddTasksVM> {
        AddTaskViewModelFactory((activity?.application as MyNotesApplication).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddTaskBinding.bind(view)

        if (args.task != null) {
            val task = args.task
            binding.etTaskTitle.setText(task?.title)
            binding.description.setText(task?.description)
            key = task?.id
        }


        binding.btnSaveTask.setOnClickListener {
            val title = binding.etTaskTitle.text.toString()
            val description = binding.description.text.toString()

            when {
                TextUtils.isEmpty(title) -> {
                    Toast.makeText(context, "Please Enter A title", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(description) -> {
                    Toast.makeText(context, "Please Enter Task Description", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    val aTask = TaskEntity(title, description, key)
                    saveTask(aTask)
                }
            }
        }
    }

    private fun saveTask(aTask: TaskEntity) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            showProgressBar()
            viewModel.saveTask(aTask)
            hideProgressBar()
            Toast.makeText(activity, "Task Saved", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addTaskFragment_to_allTasksFragment)
        }

    }

    private fun hideProgressBar() {
        binding.progressbar.visibility = GONE
    }

    private fun showProgressBar() {
        binding.progressbar.visibility = VISIBLE
    }
}