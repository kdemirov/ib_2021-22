package lab_02.server.kdc;

public class KDCSendEncryptedFrame {
   private KDCSendEncryptedFrameFromUser frameFromUser;
   private KDCSendEncryptedFrameToUser toUser;

   public KDCSendEncryptedFrame(KDCSendEncryptedFrameFromUser frameFromUser,
                                KDCSendEncryptedFrameToUser toUser){
       this.frameFromUser= frameFromUser;
       this.toUser = toUser;
   }

    public KDCSendEncryptedFrameFromUser getFrameFromUser() {
        return frameFromUser;
    }

    public KDCSendEncryptedFrameToUser getToUser() {
        return toUser;
    }
}
