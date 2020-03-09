package edu.umkc.lvp4b.budgettracker.ui.transaction;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.umkc.lvp4b.budgettracker.data.AppDatabase;
import edu.umkc.lvp4b.budgettracker.data.TransactionEntity;

public class TransactionsViewModel extends AndroidViewModel {

    private MutableLiveData<List<Transaction>> transactions = new MutableLiveData<>();
    private final AppDatabase database;
    private Transaction selectedTransaction;

    public TransactionsViewModel(Application application){
        super(application);
        transactions.setValue(new ArrayList<>());
        database = AppDatabase.getInstance(application.getApplicationContext());
        new Init(this, database).execute();
    }

    private static class Init extends AsyncTask<Object, Object, Void>{
        private final TransactionsViewModel transactionsViewModel;
        private final AppDatabase database;

        private Init(TransactionsViewModel transactionsViewModel, AppDatabase database) {
            this.transactionsViewModel = transactionsViewModel;
            this.database = database;
        }

        @Override
        protected Void doInBackground(Object... objects) {
            transactionsViewModel.setTransactions(database.transactionDao().getAll().stream()
                    .map(Transaction::fromEntity).collect(Collectors.toList()));
            return null;
        }
    }

    public LiveData<List<Transaction>> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> txns){
        transactions.postValue(txns);
    }

    public Transaction getSelectedTransaction(){
        return selectedTransaction;
    }

    public void setSelectedTransaction(Transaction selectedTransaction){
        this.selectedTransaction = selectedTransaction;
    }

    public void updateTransaction(Transaction transaction){
        new UpdateTask(database).execute(transaction);
    }

    private static class UpdateTask extends AsyncTask<Transaction, Void, Void> {
        private final AppDatabase database;

        private UpdateTask(AppDatabase database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(Transaction... transactions) {
            for (Transaction transaction : transactions) {
                database.transactionDao().delete(database.transactionDao().getAllLineItems(transaction.getId()));
                database.transactionDao().insert(transaction.toLineItemEntities());
                database.transactionDao().update(transaction.toEntity());
            }
            return null;
        }
    }

    public int deleteTransaction(Transaction transaction){
        final List<Transaction> txns = new ArrayList<>(transactions.getValue());
        final int position = txns.indexOf(transaction);
        txns.remove(transaction);
        new DeleteTask(database).execute(transaction);
        transactions.setValue(txns);
        return position;
    }

    private static class DeleteTask extends AsyncTask<Transaction, Void, Void> {
        private final AppDatabase database;

        private DeleteTask(AppDatabase database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(Transaction... transactions) {
            for (Transaction transaction : transactions) {
                database.transactionDao().delete(transaction.toLineItemEntities());
                database.transactionDao().delete(transaction.toEntity());
            }
            return null;
        }
    }

    public void addTransaction(Transaction transaction) {
        final List<Transaction> txns = new ArrayList<>(transactions.getValue());
        txns.add(transaction);
        new InsertTask(database, transaction).execute(transaction);
        transactions.setValue(txns);
    }

    private static class InsertTask extends AsyncTask<Transaction, Void, TransactionEntity> {
        private final AppDatabase database;
        private final Transaction transaction;

        private InsertTask(AppDatabase database, Transaction transaction) {
            this.database = database;
            this.transaction = transaction;
        }

        @Override
        protected TransactionEntity doInBackground(Transaction... transactions) {
            TransactionEntity entity = transactions[0].toEntity();
            database.transactionDao().insert(entity);
            database.transactionDao().insert(transactions[0].toLineItemEntities());
            return entity;
        }

        @Override
        protected void onPostExecute(TransactionEntity entity) {
            transaction.setId(entity.id);
        }
    }
}
