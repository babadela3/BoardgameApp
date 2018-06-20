package android.bg.ro.boardgame;

import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ForgotPasswordActivity extends AppCompatActivity implements TaskDelegate{

    private TextView email;
    private TaskDelegate taskDelegate;
    private GenericHttpService genericHttpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();
        taskDelegate = this;

        email = (TextView) findViewById(R.id.textEmail);
        Button forgotButton = (Button) findViewById(R.id.buttonForgot);


        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL url = null;
                try {
                    url = new URL("http://" + getResources().getString(R.string.localhost) + "/user/sendPassword");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
                params.add(new Pair<>("email",email.getText().toString()));

                genericHttpService = (GenericHttpService) new GenericHttpService(ForgotPasswordActivity.this.getApplicationContext(),"user/sendPassword", params,taskDelegate).execute(url);
            }
        });


    }

    @Override
    public void TaskCompletionResult(String result) {
        switch (genericHttpService.getResponseCode()) {
            case 200:
                Toast.makeText(ForgotPasswordActivity.this, "We sent an email with your password.",
                        Toast.LENGTH_LONG).show();
                break;
            case 401:
                Toast.makeText(ForgotPasswordActivity.this, "The mail is incorrect.",
                        Toast.LENGTH_LONG).show();
                break;

        }
    }
}