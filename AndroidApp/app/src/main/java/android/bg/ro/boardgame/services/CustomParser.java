package android.bg.ro.boardgame.services;

import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.models.constrants.AccountTypeEnum;
import android.util.Base64;

import org.json.simple.JSONObject;

public class CustomParser {

    public User getUser(JSONObject json) {
        User user = new User();
        user.setId(Integer.parseInt(json.get("id").toString()));
        user.setName(json.get("name").toString());
        user.setEmail((json.get("email").toString()));
        user.setTown(json.get("town").toString());
        user.setAccountType(AccountTypeEnum.valueOf(json.get("accountType").toString()));
        user.setProfilePicture(Base64.decode(json.get("profilePicture").toString().getBytes(), Base64.DEFAULT));
        return user;
    }
}
