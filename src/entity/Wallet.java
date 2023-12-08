package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Wallet {

    private final UUID id;
    private UUID userId;
    private double finalBalance;
    private List<Transaction> transactions;

    public Wallet(UUID userId, double finalBalance) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.finalBalance = finalBalance;
        this.transactions = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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
