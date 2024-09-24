package com.example.androidassignment.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.androidassignment.R
import com.example.androidassignment.databinding.ActivityDashboardBinding
import com.example.androidassignment.databinding.ItemProgressDialogBinding
import com.example.androidassignment.model.Content
import com.example.androidassignment.viewmodel.DashboardViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DashboardActivity : AppCompatActivity() {

    private lateinit var mainViewModel: DashboardViewModel

    private lateinit var binding: ActivityDashboardBinding
    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = DashboardViewModel()
        observeDashboardData()
        mainViewModel.getDashboardData()
    }

    private fun observeDashboardData() {
        mainViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) showProgress()
        }

        mainViewModel.isError.observe(this) { isError ->
            if (isError) {
                hideProgress()
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = mainViewModel.errorMessage
            }
        }

        mainViewModel.dashboardData.observe(this) { data ->
            // Display dashboard data to the UI
            val contentData: String? = data?.choices?.get(0)?.message?.content
            val jsonType = object : TypeToken<Content>() {}.type
            val content = Gson().fromJson<Content>(contentData, jsonType)
            setDashboardData(content)
        }
    }

    private fun setDashboardData(content: Content?) {
        hideProgress()
        binding.apply {
            scrollView.visibility = View.VISIBLE
            overallScoreProgress.text = "53"
            progressScore.progress = 53
            titleTextView.text =
                content!!.titles.joinToString { it }
            binding.descriptionTextView.text = content.description
        }
    }

    private fun showProgress(msg: String = "Loading...") {
        if (progressDialog != null && !progressDialog!!.isShowing) {
            return
        } else if (isFinishing) {
            return
        }
        val builder = AlertDialog.Builder(this)
        val view = ItemProgressDialogBinding.inflate(LayoutInflater.from(this))
        view.root.findViewById<TextView>(R.id.progress_text).text = msg
        builder.setView(view.root)
        progressDialog = builder.create()
        progressDialog?.show()
        progressDialog?.setCancelable(false)
    }

    private fun hideProgress() {
        if (progressDialog != null && progressDialog?.isShowing == true && !isFinishing) {
            progressDialog?.dismiss()
            progressDialog = null
        }
    }
}