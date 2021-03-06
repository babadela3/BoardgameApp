package android.bg.ro.boardgame;

import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.bg.ro.boardgame.utils.Constant;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements TaskDelegate {

    private TextView email;
    private TextView password;
    private TextView forgotPassword;
    private TaskDelegate taskDelegate;
    private GenericHttpService genericHttpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        final Button loginButton = (Button) findViewById(R.id.buttonLogin);
        email = (TextView) findViewById(R.id.textEmail);
        password = (TextView) findViewById(R.id.textPassword);
        taskDelegate = this;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL url = null;
                try {
                    url = new URL("http://" + Constant.IP + "/getUser");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
                params.add(new Pair<>("email",email.getText().toString()));
                params.add(new Pair<>("password",password.getText().toString()));

                genericHttpService = (GenericHttpService) new GenericHttpService(LoginActivity.this.getApplicationContext(),"getUser", params,taskDelegate).execute(url);
            }
        });

        forgotPassword = (TextView) findViewById(R.id.textForgotPassword);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.bg.ro.boardgame.ForgotPasswordActivity");
                startActivity(intent);
            }
        });


    }

    @Override
    public void TaskCompletionResult(String result) {
        switch (genericHttpService.getResponseCode()) {
            case 200:
                JSONParser parser = new JSONParser();
                JSONObject json = null;
                try {
                    json = (JSONObject) parser.parse(genericHttpService.getResponse());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                intent.putExtra("email", (json.get("email").toString()));
                intent.putExtra("password", (password.getText().toString()));
                startActivity(intent);
                finish();
                break;
            case 401:
                Toast.makeText(LoginActivity.this, "The mail or password is incorrect.",
                        Toast.LENGTH_LONG).show();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap scaleImage(Bitmap bitmap){
        final int maxSize = 960;
        int outWidth;
        int outHeight;
        int inWidth = bitmap.getWidth();
        int inHeight = bitmap.getHeight();
        if(inWidth > inHeight){
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }

        return Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, false);
    }
}

