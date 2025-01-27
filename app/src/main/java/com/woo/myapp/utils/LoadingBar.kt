package com.woo.myapp.utils

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.woo.myapp.R


@Composable
fun LoadingBar() {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(Color.Transparent), contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(ImageRequest.Builder(context).data(data = R.drawable.loadingbarnew).apply(block = {
                size(Size.ORIGINAL)
            }).build(), imageLoader = imageLoader),
            contentDescription = null,
            modifier = Modifier.wrapContentSize()
        )

    }
}