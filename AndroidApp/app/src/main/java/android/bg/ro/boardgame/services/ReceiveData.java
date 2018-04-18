package android.bg.ro.boardgame.services;

import android.bg.ro.boardgame.R;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class ReceiveData extends AsyncTask<URL, Integer, Long> {

    private String response = "";
    private String mapping;
    private List<Pair<String, String>> parameters;
    private int responseCode;
    private TaskDelegate delegate;
    private Context context;

    public ReceiveData(Context context, String mapping, List<Pair<String, String>> parameters, TaskDelegate taskDelegate) {
        this.context = context;
        this.mapping = mapping;
        this.parameters = parameters;
        this.delegate = taskDelegate;

    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public List<Pair<String, String>> getParameters() {
        return parameters;
    }

    public void setParameters(List<Pair<String, String>> parameters) {
        this.parameters = parameters;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder feedback = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                feedback.append("&");

            feedback.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            feedback.append("=");
            feedback.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return feedback.toString();
    }

    private void getData() throws IOException {

        HashMap<String, String> params = new HashMap<>();

        for(Pair<String, String> pair : parameters){
            params.put(pair.first,pair.second);
        }

        URL url = new URL("http://" + context.getResources().getString(R.string.localhost) + "/" + mapping);
        HttpURLConnection client = null;
        try {
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            // You need to specify the context-type.  In this case it is a
            // form submission, so use "multipart/form-data"
            client.setDoInput(true);
            client.setDoOutput(true);

            OutputStream os = client.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(params));

            writer.flush();
            writer.close();
            os.close();
            responseCode = client.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            }
            else {
                response = "";
            }

        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        finally {
            if(client != null) // Make sure the connection is not null.
                client.disconnect();
        }
    }

    @Override
    protected Long doInBackground(URL... params) {
        try {
            getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // This counts how many bytes were downloaded
        final byte[] result = response.getBytes();
        Long numOfBytes = Long.valueOf(result.length);
        return numOfBytes;
    }

    @Override
    protected void onPostExecute(Long result) {
        delegate.TaskCompletionResult(response);
    }
}