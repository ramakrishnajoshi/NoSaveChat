package com.example.nosavechat.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast

/**
 * Utility class for WhatsApp related operations
 */
object WhatsAppUtil {

    /**
     * Opens WhatsApp chat with the given phone number
     * @param context Android context
     * @param phoneNumber Phone number to open chat with
     * @return true if WhatsApp was opened successfully, false otherwise
     */
    /**
     * Checks if WhatsApp is installed on the device
     * @param context Android context
     * @return true if WhatsApp is installed, false otherwise
     */
    private fun isWhatsAppInstalled(context: Context): Boolean {
        val packageManager = context.packageManager
        return try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * Opens WhatsApp chat with the given phone number
     * @param context Android context
     * @param phoneNumber Phone number to open chat with
     * @return true if WhatsApp was opened successfully, false otherwise
     */
    fun openWhatsAppChat(context: Context, phoneNumber: String): Boolean {
        // First check if WhatsApp is installed
        if (!isWhatsAppInstalled(context)) {
            // Show a more helpful error message
            Toast.makeText(
                context,
                "WhatsApp is not installed. Please install WhatsApp to use this feature.",
                Toast.LENGTH_LONG
            ).show()

            // Optionally, open Play Store to download WhatsApp
            try {
                val storeIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.whatsapp"))
                storeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(storeIntent)
            } catch (e: Exception) {
                // If Play Store is not available, open browser
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")
                )
                webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(webIntent)
            }

            return false
        }

        // Format the phone number (remove spaces, dashes, etc.)
        val formattedNumber = phoneNumber.replace(Regex("[\\s-()]"), "")

        // Create the WhatsApp URI
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$formattedNumber")

        // Create the intent
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.whatsapp")

        try {
            context.startActivity(intent)
            return true
        } catch (e: Exception) {
            Toast.makeText(context, "Error opening WhatsApp", Toast.LENGTH_SHORT).show()
            return false
        }
    }
}
