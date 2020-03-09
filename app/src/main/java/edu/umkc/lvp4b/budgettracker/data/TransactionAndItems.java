package edu.umkc.lvp4b.budgettracker.data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TransactionAndItems {
    @Embedded
    public TransactionEntity transaction;

    @Relation(parentColumn = "id", entityColumn = "transactionId")
    public List<LineItemEntity> lineItems;
}
