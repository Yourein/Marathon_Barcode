package com.example.marathon_barcode.usecase

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import com.example.marathon_barcode.utils.BarcodeAnalyser
import java.util.concurrent.ExecutorService
import javax.inject.Singleton

@Singleton
class CaptureScreenUseCase {
    fun getPreview(previewView: PreviewView): Preview {
        return Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
    }

    fun getImageAnalyser(
        executor: ExecutorService,
        onBarCodeCaptured: (String) -> Unit
    ): ImageAnalysis {
        return ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(
                    executor,
                    BarcodeAnalyser(onBarCodeCaptured)
                )
            }
    }
}