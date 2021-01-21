package com.androrier.buttontoaction.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androrier.buttontoaction.model.MyAction

@Dao
interface MyActionDao {

    @Query("SELECT * FROM actions")
    fun getAllActions(): List<MyAction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg actions: MyAction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAction(action: MyAction)

    @Update
    fun update(action: MyAction)

    @Query("SELECT * FROM actions where id =:id")
    fun getById(id: Long) : MyAction

}