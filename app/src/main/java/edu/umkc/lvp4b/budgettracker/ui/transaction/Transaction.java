package edu.umkc.lvp4b.budgettracker.ui.transaction;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import edu.umkc.lvp4b.budgettracker.data.ImageSerializer;
import edu.umkc.lvp4b.budgettracker.data.LineItemEntity;
import edu.umkc.lvp4b.budgettracker.data.TransactionAndItems;
import edu.umkc.lvp4b.budgettracker.data.TransactionEntity;

public class Transaction extends BaseObservable {
    private int id;
    private Date date;
    private String name;
    private double amount;
    private List<LineItem> lineItems;

    @Nullable
    private Bitmap image;

    public Transaction(){
        this.id = 0;
        this.date = Date.from(Instant.now());
        this.name = "";
        this.amount = 0;
        this.lineItems = new ArrayList<>();
    }

    public Transaction(int id, Date date, String name, double amount, List<LineItem> lineItems, @Nullable Bitmap image) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.amount = amount;
        this.lineItems = lineItems;
        this.image = image;
    }

    public int getId() { return id; }

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

    public Bitmap getImage() {
        return this.image;
    }

    public void setImage(@Nullable Bitmap image){
        this.image = image;
    }

    public static Transaction fromEntity(TransactionAndItems e) {
        TransactionEntity entity = e.transaction;
        return new Transaction(entity.id, new Date(entity.date), entity.name, entity.amount,
                e.lineItems.stream().sorted(Comparator.comparingInt(LineItemEntity::getOrder))
                .map(LineItem::fromEntity).collect(Collectors.toList()),
                ImageSerializer.deserialize(entity.image));
    }

    public TransactionEntity toEntity(){
        TransactionEntity entity = new TransactionEntity();
        entity.id = id;
        entity.amount = amount;
        entity.date = date.getTime();
        entity.name = name;
        entity.image = ImageSerializer.serialize(image);
        return entity;
    }

    public List<LineItemEntity> toLineItemEntities() {
        List<LineItemEntity> lineItems = new ArrayList<>();
        for (int i = 0; i != this.lineItems.size(); i++){
            LineItem lineItem = this.lineItems.get(i);
            LineItemEntity e = new LineItemEntity();
            e.amount = lineItem.getAmount();
            e.description = lineItem.getDescription();
            e.order = i;
            e.transactionId = id;
            lineItems.add(e);
        }
        return lineItems;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void addLineItem(LineItem lineItem){
        lineItems.add(lineItem);
        notifyPropertyChanged(BR.lineItem);
    }

    public void removeLineItem(int lineItemIndex) {
        lineItems.remove(lineItemIndex);
        notifyPropertyChanged(BR.lineItem);
    }
}
