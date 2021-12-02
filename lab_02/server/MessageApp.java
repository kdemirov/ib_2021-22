package lab_02.server;

import lab_02.exceptions.UserNotFoundException;
import lab_02.server.service.MessageService;
import lab_02.server.service.impl.MessageServiceImpl;

import java.security.NoSuchAlgorithmException;


public class MessageApp {

    private MessageService messageService;


    public MessageApp() {
        this.messageService = new MessageServiceImpl();
    }

    public String registerUser(String name, String surname) throws NoSuchAlgorithmException {
        return this.messageService.registerUser(name, surname);
    }

    public void sendMessage(String userIdFrom, String userIdTo, byte[] message) throws UserNotFoundException {
        messageService.save(userIdFrom, userIdTo, message);
    }

    public void printEncryptedMessagesFor(String userIdTo) {
        messageService.findMessageForUser(userIdTo)
                .forEach(System.out::println);
    }

    public void printDecryptedMessagesFor(String userIdTo) {
        messageService.getDecryptedMessageForUser(userIdTo)
                .forEach(System.out::println);
    }
}
