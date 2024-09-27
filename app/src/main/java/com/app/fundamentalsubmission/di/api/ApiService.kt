package com.app.fundamentalsubmission.di.api

import com.app.fundamentalsubmission.di.models.DetailEventModel
import com.app.fundamentalsubmission.di.models.EventModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("events")
    suspend fun getAllUpcomingEvent(
        @Query("active") active: Int = 1,
        @Query("limit") limit: Int
    ): Response<EventModel>

    @GET("events")
    suspend fun getAllFinishedEvent(
        @Query("active") active: Int = 0,
        @Query("limit") limit: Int
    ): Response<EventModel>

    @GET("events")
    suspend fun getSearchUpcomingEvent(
        @Query("active") active: Int = 1,
        @Query("q") query: String
    ): Response<EventModel>

    @GET("events")
    suspend fun getSearchFinishedEvent(
        @Query("active") active: Int = 0,
        @Query("q") query: String
    ): Response<EventModel>

    @GET("events/{id}")
    suspend fun getDetailEvent(
        @Path("id") id: Int
    ): Response<DetailEventModel>
}