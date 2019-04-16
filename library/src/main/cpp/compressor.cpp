#include <jni.h>
#include <string>
#include "turbojpeg.h"
#include "jpeglib.h"
#include <android/bitmap.h>
#include <csetjmp>

typedef uint8_t BYTE;

void writeImage(BYTE *data, const char *output, int w, int h, jint quality);

extern "C"
JNIEXPORT void JNICALL
Java_com_steven_compressor_Compressor_compress(JNIEnv *env, jobject instance, jobject bitmap,
                                               jstring output_, jint quality) {
    const char *output = env->GetStringUTFChars(output_, 0);

    //to pixels.
    AndroidBitmapInfo bitmapInfo;
    AndroidBitmap_getInfo(env, bitmap, &bitmapInfo);
    BYTE *pixels;
    AndroidBitmap_lockPixels(env, bitmap, reinterpret_cast<void **>(&pixels));
    int w = bitmapInfo.width;
    int h = bitmapInfo.height;

    BYTE *data, *tempData;
    data = static_cast<BYTE *>(malloc(w * h * 3));
    tempData = data;

    //get b,g,r pixel.
    BYTE r, g, b;
    int color;
    for (int i = 0; i < h; ++i) {
        for (int j = 0; j < w; ++j) {
            //pixels[i][j]
            color = *((int *) pixels);
            r = (color & 0x00FF0000) >> 16;
            g = (color & 0x0000FF00) >> 8;
            b = (color & 0x000000FF);
            *data = b;
            *(data + 1) = g;
            *(data + 2) = r;
            data += 3;
            pixels += 4;
        }
    }

    AndroidBitmap_unlockPixels(env, bitmap);
    //w*h*3
    writeImage(tempData, output, w, h, quality);

    free(tempData);
    env->ReleaseStringUTFChars(output_, output);
}

void writeImage(BYTE *data, const char *output, int w, int h, jint quality) {
    struct jpeg_compress_struct jpeg_struct;
    jpeg_error_mgr err;
    jpeg_struct.err = jpeg_std_error(&err);
    jpeg_create_compress(&jpeg_struct);
    FILE *file = fopen(output, "wb");
    jpeg_stdio_dest(&jpeg_struct, file);

    jpeg_struct.image_width = w;
    jpeg_struct.image_height = h;
    //open huffman.
    jpeg_struct.arith_code = false;
    jpeg_struct.optimize_coding = TRUE;
    jpeg_struct.in_color_space = JCS_RGB;


    jpeg_struct.input_components = 3;
    jpeg_set_defaults(&jpeg_struct);

    jpeg_set_quality(&jpeg_struct, quality, true);

    jpeg_start_compress(&jpeg_struct, TRUE);

    JSAMPROW row_pointer[1];
    while (jpeg_struct.next_scanline < h) {
        row_pointer[0] = &data[jpeg_struct.next_scanline * w * 3];
        jpeg_write_scanlines(&jpeg_struct, row_pointer, 1);
    }

    jpeg_finish_compress(&jpeg_struct);
    jpeg_destroy_compress(&jpeg_struct);
    fclose(file);
}