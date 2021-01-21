
package com.androrier.buttontoaction.repository

import com.androrier.buttontoaction.interfaces.Repository
import com.androrier.buttontoaction.managers.ResourceManager
import com.androrier.buttontoaction.model.MyAction
import javax.inject.Inject

class FakeActionRepository @Inject constructor(
) : Repository {

    val obj1 = MyAction(1,"toast",true,10, hashSetOf(0,1, 2, 3,4,5,6), 1000000 )
    val obj2 = MyAction(1,"notification",true,5, hashSetOf(0,1, 2, 3,4,5,6), 1000000 )
    val arr = arrayListOf(obj1, obj2)

    override suspend fun getAll() = arr
    override suspend fun insertAll(list: List<MyAction>) {

    }

    override suspend fun insert(myAction: MyAction) {
    }

    override suspend fun update (myAction: MyAction){
    }

    override suspend fun getById(id : Long): MyAction {
        return obj1
    }

}
