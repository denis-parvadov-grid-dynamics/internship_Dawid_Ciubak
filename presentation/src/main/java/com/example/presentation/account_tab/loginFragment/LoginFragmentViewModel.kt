package com.example.presentation.account_tab.loginFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.common.LoginCredentialsWrapper
import com.example.domain.model.User
import com.example.domain.repository.AppDatabaseRepository
import com.example.domain.repository.FakeStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    private val AppDatabaseRepository: AppDatabaseRepository,
    private val ApiRepository: FakeStoreRepository
) : ViewModel() {

    val userList = MutableLiveData<User>()
    private val compositeDisposable = CompositeDisposable()

    init {
        checkForLoggedInUsers()
    }

    private fun checkForLoggedInUsers() {
        AppDatabaseRepository
            .checkForLoggedUser()
            .subscribeOn(Schedulers.io())
            .subscribe({ userList.postValue(it) }, {})
            .let { compositeDisposable.add(it) }
    }

    fun checkForUserWithMatchingCredentials(loginCredentials: LoginCredentialsWrapper) {
        AppDatabaseRepository
            .getUserWithMatchingCredentials(loginCredentials.username, loginCredentials.password)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { user ->
                    makeLoginApiCall(user)
                },
                {}
            )
            .let { compositeDisposable.add(it) }
    }

    private fun updateUser(user: User, token: String) {
        val updatedUser = user.copy(token = token)
        AppDatabaseRepository.updateUser(updatedUser)
            .subscribeOn(Schedulers.io())
            .subscribe({
                userList.postValue(updatedUser)
            }, {}).let { compositeDisposable.add(it) }
    }

    private fun makeLoginApiCall(user: User) {
        ApiRepository.loginUser(LoginCredentialsWrapper(user.username, user.password))
            .subscribeOn(Schedulers.io())
            .subscribe(
                { apiResponse -> updateUser(user, apiResponse.token) },
                { updateUser(user, "customToken") }
            )
            .let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}
