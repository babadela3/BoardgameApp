package android.bg.ro.boardgame.services;

import android.bg.ro.boardgame.models.BoardGame;
import android.bg.ro.boardgame.models.Event;
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
            boardGame.setId(Integer.parseInt(jsonObject.get("id").toString()));
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

    public List<Event> getEvents(String jsonString) {
        List<Event> events = new ArrayList<>();
        JSONArray jsonArray = null;
        JSONParser parser = new JSONParser();
        try {
            jsonArray = (JSONArray) parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Iterator iter1 = jsonArray.iterator();
        while (iter1.hasNext()) {
            JSONObject eventObject = null;
            try {
                eventObject = (JSONObject) parser.parse(iter1.next().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Event event = new Event();
            event.setId(Integer.parseInt(eventObject.get("id").toString()));
            event.setTitle(eventObject.get("title").toString());
            event.setDate(eventObject.get("date").toString());
            event.setAddress(eventObject.get("address").toString());
            event.setDescription(eventObject.get("description").toString());
            event.setPicture(eventObject.get("picture").toString());
            event.setMaxSeats(Integer.parseInt(eventObject.get("maxSeats").toString()));
            event.setLatitude(Double.parseDouble(eventObject.get("latitude").toString()));
            event.setLongitude(Double.parseDouble(eventObject.get("longitude").toString()));

            JSONObject userCreator = null;
            try {
                userCreator = (JSONObject) parser.parse(eventObject.get("userCreator").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            User user = new User();
            user.setId(Integer.parseInt(userCreator.get("id").toString()));
            user.setName(userCreator.get("name").toString());
            event.setUserCreator(user);

            JSONArray arrayGames = null;
            try {
                arrayGames = (JSONArray) parser.parse(eventObject.get("boardGames").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Iterator iter2 = arrayGames.iterator();
            ArrayList<BoardGame> boardGames = new ArrayList<>();
            while (iter2.hasNext()) {
                BoardGame game = new BoardGame();
                JSONObject gameObject = null;
                try {
                    gameObject = (JSONObject) parser.parse(iter2.next().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                game.setId(Integer.parseInt(gameObject.get("id").toString()));
                game.setName(gameObject.get("name").toString());
                game.setDescription(gameObject.get("description").toString());
                game.setPicture(gameObject.get("picture").toString());
                boardGames.add(game);
            }
            event.setBoardGames(boardGames);

            JSONObject pubObject = null;
            if(eventObject.get("pub") != null) {
                try {
                    pubObject = (JSONObject) parser.parse(eventObject.get("pub").toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Pub pub = new Pub();
                pub.setId(Integer.parseInt(pubObject.get("id").toString()));
                pub.setName(pubObject.get("name").toString());
                event.setPub(pub);
            }

            events.add(event);
        }
        return events;
    }

        public List<User> getEventUsers(String jsonString) {
            List<User> users = new ArrayList<>();
            JSONArray jsonArray = null;
            JSONParser parser = new JSONParser();
            try {
                jsonArray = (JSONArray) parser.parse(jsonString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Iterator iter = jsonArray.iterator();
            while (iter.hasNext()) {
                JSONObject userObject = null;
                try {
                    userObject = (JSONObject) parser.parse(iter.next().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                User user = new User();
                user.setId(Integer.parseInt(userObject.get("userId").toString()));
                user.setStatusEvent(userObject.get("status").toString());
                user.setName(userObject.get("userName").toString());
                users.add(user);
            }
        return users;
    }
}
