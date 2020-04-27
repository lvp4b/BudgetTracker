package edu.umkc.lvp4b.budgettracker.ui.transaction;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.text.NumberFormat;
import java.text.ParseException;

import edu.umkc.lvp4b.budgettracker.data.LineItemEntity;

public class LineItem extends BaseObservable {
    private String description;
    private double amount;
    private int categoryId;

    public LineItem(){
        description = "";
        amount = 0;
        categoryId = 0;
    }

    public LineItem(String description, double amount, int categoryId){
        this.description = description;
        this.amount = amount;
        this.categoryId = categoryId;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        notifyPropertyChanged(BR.amountAsString);
    }

    @Bindable
    public String getAmountAsString() {
        return NumberFormat.getNumberInstance().format(amount);
    }

    public void setAmountAsString(String amount){
        try {
            this.amount = NumberFormat.getNumberInstance().parse(amount.replaceAll("\\.$", "")).doubleValue();
            notifyPropertyChanged(BR.amountAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public static LineItem fromEntity(LineItemEntity entity){
        return new LineItem(entity.description, entity.amount, entity.categoryId);
    }
}
