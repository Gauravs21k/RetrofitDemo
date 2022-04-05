package com.kaimiya.gaurav.retrofitdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaimiya.gaurav.retrofitdemo.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException
const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoRecAdapter: TodoRecAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecyclerView()

        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible = true

            val response = try {
                RetrofitInstance.api.getTodos()
            } catch (e: IOException) {
                Log.e(TAG, "IOException, no internet maybe")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            }

            if (response.isSuccessful && response.body() != null) {
                todoRecAdapter.todos = response.body()!!
            } else {
                Log.e(TAG, "Response not successful")
            }
            binding.progressBar.isVisible = false
        }
    }

    private fun setRecyclerView() = binding.recyclerViewTodos.apply {
        todoRecAdapter = TodoRecAdapter()
        adapter = todoRecAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)
    }
}