package com.androrier.buttontoaction.managers

import android.content.Context
import android.content.res.AssetManager
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceManager @Inject constructor(@ApplicationContext context : Context){
    private val assets: AssetManager = context.assets

    fun loadJSONFromAsset(): JSONArray {
        var json = ""
        json = try {
            val `is`: InputStream = assets.open("config.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return JSONArray()
        }
        return JSONArray(json)
    }

}
