package com.example.presentation.account_tab.accountFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.user_related.User
import com.example.domain.repository.AppDatabaseRepository
import com.example.domain.repository.FakeStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class AccountFragmentViewModel @Inject constructor(
    private val appDatabaseRepository: AppDatabaseRepository,
    private val apiRepository: FakeStoreRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val observableUserLiveData = MutableLiveData<Result<User>>()

    init {
        getUserInfoFromDatabase()
    }

    private fun getUserInfoFromDatabase() {
        appDatabaseRepository
            .checkForLoggedUser()
            .subscribeOn(Schedulers.io())
            .subscribe({
                observableUserLiveData.postValue(Result.success(it))
            }, {}).let { compositeDisposable.add(it) }
    }

    fun logoutUser() {
        // deletes one record, so we don't need to wait for the result, because It will take
        // close to none time to finish that action (1ms)
        // but we still need to do it in a coroutine, hence the .subscribe({}, {})
        appDatabaseRepository
            .deleteAllUserRecords()
            .subscribeOn(Schedulers.io())
            .subscribe({}, {})
            .let { compositeDisposable.add(it) }
    }

    fun updateUser(user: User) {
        apiRepository.updateUser(user.toUpdateUser())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    saveUpdatedUserInTheDatabase(user)
                },
                {
                    observableUserLiveData.postValue(Result.failure(Throwable(it.message)))
                }
            ).let { compositeDisposable.add(it) }
    }

    private fun saveUpdatedUserInTheDatabase(user: User) {
        appDatabaseRepository.updateUser(user)
            .subscribeOn(Schedulers.io())
            .subscribe({ observableUserLiveData.postValue(Result.success(user)) }, {})
            .let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}
