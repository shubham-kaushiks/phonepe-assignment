package service;

import entity.Offer;
import entity.Transaction;

import java.util.ArrayList;
import java.util.List;

public class OfferService {

    List<Offer> availableOffers = new ArrayList<Offer>();

    public void addOffer(Offer offer) {
        availableOffers.add(offer);
    }

    public void removeOffer(Offer offer) {
        availableOffers.remove(offer);
    }

    public void applyOffers(Transaction transaction) throws Exception {
        for (Offer offer : availableOffers) {
            if (offer.checkOfferValidity(transaction)) {
                offer.applyOffer(transaction);
            }
        }
    }
}
