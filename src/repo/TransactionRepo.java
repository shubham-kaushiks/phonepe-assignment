package repo;

import entity.Transaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TransactionRepo {
    private Map<UUID, Transaction> transactions = new HashMap<>();

    private Map<UUID, Set<UUID>> transactionsByUser = new HashMap<>();

    public void addTransaction(Transaction transaction) throws Exception {
        if (transactions.containsKey(transaction.getId())) throw new Exception("Transaction already exists");
        transactions.put(transaction.getId(), transaction);
    }

    public void addUserTransaction(UUID userId, UUID transactionId) throws Exception {
        if (transactionsByUser.getOrDefault(userId, new HashSet<>()).contains(transactionId)) throw new Exception("User has a identical transaction Id");
        Set<UUID> transactionsByCurrentUser = transactionsByUser.getOrDefault(userId, new HashSet<>());
        transactionsByCurrentUser.add(transactionId);
        transactionsByUser.put(userId, transactionsByCurrentUser);
    }
}
