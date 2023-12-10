package service;

import entity.Transaction;
import entity.TransactionType;
import entity.Wallet;
import repo.WalletRepo;
import utils.Constants;
import utils.Validator;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class WalletService {

    private WalletRepo walletRepo;

    private TransactionService transactionService;

    private OfferService offerService;

    public WalletService(WalletRepo walletRepo, TransactionService transactionService, OfferService offerService) {
        this.walletRepo = walletRepo;
        this.transactionService = transactionService;
        this.offerService = offerService;
    }

    public void createWallet(UUID userId) throws Exception {
        Wallet newWallet = new Wallet();
        walletRepo.addWallet(newWallet);
        walletRepo.addWalletOwner(userId, newWallet.getId());
    }

    public void updateWallet(Wallet wallet) throws Exception {
        walletRepo.updateWallet(wallet);
    }

    public Wallet getWalletForUser(UUID userId) throws Exception {
        return walletRepo.getWalletByOwner(userId);
    }

    public void topUpWallet(UUID userId, Double amount) throws Exception {
        Transaction topUp = new Transaction(Constants.BANK_WALLET_ID, userId, amount, TransactionType.TOP_UP,
                LocalDate.now());
        Wallet userWallet = walletRepo.getWalletByOwner(userId);
        userWallet.addTransaction(topUp);
        userWallet.setFinalBalance(userWallet.getFinalBalance() + amount);
        // Persist
        transactionService.addTransaction(topUp);
        walletRepo.updateWallet(userWallet);
    }

    public Double fetchBalance(UUID userId) throws Exception {
        return walletRepo.getWalletByOwner(userId).getFinalBalance();
    }

    public void sendMoney(UUID senderId, UUID receiverId, double amount) throws Exception {
        Validator.validateTransaction(senderId, receiverId, amount);

        Wallet sourceWallet = walletRepo.getWalletByOwner(senderId);
        Wallet destinationWallet = walletRepo.getWalletByOwner(receiverId);

        if (sourceWallet.getFinalBalance() < amount) {
            throw new Exception("Insufficient funds in source wallet");
        }

        Transaction creditTransaction = new Transaction(senderId, receiverId, amount, TransactionType.CREDIT,
                LocalDate.now());
        Transaction debitTransaction = new Transaction(senderId, receiverId, amount, TransactionType.DEBIT,
                LocalDate.now());

        synchronized (this) {
            sourceWallet.setFinalBalance(sourceWallet.getFinalBalance() - amount);
            sourceWallet.addTransaction(debitTransaction);
            destinationWallet.setFinalBalance(destinationWallet.getFinalBalance() + amount);
            destinationWallet.addTransaction(creditTransaction);
            // Persist
            walletRepo.updateWallet(sourceWallet);
            walletRepo.updateWallet(destinationWallet);
            offerService.applyOffers(debitTransaction);
            transactionService.addTransaction(debitTransaction);
            transactionService.addTransaction(creditTransaction);
        }
    }

    public List<Transaction> getTransactions(UUID userId, String filterKey, String sortBy, String sortOrder) throws Exception {
        Wallet userWallet = walletRepo.getWalletByOwner(userId);
        List<Transaction> userTransactions = userWallet.getTransactions();
        // Assuming search can be done on sender id, receiver id and transaction type
        userTransactions =  userTransactions.stream().filter(transaction ->
                        Objects.equals(transaction.getSenderId().toString(), filterKey) ||
                        Objects.equals(transaction.getReceiverId().toString(), filterKey) ||
                        Objects.equals(transaction.getTransactionType().toString(), filterKey))
                .collect(Collectors.toList());
        // Assuming sort can be done on amount and timestamp
        if (Objects.equals(sortBy, Constants.AMOUNT)) {
            if (Objects.equals(sortOrder, Constants.ASCENDING)) {
                userTransactions.sort(Comparator.comparing(Transaction::getAmount));
            } else {
                userTransactions.sort(Comparator.comparing(Transaction::getAmount).reversed());
            }
        } else if (Objects.equals(sortBy, Constants.TIMESTAMP)) {
            if (Objects.equals(sortOrder, Constants.ASCENDING)) {
                userTransactions.sort(Comparator.comparing(Transaction::getTimestamp));
            } else {
                userTransactions.sort(Comparator.comparing(Transaction::getAmount).reversed());
            }
        }
        return userTransactions;
    }

}
