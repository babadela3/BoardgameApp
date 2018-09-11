package android.bg.ro.boardgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectAddressActivity extends AppCompatActivity {

    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        getSupportActionBar().hide();

        TextView text = (TextView) findViewById(R.id.text);
        text.setText("Select address where you want to go and play.");

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("address",address);
        intent.putExtras(bundle);
        setResult(50, intent);
        super.onBackPressed();
    }
}