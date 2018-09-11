package android.bg.ro.boardgame;

import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.bg.ro.boardgame.utils.Constant;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private EditText textPassword;
    private EditText textToken;
    private EditText textEmailReset;
    private TextView textInsertToken;
    private EditText textEmail;
    private TextView labelEmail;
    private TextView labelInstructions;
    private Button forgotButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();
        taskDelegate = this;

        email = (TextView) findViewById(R.id.textEmail);
        forgotButton = (Button) findViewById(R.id.buttonForgot);

        textPassword = (EditText) findViewById(R.id.textPassword);
        textToken = (EditText) findViewById(R.id.textToken);
        textInsertToken = (TextView) findViewById(R.id.textInsertToken);
        textEmail = (EditText) findViewById(R.id.textEmail);
        labelEmail = (TextView) findViewById(R.id.labelEmail);
        labelInstructions = (TextView) findViewById(R.id.labelInstructions);
        textEmailReset = (EditText) findViewById(R.id.textEmailReset);

        textPassword.setVisibility(View.GONE);
        textToken.setVisibility(View.GONE);
        textEmailReset.setVisibility(View.GONE);

        textInsertToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textInsertToken.getText().toString().equals("Insert Token")) {
                    textPassword.setVisibility(View.VISIBLE);
                    textToken.setVisibility(View.VISIBLE);
                    textEmailReset.setVisibility(View.VISIBLE);
                    textInsertToken.setText("Send email");

                    textEmail.setVisibility(View.GONE);
                    labelEmail.setVisibility(View.GONE);
                    labelInstructions.setVisibility(View.GONE);
                    forgotButton.setText("Reset password");

                }
                else {
                    textPassword.setVisibility(View.GONE);
                    textToken.setVisibility(View.GONE);
                    textEmailReset.setVisibility(View.GONE);
                    textInsertToken.setText("Insert Token");

                    textEmail.setVisibility(View.VISIBLE);
                    labelEmail.setVisibility(View.VISIBLE);
                    labelInstructions.setVisibility(View.VISIBLE);
                    labelInstructions.setText("Enter your email below to receive your password reset instructions");
                    forgotButton.setText("Send token");
                }
            }
        });


        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(forgotButton.getText().toString().equals("Send token")) {
                    URL url = null;
                    try {
                        url = new URL("http://" + Constant.IP + "/user/sendPassword");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
                    params.add(new Pair<>("email",email.getText().toString()));

                    genericHttpService = (GenericHttpService) new GenericHttpService(ForgotPasswordActivity.this.getApplicationContext(),"user/sendPassword", params,taskDelegate).execute(url);
                }
                if(forgotButton.getText().toString().equals("Reset password")) {
                    URL url = null;
                    try {
                        url = new URL("http://" + Constant.IP + "/user/resetPassword");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
                    params.add(new Pair<>("email",textEmailReset.getText().toString()));
                    params.add(new Pair<>("token",textToken.getText().toString()));
                    params.add(new Pair<>("password",textPassword.getText().toString()));

                    genericHttpService = (GenericHttpService) new GenericHttpService(ForgotPasswordActivity.this.getApplicationContext(),"user/resetPassword", params,taskDelegate).execute(url);
                }
            }
        });


    }

    @Override
    public void TaskCompletionResult(String result) {
        if(genericHttpService.getMapping().equals("user/sendPassword")) {
            switch (genericHttpService.getResponseCode()) {
                case 200:
                    Toast.makeText(ForgotPasswordActivity.this, "We sent an email with a token.",
                            Toast.LENGTH_LONG).show();
                    break;
                case 401:
                    Toast.makeText(ForgotPasswordActivity.this, "The mail is incorrect.",
                            Toast.LENGTH_LONG).show();
                    break;

            }
        }
        if(genericHttpService.getMapping().equals("user/resetPassword")) {
            switch (genericHttpService.getResponseCode()) {
                case 200:
                    Toast.makeText(ForgotPasswordActivity.this, "The password has successfully changed.",
                            Toast.LENGTH_LONG).show();
                    break;
                case 401:
                    Toast.makeText(ForgotPasswordActivity.this, "The token or email is incorrect.",
                            Toast.LENGTH_LONG).show();
                    break;

            }
        }
    }
}