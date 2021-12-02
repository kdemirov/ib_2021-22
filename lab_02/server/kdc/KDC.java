package lab_02.server.kdc;


import lab_02.model.User;
import java.util.ArrayList;
import java.util.List;



public class KDC {
    List<User> users;


    public KDC() {
        this.users = new ArrayList<>();
    }

    public void addKEK(User user){
        this.users.add(user);
    }


}
