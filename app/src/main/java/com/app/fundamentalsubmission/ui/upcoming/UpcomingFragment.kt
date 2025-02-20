package com.app.fundamentalsubmission.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fundamentalsubmission.ViewModelsFactory
import com.app.fundamentalsubmission.adapter.EventVerticalAdapter
import com.app.fundamentalsubmission.databinding.FragmentUpcomingBinding
import com.app.fundamentalsubmission.di.models.ListEventsItem
import com.app.fundamentalsubmission.ui.MainViewModel
import kotlinx.coroutines.launch

class UpcomingFragment : Fragment() {

    private lateinit var bind: FragmentUpcomingBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelsFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bind = FragmentUpcomingBinding.inflate(inflater, container, false)

        val adapter = EventVerticalAdapter()

        bind.apply {
            rv.layoutManager = LinearLayoutManager(requireActivity())
            rv.adapter = adapter

            mainViewModel.isLoading.observe(viewLifecycleOwner) {
                prgressBar.visibility = if (it) View.VISIBLE else View.GONE
            }

            fun updateEventList(events: List<ListEventsItem>) {
                adapter.submitList(events)
            }

            fun messageError(string: String) {
                Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
            }

            mainViewModel.upcomingEvents.observe(viewLifecycleOwner) {
                it?.let {
                   updateEventList(it.listEvents)
                } ?: run {
                    messageError("Failed to initialize data")
                }
            }

            searchEvent.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    lifecycleScope.launch {
                        if (!query.isNullOrEmpty()) {
                            mainViewModel.getSearchUpcomingEvent(q = query).observe(viewLifecycleOwner) {
                                it?.let {
                                    updateEventList(it.listEvents)
                                }?: run {
                                    messageError("Failed to initialize data")
                                }
                            }
                        }
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                   return false
                }
            })
        }

        mainViewModel.loadAllUpcomingEvent(40)

        return bind.root
    }
}