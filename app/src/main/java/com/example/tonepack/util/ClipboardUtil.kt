package com.example.tonepack.util

object ClipboardUtil {
    fun copyToClipboard(context: android.content.Context, text: String) {
        val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("TonePack Copy", text)
        clipboard.setPrimaryClip(clip)
    }
}