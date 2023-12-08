import repo.TransactionRepo;
import repo.UserRepo;
import repo.WalletRepo;
import service.OfferService;
import service.TransactionService;
import service.UserService;
import service.WalletService;

public class Main {
    public static void main(String[] args) {

        UserRepo userRepo = new UserRepo();
        WalletRepo walletRepo = new WalletRepo();
        TransactionRepo transactionRepo = new TransactionRepo();

        UserService userService = new UserService(userRepo);
        WalletService walletService = new WalletService(walletRepo);
        OfferService offerService = new OfferService();
        TransactionService transactionService = new TransactionService(transactionRepo, userService, walletService, offerService);

    }
}