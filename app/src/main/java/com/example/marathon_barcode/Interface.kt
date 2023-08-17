package com.example.marathon_barcode

import androidx.lifecycle.viewmodel.compose.viewModel
import android.Manifest
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.marathon_barcode.ui.CaptureScreenRoot
import com.example.marathon_barcode.ui.LandingScreenRoot
import com.example.marathon_barcode.ui.StatusScreenRoot
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

enum class BarCodeScreen {
    Home,
    Capture,
    Status
}

// ExperimentalMaterial3Api for Scaffold
// androidx.camera.core.ExperimentalGetImage for camerax, CaptureScreenRoot
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
@Composable
fun HomeRoot(
    viewModel: HomeViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    Scaffold {
        val appState by viewModel.appState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = BarCodeScreen.Home.name,
            modifier = Modifier.padding(it)
        ) {
            composable(route = BarCodeScreen.Home.name) {
                val permissionState = rememberPermissionState(Manifest.permission.CAMERA)

                LandingScreenRoot(
                    onCaptureBarCodeClicked = {
                        when {
                            permissionState.status.isGranted -> {
                                navController.navigate(BarCodeScreen.Capture.name)
                            }

                            permissionState.status.shouldShowRationale -> {
                                permissionState.launchPermissionRequest()
                            }

                            else -> {
                                permissionState.launchPermissionRequest()
                            }
                        }
                    }
                )
            }
            composable(route = BarCodeScreen.Capture.name) {
                CaptureScreenRoot (
                    viewModel = viewModel,
                    onBarCodeCaptured = { rawString ->
                        viewModel.setBarcodeData(rawString)
                        navController.navigate(BarCodeScreen.Status.name)
                    }
                )
            }
            composable(route = BarCodeScreen.Status.name) {
                StatusScreenRoot(
                    appState.lastBarCodeData,
                    appState.barcodeHP,
                    appState.barcodeAttack,
                    appState.barcodeDefence,
                    onBackToHomeClicked = {
                        navController.navigate(BarCodeScreen.Home.name)
                    }
                )
            }
        }
    }
}