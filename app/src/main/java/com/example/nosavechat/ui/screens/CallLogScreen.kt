package com.example.nosavechat.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.nosavechat.ui.components.CallLogItemCard
import com.example.nosavechat.util.WhatsAppUtil
import com.example.nosavechat.viewmodel.CallLogViewModel

/**
 * Screen to display call logs
 */
@Composable
fun CallLogScreen(viewModel: CallLogViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val callLogs by viewModel.callLogs.observeAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val error by viewModel.error.observeAsState(initial = null)

    // Permission state
    var hasCallLogPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCallLogPermission = isGranted
        if (isGranted) {
            viewModel.loadCallLogs()
        }
    }

    // Request permission and load call logs when the screen is first displayed
    LaunchedEffect(hasCallLogPermission) {
        if (!hasCallLogPermission) {
            permissionLauncher.launch(Manifest.permission.READ_CALL_LOG)
        } else {
            viewModel.loadCallLogs()
        }
    }

    // Clean up when the screen is disposed
    DisposableEffect(Unit) {
        onDispose { }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            // Show loading indicator
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Show error message
            error != null -> {
                Text(
                    text = error ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }

            // Show permission denied message
            !hasCallLogPermission -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Permission to access call logs is required",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            // Show empty state
            callLogs.isEmpty() -> {
                Text(
                    text = "No call logs found",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Show call logs
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(callLogs) { callLog ->
                        CallLogItemCard(
                            callLogItem = callLog,
                            onWhatsAppClick = { phoneNumber ->
                                WhatsAppUtil.openWhatsAppChat(context, phoneNumber)
                            }
                        )
                    }
                }
            }
        }
    }
}
