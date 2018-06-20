package android.bg.ro.boardgame;

import android.bg.ro.boardgame.adapters.ChatAdapter;
import android.bg.ro.boardgame.adapters.ChatMessage;
import android.bg.ro.boardgame.models.Client;
import android.bg.ro.boardgame.models.Message;
import android.bg.ro.boardgame.services.CustomParser;
import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.SocketClientConnection;
import android.bg.ro.boardgame.services.SocketSendMessage;
import android.bg.ro.boardgame.services.TaskClient;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements TaskClient, TaskDelegate{

    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;

    private int friendId;
    private int userId;
    private String friendName;
    private Client client;
    private TaskClient taskClient;
    private int position;

    private GenericHttpService genericHttpService;
    private TaskDelegate taskDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        taskClient = this;
        Bundle b = this.getIntent().getExtras();
        if (b != null){
            friendName = b.getString("nameFriend");
            friendId = b.getInt("idFriend");
            userId = b.getInt("idUser");
            position = b.getInt("position");
        }
        getSupportActionBar().setTitle(friendName);

        List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
        params.add(new Pair<>("id",String.valueOf(userId)));
        new SocketClientConnection(taskClient,params).execute("");

        URL url = null;
        try {
            url = new URL("http://" + getResources().getString(R.string.localhost) + "/allMessages");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        taskDelegate = this;
        List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
        parameters.add(new Pair<>("friendOne",String.valueOf(userId)));
        parameters.add(new Pair<>("friendTwo",String.valueOf(friendId)));

        genericHttpService = (GenericHttpService) new GenericHttpService(ChatActivity.this.getApplicationContext(),"allMessages", parameters,taskDelegate).execute(url);

        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageET = findViewById(R.id.messageEdit);
                if (TextUtils.isEmpty(messageET.getText().toString())) {
                    return;
                }
                List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
                params.add(new Pair<>("receiver","@" + String.valueOf(friendId)));
                params.add(new Pair<>("message",messageET.getText().toString()));
                params.add(new Pair<>("sender",String.valueOf(userId)));
                new SocketSendMessage(ChatActivity.this.getApplicationContext(),taskClient,params,client).execute("");
            }
        });

    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadHistory(List<Message> messageList){
        chatHistory = new ArrayList<ChatMessage>();
        for(Message message : messageList) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setId(messageList.indexOf(message) + 1);
            chatMessage.setMessage(message.getMessage());
            chatMessage.setDate(message.getDate());
            if(message.getSenderId() == userId) {
                chatMessage.setMe(false);
            }
            else {
                chatMessage.setMe(true);
            }
            chatHistory.add(chatMessage);
        }

        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        for(int i=0; i<chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }

    }

    @Override
    public void onBackPressed() {
        client.disconnect();
        super.onBackPressed();
    }

    @Override
    public void TaskClientResult(Client client) {
        client.setActivity(this);
        setClient(client);
    }

    @Override
    public void processMessage(String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(chatHistory.size() + 1);
        chatMessage.setMessage(message.substring(String.valueOf(friendId).length() + 2));

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        chatMessage.setDate(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        chatMessage.setMe(false);

        messageET.setText("");
        displayMessage(chatMessage);
    }

    @Override
    public void TaskCompletionResult(String result) {
        switch (genericHttpService.getResponseCode()) {
            case 200:
                CustomParser customParser = new CustomParser();
                loadHistory(customParser.getMessages(genericHttpService.getResponse()));
        }
    }
}