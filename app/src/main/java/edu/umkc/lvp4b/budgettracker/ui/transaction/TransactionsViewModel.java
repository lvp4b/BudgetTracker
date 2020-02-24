package edu.umkc.lvp4b.budgettracker.ui.transaction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class TransactionsViewModel extends ViewModel {

    private MutableLiveData<List<Transaction>> transactions = new MutableLiveData<>();

    public TransactionsViewModel(){
        transactions.setValue(new ArrayList<Transaction>());
    }

    public LiveData<List<Transaction>> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        final List<Transaction> txns = new ArrayList<>(transactions.getValue());
        txns.add(transaction);
        transactions.setValue(txns);
    }
}
