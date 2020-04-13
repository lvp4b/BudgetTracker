package edu.umkc.lvp4b.budgettracker.ui.categories;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
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

import edu.umkc.lvp4b.budgettracker.data.CategoryEntity;
import edu.umkc.lvp4b.budgettracker.data.ImageSerializer;
import edu.umkc.lvp4b.budgettracker.data.LineItemEntity;
import edu.umkc.lvp4b.budgettracker.data.TransactionAndItems;
import edu.umkc.lvp4b.budgettracker.data.TransactionEntity;
import edu.umkc.lvp4b.budgettracker.ui.transaction.LineItem;

public class Category extends BaseObservable {
    private int id;
    private String name;
    private boolean income;

    public static final Category UNASSIGNED = new Category(0, "(Unassigned)", false);

    public Category(){
        this.id = 0;
        this.name = "";
    }

    public Category(int id, String name, boolean income) {
        this.id = id;
        this.name = name;
        this.income = income;
    }

    public int getId() { return id; }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public static Category fromEntity(CategoryEntity entity) {
        return new Category(entity.id, entity.name, entity.income);
    }

    public CategoryEntity toEntity(){
        CategoryEntity entity = new CategoryEntity();
        entity.id = id;
        entity.name = name;
        entity.income = income;
        return entity;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Bindable
    public boolean isIncome() { return income; }

    public void setIncome(boolean income) {
        this.income = income;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
