package com.example.tensorliteapp.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tensorliteapp.R

@Composable
fun Picture(bitmap: Bitmap?) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        val imageModifier = Modifier
            .size(250.dp)
            .clip(MaterialTheme.shapes.medium)

        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "",
                modifier = imageModifier,
                contentScale = ContentScale.Crop,
            )
        } else {
            Image(
                painterResource(id = R.drawable.placeholder),
                contentDescription = "",
                modifier = imageModifier,
                contentScale = ContentScale.Crop,
            )
        }
    }
}
