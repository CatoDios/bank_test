package com.example.banktest.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banktest.databinding.MainFragmentBinding
import com.example.banktest.utils.ListItemClickListener
import com.example.banktest.utils.ViewState
import com.example.domain.User


class MainFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()
    private lateinit var binding : MainFragmentBinding
    private lateinit var linearLayoutManager: LinearLayoutManager

    private val userItemClickListener: ListItemClickListener<User> = {
        findNavController()
            .navigate(
                MainFragmentDirections.actionMainFragmentToUserDetailsFragment(it)

            )
        viewModel.navigatingFromDetails = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainFragmentBinding.inflate(inflater)
        retainInstance = true
        linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.layoutManager = linearLayoutManager


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.usersList.observe(viewLifecycleOwner, Observer { viewState ->
            when (viewState){
                is ViewState.RenderSuccess -> {
                    if (binding.rvUsers.adapter == null) {
                        binding.rvUsers.adapter = UserRecyclerAdapter(userItemClickListener)
                    }
                    (binding.rvUsers.adapter as UserRecyclerAdapter).itemList =
                        viewState.output
                }
                is ViewState.Loading -> {
                    binding.progressBar.visibility = if (viewState.isLoading) View.VISIBLE else View.GONE
                }

            }
        })

        viewModel.getUsers()
    }

}