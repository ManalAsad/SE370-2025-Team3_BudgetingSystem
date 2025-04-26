package org.project.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.project.models.Transaction;

public class ManualHandler
{
    private static ObservableList<Transaction> transactionData = FXCollections.observableArrayList();

    public void addTransaction(Transaction transaction)
    {
        transactionData.add(transaction);
    }
    public ObservableList<Transaction> getTransactionData() {
        return transactionData;
    }

    public Transaction getTransaction(int index)
    {
        if (!transactionData.isEmpty() && index >= 0)
            return transactionData.get(index);

        return null;
    }

    public boolean isEmpty()
    {
        return transactionData.isEmpty();
    }

    public int getSize()
    {
        return transactionData.size();
    }
}
