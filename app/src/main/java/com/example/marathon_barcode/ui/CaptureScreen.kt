package com.example.marathon_barcode.ui

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.marathon_barcode.HomeViewModel
import java.util.concurrent.Executors


@androidx.camera.core.ExperimentalGetImage
@Composable
fun CaptureScreenRoot(
    viewModel: HomeViewModel,
    onBarCodeCaptured: (String) -> Unit
) {
    CaptureComposable(viewModel, onBarCodeCaptured)
}

@androidx.camera.core.ExperimentalGetImage
@Composable
fun CaptureComposable(
    viewModel: HomeViewModel,
    onBarCodeCaptured: (String) -> Unit
) {
        AndroidView(factory = { context ->
            val cameraExecutor = Executors.newSingleThreadExecutor()
            val previewView = PreviewView(context).also {
                it.scaleType = PreviewView.ScaleType.FILL_CENTER
            }
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            /*LifeCycleOwner*/ context as ComponentActivity,
                            /*CameraSelector*/ CameraSelector.DEFAULT_BACK_CAMERA,
                            /*   UseCases   */ viewModel.getPreview(previewView),
                            /*   UseCases   */ ImageCapture.Builder().build(),
                            /*   UseCases   */ viewModel.getImageAnalyser(cameraExecutor, onBarCodeCaptured)
                        )
                    } catch (e: Exception) {
                        Log.e("Exception", "Binding Failed", e)
                    }
                },
                ContextCompat.getMainExecutor(context)
            )
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}