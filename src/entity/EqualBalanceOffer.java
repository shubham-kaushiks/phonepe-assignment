package entity;

public class EqualBalanceOffer implements Offer{
    @Override
    public boolean checkOfferValidity(Transaction transaction) {
        return false;
    }
}
