## Compressor

Android的图像引擎为Skia,Skia是基于libjpeg进行封装和优化的。Andoroid 7.0以前，Android默认的图片压缩默认关闭Huffman编码，这是google基于手机性能的考虑，android 7.0后，Android已经默认开启Huffman编码。
```
        jpeg_set_defaults(&cinfo);
        
        // Tells libjpeg-turbo to compute optimal Huffman coding tables
        // for the image.  This improves compression at the cost of
        // slower encode performance.
        cinfo.optimize_coding = TRUE;
        jpeg_set_quality(&cinfo, quality, TRUE /* limit to baseline-JPEG values */);

        jpeg_start_compress(&cinfo, TRUE);
```
### 关于Huffman编码

一句话：Huffman编码采用变长编码的方式，相同的条件下占用的空间更少，具体原理网络上很多人都分析过，不赘述。
