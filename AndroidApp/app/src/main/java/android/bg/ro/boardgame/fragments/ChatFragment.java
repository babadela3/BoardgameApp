package android.bg.ro.boardgame.fragments;

import android.app.Fragment;
import android.bg.ro.boardgame.ChatActivity;
import android.bg.ro.boardgame.MenuActivity;
import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.adapters.ChatAdapter;
import android.bg.ro.boardgame.adapters.ChatMessage;
import android.bg.ro.boardgame.models.Client;
import android.bg.ro.boardgame.models.User;
import android.bg.ro.boardgame.services.SocketSendMessage;
import android.bg.ro.boardgame.services.TaskClient;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatFragment extends Fragment implements TaskClient{

    private TaskClient taskClient;
    private EditText messageET;
    private ListView messagesContainer;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        taskClient = this;
        Bundle bundle = getArguments();
        final int position = bundle.getInt("position");
        MenuActivity activity = (MenuActivity) getActivity();
        User user = activity.getUser();
        Client client = activity.getClient();
        client.setFragment(this);
        client.setActivity(getActivity());

        messagesContainer = (ListView) getView().findViewById(R.id.messagesContainer);
        loadDummyHistory();

        /*ImageView imageView = getView().findViewById(R.id.imageBoardGame);
        imageLoader.DisplayImage(user.getBoardGames().get(position).getPicture(), imageView);
        */
        Button button = (Button) getView().findViewById(R.id.chatSendButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messageET = getView().findViewById(R.id.messageEdit);
                if (TextUtils.isEmpty(messageET.getText().toString())) {
                    return;
                }
//                List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
//                params.add(new Pair<>("receiver","@" + String.valueOf(((MenuActivity) getActivity()).getUser().getFriends().get(position).getId())));
//                params.add(new Pair<>("message",messageET.getText().toString()));
//                new SocketSendMessage(taskClient,params,((MenuActivity) getActivity()).getClient()).execute("");
            }
        });
    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadDummyHistory(){

        chatHistory = new ArrayList<ChatMessage>();

//        ChatMessage msg = new ChatMessage();
//        msg.setId(1);
//        msg.setMe(false);
//        msg.setMessage("Hi");
//        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
//        chatHistory.add(msg);
//        ChatMessage msg1 = new ChatMessage();
//        msg1.setId(2);
//        msg1.setMe(false);
//        msg1.setMessage("How r u doing???");
//        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
//        chatHistory.add(msg1);

        adapter = new ChatAdapter(getActivity(), new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        for(int i=0; i<chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }

    }


    @Override
    public void TaskClientResult(Client client) {

    }

    @Override
    public void processMessage(String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(chatHistory.size() + 1);//dummy
        chatMessage.setMessage(message);
        chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatMessage.setMe(false);

        messageET.setText("");

        displayMessage(chatMessage);
    }
}