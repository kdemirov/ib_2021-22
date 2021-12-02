package lab_02.server.service.impl;

import lab_02.exceptions.UserNotFoundException;
import lab_02.model.EncryptedMessage;
import lab_02.utils.AES;
import lab_02.model.User;

import lab_02.server.kdc.KDCSendEncryptedFrame;
import lab_02.server.repository.UserRepository;
import lab_02.server.service.UserService;
import lab_02.utils.Utils;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class UserServiceImpl implements UserService {
    @Override
    public User findById(String id) throws UserNotFoundException {
        return UserRepository.users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User save(String name, String surname) throws NoSuchAlgorithmException {
        User user = new User(name, surname);
        UserRepository.users.add(user);
        return user;
    }

    @Override
    public boolean receiveFromKDC(String userId, KDCSendEncryptedFrame frame) throws UserNotFoundException, IOException {
        User user = findById(userId);
        boolean isVerified = user.verifyFrameFromKDC(frame);
        String toId = Utils.fromEncryptedBytesToString(frame.getFrameFromUser().getToId(), user.getMySecretKey());
        User userTo = findById(toId);
        ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.systemDefault());
        Long epochSeconds = timestamp.toEpochSecond();
        byte[] ts = Utils.longToByteArray(epochSeconds);
        byte[] encTS = AES.encrypt(ts, userTo.getMySecretKey());
        boolean isVerifiedTo = userTo.verifyFrameToKDC(frame.getToUser(), encTS);
        return isVerified && isVerifiedTo;
    }

    @Override
    public String decryptMessage(String userId, String messageFromUserId, EncryptedMessage message) throws UserNotFoundException {
        User user = findById(userId);
        SecretKey secretKey = user.getSessionKeys().get(messageFromUserId);
        return Utils.fromEncryptedBytesToString(message.getMessage(), secretKey);
    }

    @Override
    public void setSessionKey(String fromId, String toId, KDCSendEncryptedFrame frame) throws UserNotFoundException {
        User from = findById(fromId);
        User to = findById(toId);
        from.setSessionKeyFrom(frame.getFrameFromUser());
        to.setSessionKeyTo(frame.getToUser());
    }

    @Override
    public EncryptedMessage encryptMessage(String userFromId, String userToId, byte[] message) throws UserNotFoundException {
        User from = findById(userFromId);
        User to = findById(userToId);
        if (from.getSessionKeys().get(to.getId()).equals(to.getSessionKeys().get(from.getId()))) {
            byte[] encryptedMessage = AES.encrypt(message, from.getSessionKeys().get(to.getId()));
            return new EncryptedMessage(from, to, encryptedMessage);
        } else {
            return null;
        }
    }


}
