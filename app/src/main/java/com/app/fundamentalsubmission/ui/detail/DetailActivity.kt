package com.app.fundamentalsubmission.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.fundamentalsubmission.R
import com.app.fundamentalsubmission.ViewModelsFactory
import com.app.fundamentalsubmission.databinding.ActivityDetailBinding
import com.app.fundamentalsubmission.di.database.FavoriteEvent
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var bind: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelsFactory.getInstance(this@DetailActivity)
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
            detailViewModel.getDetailEvent(itemId)

            detailViewModel.detailEvent.observe(this@DetailActivity) { item ->
                item?.let { data ->
                    Glide.with(this@DetailActivity).load(data.event.mediaCover).into(ivPoto)
                    tvJudul.text = data.event.name
                    tvOwner.text = data.event.ownerName
                    tvWaktu.text = "Waktu : ${data.event.beginTime}"
                    tvQuota.text = "Kuota Tersedia : ${data.event.quota - data.event.registrants} Peserta"
                    tvDesc.text = Html.fromHtml(data.event.description, Html.FROM_HTML_MODE_LEGACY)
                    btnRegister.setOnClickListener {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(data.event.link)))
                    }

                    detailViewModel.getFavoriteById(data.event.id).observe(this@DetailActivity){ data1 ->
                        data1?.let { item1 ->
                            btnFav.setImageResource(R.drawable.baseline_favorite_24)
                            btnFav.setOnClickListener {
                                detailViewModel.deleteFavorite(item1)
                            }
                        } ?: run {
                            btnFav.setImageResource(R.drawable.baseline_favorite_border_24)
                            btnFav.setOnClickListener {
                                val favItem = FavoriteEvent(
                                    id = data.event.id,
                                    name = data.event.name,
                                    mediaCover = data.event.mediaCover,
                                    summary = data.event.summary
                                )
                                detailViewModel.insertFavorite(favoriteEvent = favItem)
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val DETAIL_ID = "id"
    }
}