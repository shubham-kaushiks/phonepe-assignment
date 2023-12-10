package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Wallet {

    private final UUID id;
    private double finalBalance;
    private List<Transaction> transactions;

    public Wallet() {
        this.id = UUID.randomUUID();
        this.finalBalance = 0.0;
        this.transactions = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public double getFinalBalance() {
        return finalBalance;
    }

    public void setFinalBalance(double finalBalance) {
        this.finalBalance = finalBalance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
}
