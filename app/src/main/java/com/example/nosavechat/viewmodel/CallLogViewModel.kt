package com.example.nosavechat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nosavechat.model.CallLogItem
import com.example.nosavechat.repository.CallLogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel to handle call log data
 */
class CallLogViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CallLogRepository(application)
    
    // LiveData to observe call logs
    private val _callLogs = MutableLiveData<List<CallLogItem>>()
    val callLogs: LiveData<List<CallLogItem>> = _callLogs
    
    // LiveData to track loading state
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    // LiveData to track errors
    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error
    
    /**
     * Loads call logs from the repository
     */
    fun loadCallLogs() {
        _isLoading.value = true
        _error.value = null
        
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val logs = repository.getRecentCalls()
                _callLogs.postValue(logs)
            } catch (e: Exception) {
                _error.postValue("Error loading call logs: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}