package com.example.tensorliteapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Buttons(onStartCamera: () -> Unit, onDetectLabel: () -> Unit, onExtractText: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Button(onClick = { onDetectLabel() }) {
                Text(text = "Etiquetas")
            }
            Button(onClick = { onExtractText() }) {
                Text(text = "Texto")
            }
        }
        Button(onClick = { onStartCamera() }) {
            Text(text = "Nueva Foto")
        }
    }
}

