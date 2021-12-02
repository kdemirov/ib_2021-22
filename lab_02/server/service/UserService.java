package lab_02.server.service;
import lab_02.exceptions.UserNotFoundException;
import lab_02.model.EncryptedMessage;
import lab_02.model.User;
import lab_02.server.kdc.KDCSendEncryptedFrame;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface UserService {
    User findById(String id) throws UserNotFoundException;
    User save(String name,String surname) throws NoSuchAlgorithmException;
    boolean receiveFromKDC(String userId,KDCSendEncryptedFrame frame) throws UserNotFoundException, IOException;
    String decryptMessage(String userId, String messageFromUserId, EncryptedMessage message) throws UserNotFoundException;
    void setSessionKey(String fromId,String toId,KDCSendEncryptedFrame frame) throws UserNotFoundException;
    EncryptedMessage encryptMessage(String userFromId,String userToId,byte[] message) throws UserNotFoundException;
}
