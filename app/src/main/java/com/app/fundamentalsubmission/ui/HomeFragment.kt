package com.app.fundamentalsubmission.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fundamentalsubmission.ViewModelsFactory
import com.app.fundamentalsubmission.adapter.EventHorizontalAdapter
import com.app.fundamentalsubmission.adapter.EventVerticalAdapter
import com.app.fundamentalsubmission.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var bind: FragmentHomeBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelsFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bind = FragmentHomeBinding.inflate(inflater, container, false)

        val adapterVerticalAdapter = EventVerticalAdapter()
        val adapterHorizontalAdapter = EventHorizontalAdapter()

        bind.apply {
            rvUpcoming.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            rvUpcoming.adapter = adapterHorizontalAdapter
            rvFinished.layoutManager = LinearLayoutManager(requireActivity())
            rvFinished.adapter = adapterVerticalAdapter

            mainViewModel.isLoading.observe(viewLifecycleOwner) {
                prgressBar.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        fun messageError(string: String){
            Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
        }

        mainViewModel.upcomingEvents.observe(viewLifecycleOwner) {
            it?.let {
                adapterHorizontalAdapter.submitList(it.listEvents)
            } ?: run {
                messageError("Failed to initialize data")
            }
        }

        mainViewModel.finishedEvent.observe(viewLifecycleOwner) {
            it?.let {
                adapterVerticalAdapter.submitList(it.listEvents)
            } ?: run {
                messageError("Failed to initialize data")
            }
        }

        mainViewModel.loadAllUpcomingEvent(5)
        mainViewModel.loadAllFinishedEvent(5)

        return bind.root
    }

}