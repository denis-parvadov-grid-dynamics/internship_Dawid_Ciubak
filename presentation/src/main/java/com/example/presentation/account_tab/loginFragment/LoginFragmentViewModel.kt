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
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    private val AppDatabaseRepository: AppDatabaseRepository,
    private val ApiRepository: FakeStoreRepository
) : ViewModel() {

    val observableLoggedInUserLiveData = MutableLiveData<Result<User>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        checkForLoggedInUsers()
    }

    private fun checkForLoggedInUsers() {

        AppDatabaseRepository
            .checkForLoggedUser()
            .subscribeOn(Schedulers.io())
            .subscribe({ user ->
                observableLoggedInUserLiveData.postValue(Result.success(user))
            }, {})
            .let { compositeDisposable.add(it) }
    }

    fun loginUser(loginCredentials: LoginCredentialsWrapper) {
        ApiRepository
            .loginUser(loginCredentials)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { apiResponse ->
                    // user logged in successfully (api response: 200)
                    observableLoggedInUserLiveData.postValue(
                        Result.success(
                            User(
                                username = loginCredentials.username,
                                password = loginCredentials.password
                            )
                        )
                    )
                },
                { throwable ->
                    // Error related with the API (connection error or api returned 401)
                    when (throwable) {
                        is HttpException -> {
                            // The api returns 401 error whenever the authentication fails
                            // (password and/or username incorrect)
                            // and because HttpException takes care of error codes 400-500
                            // it also covers the normal api response,
                            // which this check fixes
                            if (throwable.code() == 401) {
                                observableLoggedInUserLiveData.postValue(
                                    Result.failure(
                                        Throwable("The email you entered isn’t connected to an account. Try registering first")
                                    )
                                )
                            } else {
                                observableLoggedInUserLiveData.postValue(Result.failure(throwable))
                            }
                        }
                        is IOException -> {
                            observableLoggedInUserLiveData.postValue(Result.failure(throwable))
                        }
                    }
                }
            )
            .let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}
