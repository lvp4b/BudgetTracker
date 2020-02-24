package edu.umkc.lvp4b.budgettracker.ui.transaction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

public class TransactionViewModel extends ViewModel {

    private final MutableLiveData<String> name = new MutableLiveData<>();

    private final MutableLiveData<Date> date = new MutableLiveData<>();

    private final MutableLiveData<Double> amount = new MutableLiveData<>();

    public LiveData<String> getName() {
        return name;
    }

    public LiveData<Date> getDate() { return date; }

    public LiveData<Double> getAmount() { return amount; }
}
