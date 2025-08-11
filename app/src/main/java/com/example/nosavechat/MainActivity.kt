package com.example.nosavechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.nosavechat.ui.screens.CallLogScreen
import com.example.nosavechat.ui.theme.noSaveChatTheme
import com.example.nosavechat.viewmodel.CallLogViewModel

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    // Initialize ViewModel
    private val viewModel: CallLogViewModel by viewModels()
    
    // Lifecycle observer to detect app foreground/background state
    private val lifecycleObserver = object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            // App came to foreground, refresh call logs
            viewModel.refreshCallLogs()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Register lifecycle observer to detect when app comes to foreground
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)
        
        enableEdgeToEdge()
        setContent {
            noSaveChatTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("Recent Calls") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                ) { innerPadding ->
                    // Display the call log screen
                    CallLogScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Remove lifecycle observer to prevent memory leaks
        ProcessLifecycleOwner.get().lifecycle.removeObserver(lifecycleObserver)
    }
}
