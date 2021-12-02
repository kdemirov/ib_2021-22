package lab_02.server.service;

import lab_02.exceptions.UserNotFoundException;
import lab_02.model.EncryptedMessage;
import lab_02.model.Message;


import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface MessageService {
    void save(String userIdFrom,String userIdTo,byte[] message) throws UserNotFoundException;
    List <Message> getDecryptedMessageForUser(String userIdToId);
    List<EncryptedMessage> findMessageForUser(String userIdToId);
    String registerUser(String name,String surname) throws NoSuchAlgorithmException;
}
