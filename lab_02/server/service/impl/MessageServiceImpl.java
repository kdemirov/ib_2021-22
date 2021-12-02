package lab_02.server.service.impl;

import lab_02.utils.AES;
import lab_02.exceptions.UserNotFoundException;
import lab_02.model.EncryptedMessage;
import lab_02.model.Message;
import lab_02.model.User;
import lab_02.server.repository.MessageRepository;
import lab_02.server.service.KDCService;
import lab_02.server.service.MessageService;
import lab_02.server.service.UserService;
import lab_02.utils.Utils;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

public class MessageServiceImpl implements MessageService {

    private UserService userService = new UserServiceImpl();
    private KDCService kdcService = new KDCServiceImpl();

    @Override
    public void save(String userIdFrom, String userIdTo, byte[] message) throws UserNotFoundException {
        kdcService.createSessionKeyFor(userIdFrom,userIdTo);
        MessageRepository.messages.add(userService.encryptMessage(userIdFrom, userIdTo, message));
    }

    @Override
    public List<Message> getDecryptedMessageForUser(String userIdToId) {
        List<EncryptedMessage> encryptedMessages = findMessageForUser(userIdToId);
        return encryptedMessages.stream().map(m -> {
            String message = null;
            try {
                message = userService.decryptMessage(userIdToId, m.getFrom().getId(), m);
            } catch (UserNotFoundException e) {
                e.printStackTrace();
            }
            return new Message(m.getFrom(), message);
        }).collect(Collectors.toList());
    }

    @Override
    public List<EncryptedMessage> findMessageForUser(String userIdToId) {
        return MessageRepository.messages.stream()
                .filter(m -> m.getTo().getId().equals(userIdToId))
                .collect(Collectors.toList());
    }

    @Override
    public String registerUser(String name, String surname) throws NoSuchAlgorithmException {
        User user = this.userService.save(name, surname);
        kdcService.save(user.getId(), user.getMySecretKey());
        return user.getId();
    }
}
