package com.androrier.buttontoaction.viewmodel

import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androrier.buttontoaction.interfaces.Repository
import com.androrier.buttontoaction.model.MyAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    @Assisted private val savedStateHandle: SavedStateHandle?
) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (!::allActions.isInitialized) {
                allActions = TreeSet<MyAction>().apply {
                    addAll(repository.getAll())
                }
            }
            appReady.set(true)
        }
    }

    private val TAG = "MainViewModel"
    var appReady: ObservableField<Boolean> = ObservableField(false)
    val noActionFound: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var allActions: TreeSet<MyAction>
    val actionFound: MutableLiveData<String> = MutableLiveData()




    /**
     * Called from the xml file, where the action button is pressed.
     * activating the next action available. If there is no action available, triggers the live data event
     *
     * @param view the view that was pressed.
     */
    fun onActionButtonPressed(view: View? = null) {
        val action = getNextAction()
        if (action != null) {
            Log.i(TAG, "action valid is : " + action.type)
            action.setLastAction()
            viewModelScope.launch(Dispatchers.IO) {
                repository.update(action)
                withContext(Dispatchers.Main) {
                    actionFound.postValue(action.type)
                }
            }
            return
        } else {
            Log.i(TAG, "no valid action")
            noActionFound.postValue(true)
        }

    }

    /**
     * Return the next action available
     *
     * @return the next action available
     */
    fun getNextAction(): MyAction? {
        val iter = allActions.iterator()
        while (iter.hasNext()) {
            val action = iter.next()
            if (action.isActionable()) {
                return action;
            }
        }
        return null;
    }
}