package entity;

public interface Offer {

    public boolean checkOfferValidity(Transaction transaction) throws Exception;

    public void applyOffer(Transaction transaction) throws Exception;

}
