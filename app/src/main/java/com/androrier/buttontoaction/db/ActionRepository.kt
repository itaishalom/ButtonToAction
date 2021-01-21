package com.androrier.buttontoaction.db

import android.util.Log
import androidx.lifecycle.LiveData
import com.androrier.buttontoaction.interfaces.Repository
import com.androrier.buttontoaction.managers.AppSharedPrefsManeger
import com.androrier.buttontoaction.managers.ResourceManager
import com.androrier.buttontoaction.model.MyAction
import com.androrier.buttontoaction.network.ApiHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.lang.Exception
import javax.inject.Inject

open class ActionRepository @Inject constructor(
    private val actionDao: MyActionDao,
    private val apiHelper: ApiHelper,
    private val resourceManager: ResourceManager,
    private val appSharedPrefsManager: AppSharedPrefsManeger,
) : Repository {
    val TAG = "ActionRepository"
    override suspend fun getAll(): ArrayList<MyAction> {
        if (appSharedPrefsManager.getIsFirstAppRun()) {
            appSharedPrefsManager.cancelFirstAppRun()
            return this.getData()
        } else {
            return actionDao.getAllActions() as ArrayList<MyAction>
        }
    }

    override suspend fun insertAll(list: List<MyAction>) {
        actionDao.insertAll(*list.toTypedArray())
        Log.d("TAG", "Done inserting")
    }

    override suspend fun insert(myAction: MyAction) {
        actionDao.insertAction(myAction)
    }

    override suspend fun update(myAction: MyAction) {
        actionDao.update(myAction)
    }

    override suspend fun getById(id: Long): MyAction {
        return actionDao.getById(id)
    }

    /**
     * Querying remote server for config file. Reads from local in case remote server is down
     *
     * @return Array list of actions read from config file
     */
    private suspend fun getData(): ArrayList<MyAction> {
        var arr: ArrayList<MyAction>
        try {
            arr = (apiHelper.getActions()).body() as ArrayList<MyAction>
            Log.i(TAG, "Remote Server was found")
        } catch (e: Exception) {
            Log.e(TAG, "No remote DB, loading Json from local")
            arr = parseJsonToObjects(resourceManager.loadJSONFromAsset())
        }
        insertAll(arr)
        Log.d("Tag", "Done insertAll get Data")
        return actionDao.getAllActions() as ArrayList<MyAction>
    }

    /**
     * Parses json array to list of actions
     *
     * @param jsonArray - json array of jsons MyAction
     * @return array list of MyAction
     */
    private fun parseJsonToObjects(jsonArray: JSONArray): ArrayList<MyAction> {
        val gson = Gson()
        val sType = object : TypeToken<ArrayList<MyAction>>() {}.type
        val list = gson.fromJson<ArrayList<MyAction>>(jsonArray.toString(), sType)
        return list
    }
}
