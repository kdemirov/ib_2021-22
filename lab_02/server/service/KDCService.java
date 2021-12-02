package lab_02.server.service;



import javax.crypto.SecretKey;

public interface KDCService {
    void createSessionKeyFor(String userFromId, String userToId);
    void save(String userId,SecretKey kek);
}
