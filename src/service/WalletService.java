package service;

import entity.Wallet;
import repo.WalletRepo;

import java.util.UUID;

public class WalletService {

    private WalletRepo walletRepo;

    private UserService userService;

    public WalletService(WalletRepo walletRepo) {
        this.walletRepo = walletRepo;
    }

    public boolean createWallet(UUID userId, Double amount) throws Exception {
        if (userService.getUserById(userId).isEmpty()) throw new Exception("User with this id does not exist");
        try {
            Wallet wallet = new Wallet(userId, amount);
            walletRepo.addWallet(wallet);
            walletRepo.addWalletForUser(userId, wallet.getId());
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

    public boolean updateWallet(Wallet wallet) {
        try {
            walletRepo.updateWallet(wallet);
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

    public Wallet getWalletForUser(UUID userId) throws Exception {
        return walletRepo.getWalletForUser(userId);
    }

}
