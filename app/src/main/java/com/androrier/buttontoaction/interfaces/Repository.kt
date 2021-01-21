package com.androrier.buttontoaction.interfaces

import android.util.Log
import androidx.lifecycle.LiveData
import com.androrier.buttontoaction.model.MyAction

interface Repository {

    suspend fun getAll() : ArrayList<MyAction>
    suspend fun insertAll(list: List<MyAction>)

    suspend fun insert(myAction: MyAction)
    suspend fun update (myAction: MyAction)

    suspend fun getById(id : Long): MyAction


}