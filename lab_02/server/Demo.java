package lab_02.server;

public class Demo {
    public static void main(String[] args) {
        try {
            MessageApp messageApp = new MessageApp();
            String aliceId = messageApp.registerUser("Alice", "Alisovska");
            String bobId = messageApp.registerUser("Bob", "Bobski");
            String toBob = "Zdravo Bob";
            String toAlice = "Zdravo Alice";
            messageApp.sendMessage(aliceId, bobId, toBob.getBytes());
            messageApp.sendMessage(bobId, aliceId, toAlice.getBytes());
            messageApp.printEncryptedMessagesFor(bobId);
            messageApp.printDecryptedMessagesFor(bobId);
            messageApp.printEncryptedMessagesFor(aliceId);
            messageApp.printDecryptedMessagesFor(aliceId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
