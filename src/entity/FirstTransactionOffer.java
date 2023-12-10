package entity;

import service.TransactionService;
import service.WalletService;
import utils.Constants;

import java.time.LocalDate;
import java.util.List;

/*
Offer applicable if transaction >100 is done for first time
Sender gets 10% cashback
 */
public class FirstTransactionOffer implements Offer{

    private WalletService walletService;

    private TransactionService transactionService;

    public FirstTransactionOffer(WalletService walletService, TransactionService transactionService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
    }

    @Override
    public boolean checkOfferValidity(Transaction transaction) throws Exception {
        if (transaction.getAmount() < 100) {
            return false;
        }
        // Check if user has done any transaction >=100 before
        Wallet sourceWallet = walletService.getWalletForUser(transaction.getSenderId());
        List<Transaction> transactionList = sourceWallet.getTransactions();
        for (int i=0; i<transactionList.size()-1; i++) {
            if (transactionList.get(i).getAmount() >= 100 &&
                    transactionList.get(i).getTransactionType() == TransactionType.DEBIT) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void applyOffer(Transaction transaction) throws Exception {
        Wallet sourceWallet = walletService.getWalletForUser(transaction.getSenderId());
        // Give 10% cashback to sender
        Transaction cashbackTransaction = new Transaction(Constants.COMPANY_WALLET_ID, transaction.getSenderId(),
                0.10 * transaction.getAmount(), TransactionType.CASHBACK, LocalDate.now());
        sourceWallet.setFinalBalance(sourceWallet.getFinalBalance() + (0.10 * transaction.getAmount()));
        sourceWallet.addTransaction(transaction);
        // Persist information
        transactionService.addTransaction(cashbackTransaction);
        walletService.updateWallet(sourceWallet);
    }
}
