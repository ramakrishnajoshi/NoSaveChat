package com.example.nosavechat.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.nosavechat.model.CallLogItem

@Composable
fun CallLogItemCard(
    callLogItem: CallLogItem,
    onWhatsAppClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Contact icon
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Contact",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Call details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Display name if available, otherwise show number
                Text(
                    text = callLogItem.name ?: callLogItem.number,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Call type and date
                Text(
                    text = "${callLogItem.callTypeString} • ${callLogItem.formattedDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // Call duration
                val minutes = callLogItem.duration / 60
                val seconds = callLogItem.duration % 60
                val durationText = if (callLogItem.duration > 0) {
                    "Duration: ${if (minutes > 0) "$minutes min " else ""}${seconds} sec"
                } else {
                    ""
                }
                
                if (durationText.isNotEmpty()) {
                    Text(
                        text = durationText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // WhatsApp button
            IconButton(
                onClick = { onWhatsAppClick(callLogItem.number) }
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Open WhatsApp",
                    tint = Color(0xFF25D366) // WhatsApp green color
                )
            }
        }
    }
}