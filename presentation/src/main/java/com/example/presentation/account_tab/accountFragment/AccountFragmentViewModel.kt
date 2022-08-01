package com.example.presentation.account_tab.accountFragment

import androidx.lifecycle.ViewModel
import com.example.domain.repository.AppDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class AccountFragmentViewModel @Inject constructor(
    private val appDatabaseRepository: AppDatabaseRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}
