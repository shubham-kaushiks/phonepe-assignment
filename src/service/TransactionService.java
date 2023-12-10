package service;

import entity.Transaction;
import repo.TransactionRepo;

public class TransactionService {

    private TransactionRepo transactionRepo;

    public TransactionService(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public void addTransaction(Transaction transaction) throws Exception {
        transactionRepo.addTransaction(transaction);
    }

}
