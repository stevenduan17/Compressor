package com.steven.compressor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

/**
 * @author Steven
 * @version 1.0
 * @since 2019/4/16
 */
class Compressor private constructor(private val config: Config) {

    companion object {

        init {
            System.loadLibrary("compressor")
        }

        fun getDefault(config: Config) = Compressor(config)
    }


    fun compress(src: String, output: String) {
        val srcFile = File(src)
        if (!srcFile.exists()) {
            throw IllegalAccessException("source file does not exists.")
        }
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(src, options)
        val iss = getInSampleSize(options)
        options.apply {
            inSampleSize = iss
            inJustDecodeBounds = false

        }
        //Android 7.0已经默认开启huffman编码
        val bitmap = BitmapFactory.decodeFile(src, options)
        compress(bitmap, output, config.quality)
    }

    private external fun compress(bitmap: Bitmap, output: String, quality: Int)

    private fun getInSampleSize(options: BitmapFactory.Options): Int {
        val outWidth = Math.max(options.outWidth, options.outHeight)
        val outHeight = Math.min(options.outWidth, options.outHeight)
        val targetWidth = Math.max(config.maxWidth, config.maxHeight)
        val targetHeight = Math.min(config.maxWidth, config.maxHeight)
        var inSampleSize = 1
        if (outHeight > targetHeight || outWidth > targetWidth) {
            val hRatio = Math.ceil(outHeight.toDouble() / targetHeight.toDouble())
            val wRatio = Math.ceil(outWidth.toDouble() / targetWidth.toDouble())
            inSampleSize = Math.max(hRatio, wRatio).toInt()
        }
        return inSampleSize
    }
}