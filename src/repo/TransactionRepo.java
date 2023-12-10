package repo;

import entity.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TransactionRepo {
    private Map<UUID, Transaction> transactions = new HashMap<>();

    public void addTransaction(Transaction transaction) throws Exception {
        if (transactions.containsKey(transaction.getId())) throw new Exception("Transaction already exists");
        transactions.put(transaction.getId(), transaction);
    }

    public Transaction getTransaction(UUID id) throws Exception {
        if (!transactions.containsKey(id)) throw new Exception("Transaction does not exist");
        return transactions.get(id);
    }
}
