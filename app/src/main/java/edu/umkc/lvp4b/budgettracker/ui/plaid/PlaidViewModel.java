package edu.umkc.lvp4b.budgettracker.ui.plaid;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import edu.umkc.lvp4b.budgettracker.ui.transaction.LineItem;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaidViewModel extends AndroidViewModel {
    private MutableLiveData<List<LineItem>> lineItems = new MutableLiveData<>();

    private List<PlaidApi.Transaction> txns;

    public PlaidViewModel(Application application){
        super(application);
        lineItems.setValue(new ArrayList<>());
        new Init().execute();
    }

    private class Init extends AsyncTask<Object, Object, Void>{
        @Override
        protected Void doInBackground(Object... objects) {
            Single<PlaidApi.TransactionResponse> single = new PlaidApi.Impl().getTransactions(
                    "access-sandbox-f15e5a1d-6e28-4b6a-ae0b-32bcebe10747", "1990-01-01", "2030-12-31");

            List<PlaidApi.Transaction> transactions = single.blockingGet().transactions;

            List<LineItem> lineItems = new ArrayList<>();
            for (int i = 0; i < transactions.size(); i++) {
                PlaidApi.Transaction txn = transactions.get(i);
                lineItems.add(new LineItem(txn.name, txn.amount, i + 1));
            }

            setLineItems(lineItems);
            transactions.add(0, null);
            txns = transactions;
            return null;
        }
    }

    public LiveData<List<LineItem>> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> values){
        lineItems.postValue(values);
    }

    public List<PlaidApi.Transaction> getTxns() {
        return txns;
    }
}
