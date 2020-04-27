package edu.umkc.lvp4b.budgettracker.ui.plaid;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

interface PlaidApi {
        @POST("transactions/get")
        @Headers("Content-Type: application/json")
        Single<TransactionResponse> getTransactions(@Body JsonElement body);

        @POST("item/public_token/exchange")
        @Headers("Content-Type: application/json")
        Call<ExchangeResponse> exchange(@Body JsonElement body);

        static class Impl {
            private final PlaidApi plaidApi;
            private final String CLIENT_ID = "5ea4b426155af90013f2f10d";
            private final String SECRET = "d43f343305ff1e5e53f447b9de859c";

            Impl() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://sandbox.plaid.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();

                plaidApi = retrofit.create(PlaidApi.class);
            }

            Single<TransactionResponse> getTransactions
                    (String accessToken, String startDate, String endDate) {
                Map<String, String> data = new HashMap<>();
                data.put("client_id", CLIENT_ID);
                data.put("secret", SECRET);
                data.put("access_token", accessToken);
                data.put("start_date", startDate);
                data.put("end_date", endDate);
                return plaidApi.getTransactions(new Gson().toJsonTree(data));
            }

            Call<ExchangeResponse> exchange(String publicToken) {
                Map<String, String> data = new HashMap<>();
                data.put("client_id", CLIENT_ID);
                data.put("secret", SECRET);
                data.put("public_token", publicToken);
                return plaidApi.exchange(new Gson().toJsonTree(data));
            }
        }

        static class TransactionResponse {
            public List<Transaction> transactions;
        }

        static class Transaction {
            public double amount;

            public String date;

            public String name;

            @SerializedName("transaction_id")
            public String id;
        }

        static class ExchangeResponse {
            public String access_token;
        }
}
