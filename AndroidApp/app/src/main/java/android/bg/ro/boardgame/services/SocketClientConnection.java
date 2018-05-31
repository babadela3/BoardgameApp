package android.bg.ro.boardgame.services;

import android.bg.ro.boardgame.R;
import android.bg.ro.boardgame.models.Client;
import android.os.AsyncTask;
import android.util.Pair;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class SocketClientConnection extends AsyncTask<String, Void, String> {

    private TaskClient taskClient;
    private List<Pair<String, String>> parameters;
    private Client client;

    public SocketClientConnection(TaskClient taskClient, List<Pair<String, String>> parameters) {
        this.taskClient = taskClient;
        this.parameters = parameters;
    }

    @Override
    protected String doInBackground(String... params) {
        for(Pair<String, String> pair : parameters){
            if(pair.first.equals("id")){
                client = new Client("192.168.1.104", 8181, pair.second);
                client.start();
            }
        }
        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
        taskClient.TaskClientResult(client);
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}