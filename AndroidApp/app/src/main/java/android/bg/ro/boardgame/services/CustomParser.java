package android.bg.ro.boardgame.services;

import android.bg.ro.boardgame.models.BoardGame;
import android.bg.ro.boardgame.models.Friend;
import android.bg.ro.boardgame.models.Message;
import android.bg.ro.boardgame.models.Pub;
import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.models.constrants.AccountTypeEnum;
import android.util.Base64;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;
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

    public List<Message> getMessages(String jsonString){
        List<Message> messages = new ArrayList<>();
        JSONArray json = null;
        JSONParser parser = new JSONParser();
        try {
            json = (JSONArray) parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Iterator iter = json.iterator();
        while (iter.hasNext()) {
            JSONObject messageObject = null;
            try {
                messageObject = (JSONObject) parser.parse(iter.next().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Message message = new Message();
            message.setSenderId(Integer.parseInt(messageObject.get("sender").toString()));
            message.setReceiverId(Integer.parseInt(messageObject.get("receiver").toString()));
            message.setMessage(messageObject.get("message").toString());
            message.setDate(messageObject.get("date").toString());
            messages.add(message);
        }

        return messages;
    }

    public List<Pub> getPubs(String jsonString){
        List<Pub> pubs = new ArrayList<>();
        JSONArray json = null;
        JSONParser parser = new JSONParser();
        try {
            json = (JSONArray) parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Iterator iter = json.iterator();
        while (iter.hasNext()) {
            JSONObject pubObject = null;
            try {
                pubObject = (JSONObject) parser.parse(iter.next().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Pub pub = new Pub();
            pub.setId(Integer.parseInt(pubObject.get("id").toString()));
            pub.setName(pubObject.get("name").toString());
            pub.setEmail(pubObject.get("email").toString());
            pub.setAddress(pubObject.get("address").toString());
            pub.setDescription(pubObject.get("description").toString());
            pubs.add(pub);
        }
        return pubs;
    }
}
