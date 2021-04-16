package com.example.banktest.presentation.main

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banktest.utils.ViewState
import com.example.banktest.utils.getViewStateFlowForNetworkCall
import com.example.domain.User
import com.example.usecase.GetUsersUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val getUsersUseCase: GetUsersUseCase

) : ViewModel(){

    var navigatingFromDetails = false
    private val userListCache = ArrayList<User>()

    object Constants{
        const val LIMIT_USERS = 10
    }

    val usersList : MutableLiveData<ViewState<List<User>>> by lazy {
        MutableLiveData<ViewState<List<User>>>()
    }

    fun getUsers(quantity : Int = Constants.LIMIT_USERS){
       if (navigatingFromDetails) {
           usersList.value = ViewState.RenderSuccess(userListCache)
           return
       }

       viewModelScope.launch {
           getViewStateFlowForNetworkCall {
               getUsersUseCase.getUsers(quantity = quantity)
           }.collect {
               when (it){
                   is ViewState.Loading -> usersList.value = it
                   is ViewState.RenderFailure -> usersList.value = it
                   is ViewState.RenderSuccess ->{
                       usersList.value = ViewState.RenderSuccess(it.output)
                       userListCache.addAll(it.output)
                   }
               }
           }
       }
   }

}