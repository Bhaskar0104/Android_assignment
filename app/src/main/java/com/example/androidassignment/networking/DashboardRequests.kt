package com.example.androidassignment.networking

import com.example.androidassignment.model.DashboardResponse
import retrofit2.Call
import retrofit2.http.GET

interface DashboardRequests {
    @GET("b/6HBE")
    fun getDashboardData(): Call<DashboardResponse>
}