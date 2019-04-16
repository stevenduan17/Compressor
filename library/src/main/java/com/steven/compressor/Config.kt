package com.steven.compressor

import java.lang.IllegalArgumentException

/**
 * @author Steven
 * @version 1.0
 * @since 2019/4/16
 */
class Config private constructor(
    val maxWidth: Float,
    val maxHeight: Float,
    val quality: Int
) {


    class Builder private constructor() {

        companion object {
            private const val DEFAULT_QUALITY = 70
            private const val MAX_HEIGHT = 1080F
            private const val MAX_WIDTH = 720F

            fun newBuilder() = Builder()
        }

        private var mQuality: Int = DEFAULT_QUALITY
        private var mMaxHeight: Float = MAX_HEIGHT
        private var mMaxWidth: Float = MAX_WIDTH


        fun setQuality(quality: Int): Builder {
            if ((quality < 0) or (quality > 100)) {
                throw IllegalArgumentException("quality should be range from 0 to 100.")
            }
            this.mQuality = quality
            return this
        }

        fun setMaxSize(maxHeight: Float, maxWidth: Float): Builder {
            this.mMaxHeight = maxHeight
            this.mMaxWidth = maxWidth
            return this
        }


        fun build(): Config {
            return Config(mMaxHeight, mMaxWidth,mQuality)
        }
    }
}