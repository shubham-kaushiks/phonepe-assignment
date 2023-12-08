package service;

import entity.Transaction;
import entity.User;
import entity.Wallet;
import repo.TransactionRepo;
import utils.Validator;

import java.time.LocalDate;
import java.util.UUID;

public class TransactionService {

    private TransactionRepo transactionRepo;

    private UserService userService;

    private WalletService walletService;

    private OfferService offerService;

    public TransactionService(TransactionRepo transactionRepo, UserService userService, WalletService walletService, OfferService offerService) {
        this.transactionRepo = transactionRepo;
        this.userService = userService;
        this.walletService = walletService;
        this.offerService = offerService;
    }

    public boolean transact(UUID senderId, UUID receiverId, double amount, String description) {
        try {
            Validator.validateTransaction(senderId, receiverId, amount, description);
            User sourceUser = userService.getUserById(senderId);
            User destinationUser = userService.getUserById(receiverId);

            Wallet sourceWallet = walletService.getWalletForUser(sourceUser.getId());
            Wallet destinationWallet = walletService.getWalletForUser(destinationUser.getId());

            if (sourceWallet.getFinalBalance() < amount) {
                System.out.println("Insufficient funds in source wallet");
                return false;
            }

            Transaction transaction = new Transaction(sourceUser.getId(), destinationUser.getId(),
                    amount, description, LocalDate.now());

            synchronized (this) {
                sourceWallet.setFinalBalance(sourceWallet.getFinalBalance() - amount);
                destinationWallet.setFinalBalance(destinationWallet.getFinalBalance() + amount);
                walletService.updateWallet(sourceWallet);
                walletService.updateWallet(destinationWallet);
                offerService.applyOffers(transaction);
                transactionRepo.addTransaction(transaction);
                transactionRepo.addUserTransaction(sourceUser.getId(), transaction.getId());
                transactionRepo.addUserTransaction(destinationUser.getId(), transaction.getId());
                return true;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
