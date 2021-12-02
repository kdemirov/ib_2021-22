package lab_02.server.service.impl;

import lab_02.exceptions.UserNotFoundException;
import lab_02.utils.AES;
import lab_02.model.User;
import lab_02.server.kdc.KDCSendEncryptedFrame;
import lab_02.server.kdc.KDCSendEncryptedFrameFromUser;
import lab_02.server.kdc.KDCSendEncryptedFrameToUser;
import lab_02.server.repository.KDCRepository;
import lab_02.server.service.KDCService;
import lab_02.server.service.UserService;
import lab_02.utils.Utils;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class KDCServiceImpl implements KDCService {

    private UserService userService = new UserServiceImpl();
    private static SecureRandom random = new SecureRandom();

    @Override
    public void createSessionKeyFor(String userFromId, String userToId) {
        try {
            User from = userService.findById(userFromId);
            User to = userService.findById(userToId);
            if (!from.getSessionKeys().containsKey(to.getId()) && !to.getSessionKeys().containsKey(from.getId())) {
                String nonce = from.generateNonce();
                SecretKey sessionKey = Utils.generateRandomSecretKey();
                byte[] encryptedKey = AES.encrypt(sessionKey.getEncoded(), from.getMySecretKey());
                byte[] encryptedLifetime = AES.encrypt(Utils.longToByteArray(KDCRepository.miliseconds), from.getMySecretKey());
                byte[] encryptedNonce = AES.encrypt(nonce.getBytes(), from.getMySecretKey());
                byte[] encryptedIdTo = AES.encrypt(userToId.getBytes(), from.getMySecretKey());
                KDCSendEncryptedFrameFromUser fromUser = new KDCSendEncryptedFrameFromUser(encryptedKey,
                        encryptedNonce, encryptedLifetime, encryptedIdTo);
                byte[] encryptedKeyTo = AES.encrypt(sessionKey.getEncoded(), to.getMySecretKey());
                byte[] encryptedIdFrom = AES.encrypt(from.getId().getBytes(), to.getMySecretKey());
                byte[] encryptedLifetimeTo = AES.encrypt(Utils.longToByteArray(KDCRepository.miliseconds), to.getMySecretKey());
                KDCSendEncryptedFrameToUser toUser = new KDCSendEncryptedFrameToUser(encryptedKeyTo, encryptedIdFrom, encryptedLifetimeTo);
                KDCSendEncryptedFrame frame = new KDCSendEncryptedFrame(fromUser, toUser);
                if (userService.receiveFromKDC(from.getId(), frame)) {
                    userService.setSessionKey(from.getId(), to.getId(), frame);
                } else {
                    throw new RuntimeException("An Error Occurred");
                }
            }
        } catch (UserNotFoundException | NoSuchAlgorithmException | IOException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void save(String userId, SecretKey kek) {
        KDCRepository.KEKS.putIfAbsent(userId, kek);
    }


}
