package android.bg.ro.boardgame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BoardGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_boardgame);
        getSupportActionBar().hide();

    }
}
