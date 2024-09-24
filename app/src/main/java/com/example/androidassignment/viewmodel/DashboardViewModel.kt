package com.example.androidassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidassignment.model.DashboardResponse
import com.example.androidassignment.networking.DashboardRequests
import com.example.androidassignment.networking.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {
    private val _dashboardData = MutableLiveData<DashboardResponse>()
    val dashboardData: LiveData<DashboardResponse> get() = _dashboardData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var errorMessage: String = ""
        private set

    fun getDashboardData() {
        _isLoading.value = true
        _isError.value = false

        val apiService = RetrofitBuilder.build().create(DashboardRequests::class.java)
        val dashboardResponse = apiService.getDashboardData()

        dashboardResponse.enqueue(object : Callback<DashboardResponse> {
            override fun onResponse(
                call: Call<DashboardResponse>,
                response: Response<DashboardResponse>
            ) {
                val responseBody = response.body()
                if (!response.isSuccessful || responseBody == null) {
                    onError("Data Processing Error")
                    return
                }

                _isLoading.value = false
                _dashboardData.postValue(responseBody)
            }

            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }
        })
    }

    private fun onError(inputMessage: String?) {
        val message =
            if (inputMessage.isNullOrBlank() or inputMessage.isNullOrEmpty()) "Unknown Error"
            else inputMessage

        errorMessage = StringBuilder("ERROR: ")
            .append("$message").toString()

        _isError.value = true
        _isLoading.value = false
    }
}