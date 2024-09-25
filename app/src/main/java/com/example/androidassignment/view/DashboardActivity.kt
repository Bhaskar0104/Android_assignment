package com.example.androidassignment.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.androidassignment.R
import com.example.androidassignment.databinding.ActivityDashboardBinding
import com.example.androidassignment.databinding.ItemProgressDialogBinding
import com.example.androidassignment.model.Content
import com.example.androidassignment.networking.NetworkState
import com.example.androidassignment.viewmodel.DashboardViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DashboardActivity : AppCompatActivity() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var binding: ActivityDashboardBinding
    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        observeDashboardData()
        viewModel.getDashboardData()
    }

    private fun observeDashboardData() {
        viewModel.networkState.observe(this) { state ->
            when (state) {
                is NetworkState.Loading -> showProgress()
                is NetworkState.Success -> {
                    hideProgress()
                    val contentData: String? = state.data.choices[0].message?.content
                    val jsonType = object : TypeToken<Content>() {}.type
                    val content = Gson().fromJson<Content>(contentData, jsonType)
                    setDashboardData(content)
                }

                is NetworkState.Error -> {
                    hideProgress()
                    binding.errorText.visibility = View.VISIBLE
                    binding.errorText.text = state.message
                }
            }
        }
    }

    private fun setDashboardData(content: Content?) {
        binding.apply {
            scrollView.visibility = View.VISIBLE
            overallScoreProgress.text = "53"
            progressScore.progress = 53
            titleTextView.text = content?.titles?.joinToString { it } ?: "No Title"
            descriptionTextView.text = content?.description ?: "No Description"
        }
    }

    private fun showProgress(msg: String = "Loading...") {
        if (progressDialog != null && progressDialog!!.isShowing) {
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
