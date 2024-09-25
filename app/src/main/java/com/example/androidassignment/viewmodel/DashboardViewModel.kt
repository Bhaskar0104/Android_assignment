package com.example.androidassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.androidassignment.model.DashboardResponse
import com.example.androidassignment.networking.NetworkState
import com.example.androidassignment.repository.DashboardRepository

class DashboardViewModel : ViewModel() {

    private val repository: DashboardRepository = DashboardRepository()

    val networkState: LiveData<NetworkState<DashboardResponse>> get() = repository.networkState

    fun getDashboardData() {
        repository.fetchDashboardData()
    }
}