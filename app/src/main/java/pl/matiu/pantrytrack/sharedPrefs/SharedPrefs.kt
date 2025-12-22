package pl.matiu.pantrytrack.sharedPrefs

import android.content.Context

class SharedPrefs {

    fun saveType(context: Context, type: String) {
        val prefs = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        prefs.edit().putString("type", type).apply()
    }

    fun readType(context: Context): String? {
        val prefs = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        return prefs.getString("type", null)
    }
}