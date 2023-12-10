package utils;

import java.util.UUID;

public class Validator {
    public static void validateTransaction(UUID senderId, UUID receiverId, double amount) throws Exception {
        if (senderId == null) throw new Exception("Sender ID cannot be null");
        if (receiverId == null) throw new Exception("Receiver ID cannot be null");
        if (amount <= 0) throw new Exception("Amount must be greater than 0");
        if (senderId == receiverId) throw new Exception("Sender and receiver cannot be the same");
    }
}
