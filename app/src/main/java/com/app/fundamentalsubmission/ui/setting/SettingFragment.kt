package com.app.fundamentalsubmission.ui.setting

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.app.fundamentalsubmission.ViewModelsFactory
import com.app.fundamentalsubmission.databinding.FragmentSettingBinding
import com.app.fundamentalsubmission.di.DailyReminderWorker
import com.app.fundamentalsubmission.di.Injection.messageError
import com.app.fundamentalsubmission.di.models.EventModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment() {

    private lateinit var bind: FragmentSettingBinding
    private val settingViewModel by viewModels<SettingViewModel> {
        ViewModelsFactory.getInstance(requireActivity())
    }
    private lateinit var listEvent: EventModel

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                messageError(requireActivity(), "Notifications permission granted")
            } else {
                messageError(requireActivity(),"Notifications permission rejected")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bind = FragmentSettingBinding.inflate(layoutInflater, container, false)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        settingViewModel.getTheme().observe(viewLifecycleOwner){ isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            bind.swTheme.isChecked = isDarkMode
        }

        settingViewModel.getAllNearestEvent()

        settingViewModel.nearestEvent.observe(viewLifecycleOwner) { list ->
            list?.let {
                listEvent = it
            } ?: messageError(requireActivity(), "Data tidak ada")
        }

        settingViewModel.getDailyReminder().observe(viewLifecycleOwner) { isActive ->
            if (isActive) {
                if (::listEvent.isInitialized && listEvent.listEvents.isNotEmpty()) {
                    val inputData = Data.Builder()
                        .putString("eventName", listEvent.listEvents[0].name)
                        .putString("eventDate", listEvent.listEvents[0].beginTime)
                        .build()

                    val workRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
                        .setInputData(inputData)
                        .build()

                    WorkManager.getInstance(requireActivity()).enqueueUniquePeriodicWork(
                        "DailyReminder",
                        ExistingPeriodicWorkPolicy.UPDATE,
                        workRequest
                    )
                }
            } else {
                WorkManager.getInstance(requireActivity()).cancelUniqueWork("DailyReminder")
            }
            bind.swDaily.isChecked = isActive
        }


        bind.swDaily.setOnCheckedChangeListener{_, isChecked ->
            lifecycleScope.launch {
                settingViewModel.saveDailyReminder(isChecked)
            }
        }

        bind.swTheme.setOnCheckedChangeListener{ _, isChecked ->
            lifecycleScope.launch {
                settingViewModel.saveThemeSet(isDarkMode = isChecked)
            }
        }

        return bind.root
    }

}