package android.bg.ro.boardgame.services;

import android.bg.ro.boardgame.models.BoardGame;
import android.bg.ro.boardgame.models.Friend;
import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.models.constrants.AccountTypeEnum;
import android.util.Base64;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomParser {

    public User getUser(JSONObject json) {
        User user = new User();
        JSONArray array = (JSONArray) json.get("boardGames");
        Iterator iter = array.iterator();

        List<BoardGame> boardGameList = new ArrayList<>();
        while (iter.hasNext()) {
            BoardGame boardGame = new BoardGame();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) parser.parse(iter.next().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            boardGame.setPicture(jsonObject.get("picture").toString());
            boardGame.setId(Integer.parseInt(json.get("id").toString()));
            boardGame.setDescription(jsonObject.get("description").toString());
            boardGame.setName(jsonObject.get("name").toString());

            boardGameList.add(boardGame);
        }

        array = (JSONArray) json.get("friendshipsSetOne");
        iter = array.iterator();
        List<Friend> friends = new ArrayList<>();
        while (iter.hasNext()) {
            Friend friend = new Friend();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = null;
            JSONObject friendship = null;
            try {
                friendship = (JSONObject) parser.parse(iter.next().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                jsonObject = (JSONObject) parser.parse(friendship.get("friendOne").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            friend.setId(Integer.parseInt(jsonObject.get("id").toString()));
            friend.setName(jsonObject.get("name").toString());
            friends.add(friend);
        }

        user.setId(Integer.parseInt(json.get("id").toString()));
        user.setName(json.get("name").toString());
        user.setEmail((json.get("email").toString()));
        user.setTown(json.get("town").toString());
        user.setAccountType(AccountTypeEnum.valueOf(json.get("accountType").toString()));
        user.setProfilePicture(Base64.decode(json.get("profilePicture").toString().getBytes(), Base64.DEFAULT));
        user.setBoardGames(boardGameList);
        user.setFriends(friends);

        return user;
    }
}
