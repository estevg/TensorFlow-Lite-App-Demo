package com.example.tensorliteapp

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tensorliteapp.components.Buttons
import com.example.tensorliteapp.components.Details
import com.example.tensorliteapp.components.Picture
import com.example.tensorliteapp.core.TakePhoto
import com.example.tensorliteapp.core.getImageUri
import com.example.tensorliteapp.ui.theme.TensorLiteAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class MainActivity : ComponentActivity() {

    private val dispatchTakePicture = TakePhoto(this)
    private val TAG = "TensorFlow"

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            var bitmap: Bitmap? by rememberSaveable {
                mutableStateOf(null)
            }


            val cameraPermissionState = rememberPermissionState(
                android.Manifest.permission.CAMERA
            )

            var text by remember {
                mutableStateOf("")
            }

            TensorLiteAppTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Picture(bitmap)
                    Spacer(modifier = Modifier.size(30.dp))
                    Buttons(
                        onStartCamera = {
                            if (cameraPermissionState.status.isGranted) {
                                dispatchTakePicture.onDispatchCamera { image ->
                                    bitmap = image
                                }
                            } else {
                                cameraPermissionState.launchPermissionRequest()
                            }
                        },
                        onDetectLabel = {
                            bitmap?.let {
                                extractLabels(it) { label ->
                                    text = label
                                }
                            }
                        },
                        onExtractText = {
                            bitmap?.let {
                                extractText(it) { label ->
                                    text = label
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.size(30.dp))
                    Details(text)
                }
            }
        }
    }

    private fun extractLabels(bitmap: Bitmap, onChangeText: (String) -> Unit) {
        try {

            val image = getImageUri(this, bitmap)?.let { InputImage.fromFilePath(this, it) }
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            if (image != null) {
                labeler.process(image)
                    .addOnSuccessListener { labels ->
                        val labelsString = StringBuilder()
                        for (label in labels) {
                            labelsString.append(
                                "${label.text}  ->  ${
                                    String.format(
                                        "%.1f",
                                        label.confidence * 100
                                    )
                                }%\n"
                            )
                        }
                        onChangeText(labelsString.toString())
                    }
                    .addOnFailureListener { e ->
                        Log.i(TAG, "Error: $e")
                    }
            }
        } catch (e: Error) {
            Log.i(TAG, "Error: $e")
        }
    }

    private fun extractText(bitmap: Bitmap, onChangeText: (String) -> Unit) {
        try {
            val image = getImageUri(this, bitmap)?.let { InputImage.fromFilePath(this, it) }
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            if (image != null) {
                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        onChangeText(visionText.text)
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error extracting text: $e")
                    }
            }
        } catch (e: Error) {
            Log.e(TAG, "Error: $e")
        }
    }
}













