package com.example.presentation.account_tab.registerFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.User
import com.example.domain.repository.AppDatabaseRepository
import com.example.domain.repository.FakeStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class RegisterFragmentViewModel @Inject constructor(
    private val databaseRepository: AppDatabaseRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val observableLoggedInUserLiveData = MutableLiveData<User>()

    fun saveUserInDatabase(user: User) {
        databaseRepository.saveUserInDatabase(user)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { observableLoggedInUserLiveData.postValue(user) }, {}
            ).let {
                compositeDisposable.add(it)
            }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}
