package entity;

public class FirstTransactionOffer implements Offer{
    @Override
    public boolean checkOfferValidity(Transaction transaction) {
        return false;
    }
}
