package edu.umkc.lvp4b.budgettracker.ui.transaction;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

public class Transaction extends BaseObservable {
    private Date date;
    private String name;
    private double amount;

    public Transaction(){
        this.date = Date.from(Instant.now());
        this.name = "";
        this.amount = 0;
    }

    public Transaction(Date date, String name, double amount) {
        this.date = date;
        this.name = name;
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    @Bindable
    public String getDateAsString() {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    @Bindable
    public String getAmountAsString() {
        return NumberFormat.getNumberInstance().format(amount);
    }

    public void setDateAsString(String date){
        try {
            this.date = DateFormat.getDateInstance().parse(date);
            notifyPropertyChanged(BR.dateAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setName(String name){
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public void setAmountAsString(String amount){
        try {
            this.amount = NumberFormat.getNumberInstance().parse(amount.replaceAll("\\.$", "")).doubleValue();
            notifyPropertyChanged(BR.amountAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
