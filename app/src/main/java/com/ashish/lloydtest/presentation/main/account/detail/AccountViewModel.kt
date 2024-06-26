package com.ashish.lloydtest.presentation.main.account.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashish.lloydtest.business.domain.util.StateMessage
import com.ashish.lloydtest.business.domain.util.UIComponentType
import com.ashish.lloydtest.business.domain.util.doesMessageAlreadyExistInQueue
import com.ashish.lloydtest.business.interactors.account.GetAccount
import com.ashish.lloydtest.presentation.session.SessionEvents
import com.ashish.lloydtest.presentation.session.SessionManager

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AccountViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val getAccount: GetAccount,
) : ViewModel() {

    private val TAG: String = "AppDebug"

    val state: MutableLiveData<AccountState> = MutableLiveData(AccountState())

    fun onTriggerEvent(event: AccountEvents) {
        when (event) {
            is AccountEvents.GetAccount -> {
                getAccount()
            }
            is AccountEvents.Logout -> {
                logout()
            }
            is AccountEvents.OnRemoveHeadFromQueue -> {
                removeHeadFromQueue()
            }
        }
    }

    private fun removeHeadFromQueue() {
        state.value?.let { state ->
            try {
                val queue = state.queue
                queue.remove() // can throw exception if empty
                this.state.value = state.copy(queue = queue)
            } catch (e: Exception) {
                Log.d(TAG, "removeHeadFromQueue: Nothing to remove from DialogQueue")
            }
        }
    }

    private fun appendToMessageQueue(stateMessage: StateMessage){
        state.value?.let { state ->
            val queue = state.queue
            if(!stateMessage.doesMessageAlreadyExistInQueue(queue = queue)){
                if(!(stateMessage.response.uiComponentType is UIComponentType.None)){
                    queue.add(stateMessage)
                    this.state.value = state.copy(queue = queue)
                }
            }
        }
    }

    private fun getAccount() {
        state.value?.let { state ->
            getAccount.execute(
                authToken = sessionManager.state.value?.authToken,
            ).onEach { dataState ->
                this.state.value = state.copy(isLoading = dataState.isLoading)

                dataState.data?.let { account ->
                    this.state.value = state.copy(account = account)
                }

                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }

            }.launchIn(viewModelScope)
        }
    }

    private fun logout() {
        sessionManager.onTriggerEvent(SessionEvents.Logout)
    }

}















