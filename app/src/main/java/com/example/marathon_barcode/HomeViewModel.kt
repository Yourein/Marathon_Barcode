package com.example.marathon_barcode

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import com.example.marathon_barcode.usecase.CaptureScreenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.BigInteger
import java.security.MessageDigest
import java.util.concurrent.ExecutorService

data class AppState(
    val lastBarCodeData: String = "",
    val barcodeHP: Int = 0,
    val barcodeAttack: Int = 0,
    val barcodeDefence: Int = 0,
)

class HomeViewModel: ViewModel() {
    private val captureScreenUseCase = CaptureScreenUseCase()
    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    fun setBarcodeData(rawValue: String) {
        val hashBytes = MessageDigest.getInstance("SHA256").digest(rawValue.toByteArray())
        val no = BigInteger(1, hashBytes)
        Log.i("BigInt", no.toString())
        var hashtext = no.toString(16).uppercase()

        while(hashtext.length < 32) {
            hashtext = "1$hashtext"
        }

        val hpPreValue = Integer.decode(("0x" + hashtext.substring(0, 5))) % 256
        val attackPreValue = Integer.decode(("0x" + hashtext.substring(10, 15))) % 256
        val defencePreValue = Integer.decode("0x" + hashtext.substring(20, 25)) % 256

        _appState.update { currentState ->
            currentState.copy(
                barcodeHP = if (hpPreValue == 0) 1 else hpPreValue,
                barcodeAttack = if (attackPreValue == 0) 1 else attackPreValue,
                barcodeDefence =  if (defencePreValue == 0) 1 else defencePreValue,
            )
        }
    }

    fun getPreview(previewView: PreviewView): Preview {
        return captureScreenUseCase.getPreview(previewView)
    }

    fun getImageAnalyser(
        executor: ExecutorService,
        onBarCodeCaptured: (String) -> Unit
    ): ImageAnalysis {
        return captureScreenUseCase.getImageAnalyser(executor, onBarCodeCaptured)
    }
}