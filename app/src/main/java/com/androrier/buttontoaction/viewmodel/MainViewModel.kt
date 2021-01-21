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
import com.androrier.buttontoaction.interfaces.OnActionSelectedListener
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
            dataLoaded = true
            Log.i(TAG, "Data loaded")
            appReady.set(actionListenerSet && dataLoaded)
        }
    }

    private val TAG = "MainViewModel"
    var appReady: ObservableField<Boolean> = ObservableField(false)
    val noActionFound: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var allActions: TreeSet<MyAction>
    lateinit var onActionSelectedListener: OnActionSelectedListener //OnActionSelected is a listener
    var dataLoaded = false
    var actionListenerSet = false

    /**
     * Initialise the viewModel class with the listener
     *
     * @param onActionSelectedListener
     */
    fun initViewModel(onActionSelectedListener: OnActionSelectedListener) {
        this.onActionSelectedListener = onActionSelectedListener
        Log.i(TAG, "Event listener set")
        actionListenerSet = true
        appReady.set(actionListenerSet && dataLoaded)
    }


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
                    when (action.type) {
                        "animation" -> onActionSelectedListener.onAnimationAction()
                        "toast" -> onActionSelectedListener.onToastAction()
                        "call" -> onActionSelectedListener.onCallAction()
                        "notification" -> onActionSelectedListener.onNotificationAction()
                    }
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