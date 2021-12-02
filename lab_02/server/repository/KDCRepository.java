package lab_02.server.repository;



import lab_02.utils.Utils;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

public class KDCRepository {
    public static Map<String, SecretKey> KEKS = new HashMap<>();
    public static long miliseconds = Utils.generateRandomLifetime();
}
