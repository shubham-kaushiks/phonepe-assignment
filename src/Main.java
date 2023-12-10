import entity.EqualBalanceOffer;
import entity.FirstTransactionOffer;
import entity.Offer;
import entity.Transaction;
import entity.User;
import repo.TransactionRepo;
import repo.UserRepo;
import repo.WalletRepo;
import service.OfferService;
import service.TransactionService;
import service.UserService;
import service.WalletService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Initialize repos
        UserRepo userRepo = new UserRepo();
        WalletRepo walletRepo = new WalletRepo();
        TransactionRepo transactionRepo = new TransactionRepo();
        // Initialize services
        UserService userService = new UserService(userRepo);
        OfferService offerService = new OfferService();
        TransactionService transactionService = new TransactionService(transactionRepo);
        WalletService walletService = new WalletService(walletRepo, transactionService, offerService);
        // Register offers into the system
        Offer equalBalanceOffer = new EqualBalanceOffer(walletService, transactionService);
        Offer firstTransactionOffer = new FirstTransactionOffer(walletService, transactionService);
        offerService.addOffer(equalBalanceOffer);
        offerService.addOffer(firstTransactionOffer);

        try {
            System.out.println("Registering Users");
            User user1 = userService.registerUser("User1", "user1@gmail.com");
            User user2 = userService.registerUser("User2", "user2@gmail.com");
            System.out.println("Creating wallets");
            walletService.createWallet(user1.getId());
            walletService.createWallet(user2.getId());
            walletService.topUpWallet(user1.getId(), 250.0);
            System.out.println("Balance after top up of rupees 250 :");
            System.out.println(walletService.fetchBalance(user1.getId()));
            System.out.println(walletService.fetchBalance(user2.getId()));
            // Send amount 100 and check first transaction >100 offer applied or not
            walletService.sendMoney(user1.getId(), user2.getId(), 100.0);
            System.out.println("Balance after sending rupees 100 :");
            System.out.println(walletService.fetchBalance(user1.getId()));
            System.out.println(walletService.fetchBalance(user2.getId()));
            // Now User1 has 160 and User2 has 100. Send 30 more and see if equal balance offer applied or not
            walletService.sendMoney(user1.getId(), user2.getId(), 30.0);
            System.out.println("Balance after sending rupees 30 more :");
            System.out.println(walletService.fetchBalance(user1.getId()));
            System.out.println(walletService.fetchBalance(user2.getId()));
            System.out.println("All transactions by User1 sorted by amount ascending :");
            List<Transaction> user1Transactions = walletService.getTransactions(user1.getId(), user1.getId().toString(),
                    "Amount", "Ascending");
            for(Transaction transaction : user1Transactions) {
                System.out.println("--------");
                System.out.println("Sender : " + transaction.getSenderId());
                System.out.println("Receiver : " + transaction.getReceiverId());
                System.out.println("Amount : " + transaction.getAmount());
                System.out.println("Transaction Type : " + transaction.getTransactionType());
                System.out.println("--------");
            }
        } catch (Exception exception) {
            System.out.println("Exception Raised : " + exception.getMessage());
        }

    }
}