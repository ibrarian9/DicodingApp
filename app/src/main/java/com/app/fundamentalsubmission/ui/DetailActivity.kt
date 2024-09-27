package com.app.fundamentalsubmission.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.app.fundamentalsubmission.R
import com.app.fundamentalsubmission.ViewModelsFactory
import com.app.fundamentalsubmission.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var bind: ActivityDetailBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelsFactory.getInstance()
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val itemId = intent.getIntExtra(DETAIL_ID, 0)

        bind.apply {
            lifecycleScope.launch {
                mainViewModel.getDetailEvent(itemId).observe(this@DetailActivity) { item ->
                    Glide.with(this@DetailActivity).load(item.mediaCover).into(ivPoto)
                    tvJudul.text = item.name
                    tvOwner.text = item.ownerName
                    tvWaktu.text = "Waktu : ${item.beginTime}"
                    tvQuota.text = "Kuota Tersedia : ${item.quota - item.registrants} Peserta"
                    tvDesc.text = Html.fromHtml(item.description, Html.FROM_HTML_MODE_LEGACY)
                    btnRegister.setOnClickListener {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.link)))
                    }
                }
            }
        }
    }

    companion object {
        const val DETAIL_ID = "id"
    }
}