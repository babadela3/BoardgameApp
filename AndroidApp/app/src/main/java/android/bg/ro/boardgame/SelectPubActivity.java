package android.bg.ro.boardgame;

import android.bg.ro.boardgame.adapters.PubAdapter;
import android.bg.ro.boardgame.models.Pub;
import android.bg.ro.boardgame.services.CustomParser;
import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SelectPubActivity extends AppCompatActivity implements TaskDelegate{

    private ArrayList<Pub> pubs;
    Pub selectedPub;
    private TaskDelegate taskDelegate;
    private GenericHttpService genericHttpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        getSupportActionBar().hide();

        taskDelegate = this;
        URL url = null;
        try {
            url = new URL("http://" + getResources().getString(R.string.localhost) + "/getPubs");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
        genericHttpService = (GenericHttpService) new GenericHttpService(SelectPubActivity.this.getApplicationContext(),"getPubs", params,taskDelegate).execute(url);

        TextView title = (TextView) findViewById(R.id.createEvent_title);
        title.setText("Select Pub");
        TextView text = (TextView) findViewById(R.id.text);
        text.setText("Select the pub where you want to go and play.");

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPub = null;
                boolean singleSelected = false;
                for(Pub pub : pubs){
                    if(pub.isSelected()){
                        if(selectedPub == null){
                            singleSelected = true;
                            selectedPub = pub;
                        }
                        else {
                            singleSelected = false;
                        }
                    }
                }
                if(singleSelected || selectedPub == null){
                    onBackPressed();
                }
                else {
                    Toast.makeText(SelectPubActivity.this, "Please select just one pub.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedPub",selectedPub);
        intent.putExtras(bundle);
        setResult(40, intent);
        super.onBackPressed();
    }

    public ArrayList<Pub> getPubs() {
        return pubs;
    }

    public void setPubs(ArrayList<Pub> pubs) {
        this.pubs = pubs;
    }

    @Override
    public void TaskCompletionResult(String result) {
        switch (genericHttpService.getResponseCode()) {
            case 200:
                CustomParser customParser = new CustomParser();
                pubs = new ArrayList<>(customParser.getPubs(genericHttpService.getResponse()));

                PubAdapter adapter = new PubAdapter(SelectPubActivity.this, 0, pubs);
                ListView listView = (ListView) findViewById(R.id.listFriends);
                listView.setAdapter(adapter);

                break;
        }
    }
}