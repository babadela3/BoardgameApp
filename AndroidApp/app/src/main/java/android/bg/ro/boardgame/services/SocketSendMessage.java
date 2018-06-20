package android.bg.ro.boardgame.services;

import android.bg.ro.boardgame.models.Client;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SocketSendMessage extends AsyncTask<String, Void, String> implements TaskDelegate{

    private TaskClient taskClient;
    private TaskDelegate taskDelegate;
    private List<Pair<String, String>> parameters;
    private Client client;
    private Context context;

    public SocketSendMessage(Context context, TaskClient taskClient, List<Pair<String, String>> parameters, Client client) {
        this.taskClient = taskClient;
        this.parameters = parameters;
        this.client = client;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String receiver = "";
        String sender = "";
        String message = "";
        for(Pair<String, String> pair : parameters){
            if(pair.first.equals("receiver")){
                receiver = pair.second;
            }
            if(pair.first.equals("sender")){
                sender = pair.second;
            }
            if(pair.first.equals("message")){
                message = pair.second;
            }
        }
        String finalMessage = receiver + " " + message;
        client.sendMessage(finalMessage);

        URL url = null;
        try {
            url = new URL("http://10.11.20.237:8182/sendMessage");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
        parameters.add(new Pair<>("sender",sender));
        parameters.add(new Pair<>("receiver",receiver.substring(receiver.indexOf("@") + 1)));
        parameters.add(new Pair<>("message",message));
        taskDelegate = this;

        new GenericHttpService(context,"sendMessage", parameters,taskDelegate).execute(url);
        return finalMessage;
    }

    @Override
    protected void onPostExecute(String result) {
        taskClient.processMessage(result);
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}

    @Override
    public void TaskCompletionResult(String result) {

    }
}