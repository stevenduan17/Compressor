package com.steven.sample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.steven.compressor.Compressor
import com.steven.compressor.Config
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }

        button.setOnClickListener { test() }
    }

    private fun test() {
        val file = File(Environment.getExternalStorageDirectory(), "test.jpg")
        val output = File(Environment.getExternalStorageDirectory(), "test_compressed.jpg")
        object : Thread() {
            override fun run() {
                super.run()
                Log.d(TAG, ">>>start: " + System.currentTimeMillis())
                Compressor.getDefault(Config.Builder.newBuilder().build()).compress(
                    file.absolutePath,
                    output.absolutePath
                )
                Log.d(TAG, ">>>finish: " + System.currentTimeMillis())
            }
        }.start()
    }
}
