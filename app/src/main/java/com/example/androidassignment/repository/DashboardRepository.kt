package com.example.androidassignment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidassignment.model.DashboardResponse
import com.example.androidassignment.networking.DashboardRequests
import com.example.androidassignment.networking.NetworkState
import com.example.androidassignment.networking.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardRepository {

    private val _networkState = MutableLiveData<NetworkState<DashboardResponse>>()
    val networkState: LiveData<NetworkState<DashboardResponse>> get() = _networkState

    fun fetchDashboardData() {
        _networkState.value = NetworkState.Loading

        val apiService = RetrofitBuilder.build().create(DashboardRequests::class.java)
        val dashboardResponse = apiService.getDashboardData()

        dashboardResponse.enqueue(object : Callback<DashboardResponse> {
            override fun onResponse(
                call: Call<DashboardResponse>,
                response: Response<DashboardResponse>
            ) {
                val responseBody = response.body()
                if (!response.isSuccessful || responseBody == null) {
                    _networkState.value = NetworkState.Error("Data Processing Error")
                    return
                }

                _networkState.value = NetworkState.Success(responseBody)
            }

            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                _networkState.value = NetworkState.Error(t.message ?: "Unknown Error")
                t.printStackTrace()
            }
        })
    }
}
