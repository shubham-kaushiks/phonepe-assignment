package entity;

import service.TransactionService;
import service.WalletService;
import utils.Constants;

import java.time.LocalDate;

/*
Offer applicable if balance of sender and receiver become equal
Sender and receiver get 50 rupees each
 */
public class EqualBalanceOffer implements Offer{

    private WalletService walletService;

    private TransactionService transactionService;

    public EqualBalanceOffer(WalletService walletService, TransactionService transactionService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
    }
    @Override
    public boolean checkOfferValidity(Transaction transaction) throws Exception {
        if (transaction.getTransactionType() == TransactionType.TOP_UP ||
                transaction.getTransactionType() == TransactionType.WITHDRAWAL) {
            return false;
        }
        Wallet senderWallet = walletService.getWalletForUser(transaction.getSenderId());
        Wallet receiverWallet = walletService.getWalletForUser(transaction.getReceiverId());
        return (int) senderWallet.getFinalBalance() == (int) receiverWallet.getFinalBalance();
    }

    @Override
    public void applyOffer(Transaction transaction) throws Exception {
        Wallet senderWallet = walletService.getWalletForUser(transaction.getSenderId());
        Wallet receiverWallet = walletService.getWalletForUser(transaction.getReceiverId());
        // Give 50 rupees to sender
        Transaction senderCashbackTransaction = new Transaction(Constants.COMPANY_WALLET_ID, transaction.getSenderId(),
                10, TransactionType.CASHBACK, LocalDate.now());
        senderWallet.setFinalBalance(senderWallet.getFinalBalance() + 50);
        senderWallet.addTransaction(senderCashbackTransaction);
        // Give 50 rupees to receiver
        Transaction receiverCashbackTransaction = new Transaction(Constants.COMPANY_WALLET_ID, transaction.getReceiverId(),
                10, TransactionType.CASHBACK, LocalDate.now());
        receiverWallet.setFinalBalance(receiverWallet.getFinalBalance() + 50);
        receiverWallet.addTransaction(receiverCashbackTransaction);
        // Persist information
        transactionService.addTransaction(senderCashbackTransaction);
        transactionService.addTransaction(receiverCashbackTransaction);
        walletService.updateWallet(senderWallet);
        walletService.updateWallet(receiverWallet);
    }
}
