package com.androrier.buttontoaction.model

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.random.Random.Default.nextBoolean

@Entity(tableName = "actions")
class MyAction(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @SerializedName("type")
    @ColumnInfo(name = "type") var type: String = "",
    @SerializedName("enabled")
    @ColumnInfo(name = "enabled") var enabled: Boolean = false,
    @SerializedName("priority")
    @ColumnInfo(name = "priority") var priority: Int = 0,
    @SerializedName("valid_days")
    @ColumnInfo(name = "valid_days") var validDays: HashSet<Int>? = null,
    @SerializedName("cool_down")
    @ColumnInfo(name = "cool_down") var coolDown: Long = 0,
    @ColumnInfo(name = "last_action") var lastAction: Long = 0
) : Comparable<MyAction>{
    fun setLastAction() {
        lastAction = Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis
    }

    fun isActionable(): Boolean {
        val today = Calendar.getInstance(TimeZone.getTimeZone("UTC")).get(Calendar.DAY_OF_WEEK)
        val nowMills = Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis
        if (enabled && validDays?.contains(today - 1)!! && nowMills - lastAction > coolDown){
            return true
        }
        return false
    }

    override fun compareTo(o: MyAction): Int {
        if(o.priority == this.priority){
            return if(nextBoolean()){
                1
            }else{
                -1;
            }
        }
        return if (o.priority > this.priority) 1 else -1
    }

}