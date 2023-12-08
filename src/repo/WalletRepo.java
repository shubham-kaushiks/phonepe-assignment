package repo;

import entity.User;
import entity.Wallet;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class WalletRepo {
    private Map<UUID, Wallet> wallets = new HashMap<>();

    private Map<UUID, UUID> walletOwners = new HashMap<>();

    public void addWallet(Wallet wallet) throws Exception {
        if (wallets.containsKey(wallet.getId())) throw new Exception("Wallet already created");
        wallets.put(wallet.getId(), wallet);
    }

    public void updateWallet(Wallet wallet) throws Exception {
        if (!wallets.containsKey(wallet.getId())) throw new Exception("Wallet not created");
        wallets.put(wallet.getId(), wallet);
    }

    public void addWalletForUser(UUID userId, UUID walletId) throws Exception {
        if (!walletOwners.containsKey(userId)) throw new Exception("User does not exist");
        walletOwners.put(userId, walletId);
    }

    public Wallet getWalletForUser(UUID userId) throws Exception {
        if (!walletOwners.containsKey(userId)) throw new Exception("User does not exist");
        return wallets.get(walletOwners.get(userId));
    }
}
