package android.bg.ro.boardgame;

import android.bg.ro.boardgame.adapters.AddBoardGameAdapter;
import android.bg.ro.boardgame.adapters.BoardGameSelectAdapter;
import android.bg.ro.boardgame.adapters.CustomPagerAdapter;
import android.bg.ro.boardgame.models.BoardGame;
import android.bg.ro.boardgame.models.Pub;
import android.bg.ro.boardgame.models.PubPicture;
import android.bg.ro.boardgame.services.CustomParser;
import android.bg.ro.boardgame.services.GenericHttpService;
import android.bg.ro.boardgame.services.GoogleMapsService;
import android.bg.ro.boardgame.services.TaskDelegate;
import android.bg.ro.boardgame.services.TaskGoogleMaps;
import android.bg.ro.boardgame.utils.Constant;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PubActivity extends AppCompatActivity implements TaskDelegate, OnMapReadyCallback, TaskGoogleMaps{

    private GoogleMap mMap;
    TaskGoogleMaps taskGoogleMaps;
    TaskDelegate taskDelegate;
    private int pubId;
    private CustomPagerAdapter mCustomPagerAdapter;
    private GenericHttpService genericHttpService;
    private GenericHttpService genericHttpService1;
    private List<PubPicture> pubPictureList;
    private Pub pub;
    private List<BoardGame> boardGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);
        getSupportActionBar().hide();

        taskDelegate = this;
        Bundle bundle = getIntent().getExtras();
        pubId = bundle.getInt("pubId");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        URL url = null;
        try {
            url = new URL("http://" + Constant.IP + "/getPubInfo");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
        parameters.add(new Pair<>("id",String.valueOf(pubId)));

        genericHttpService = (GenericHttpService) new GenericHttpService(this,"getPubInfo", parameters,taskDelegate).execute(url);

    }

    @Override
    public void TaskCompletionResult(String result) {
        switch (genericHttpService.getResponseCode()) {
            case 200:
                CustomParser customParser = new CustomParser();
                switch (genericHttpService.getMapping()) {
                    case "getPubInfo":
                        pub = customParser.getPub(genericHttpService.getResponse());

                        TextView name = findViewById(R.id.name);
                        name.setText(pub.getName());
                        TextView description = findViewById(R.id.description);
                        description.setText(pub.getDescription());
                        TextView email = findViewById(R.id.email);
                        email.setText(pub.getEmail());
                        TextView address = findViewById(R.id.address);
                        address.setText(pub.getAddress());

                        taskGoogleMaps = this;
                        List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
                        parameters.add(new Pair<>("location", pub.getAddress()));
                        new GoogleMapsService(PubActivity.this, taskGoogleMaps, parameters).execute("");

                        URL urlGames = null;
                        try {
                            urlGames = new URL("http://" + Constant.IP + "/getPubGames");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        List<Pair<String, String>> paramsGames = new ArrayList<Pair<String, String>>();
                        paramsGames.add(new Pair<>("pubId", String.valueOf(pub.getId())));

                        genericHttpService1 = (GenericHttpService) new GenericHttpService(this, "getPubGames", paramsGames, taskDelegate).execute(urlGames);

                        ImageView profilePic = (ImageView) findViewById(R.id.imgView);

                        BitmapFactory.Options options = new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
                        options.inPurgeable = true; // inPurgeable is used to free up memory while required
                        Bitmap songImage = BitmapFactory.decodeByteArray(pub.getProfilePicture(), 0, pub.getProfilePicture().length, options);//Decode image, "thumbnail" is the object of image file
                        profilePic.setImageBitmap(songImage);

                        mCustomPagerAdapter = new CustomPagerAdapter(this, pub.getId());

                        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
                        mViewPager.setAdapter(mCustomPagerAdapter);

                        pubPictureList = new ArrayList<>();
                        for (int id : pub.getPicturesId()) {
                            URL url = null;
                            try {
                                url = new URL("http://" + Constant.IP + "/getPubPicture");
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }

                            List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
                            params.add(new Pair<>("id", String.valueOf(id)));

                            genericHttpService = (GenericHttpService) new GenericHttpService(this, "getPubPicture", params, taskDelegate).execute(url);
                        }
                        break;
                    case "getPubPicture":
                        PubPicture pubPicture = customParser.getPubPicture(genericHttpService.getResponse());
                        pubPictureList.add(pubPicture);
                        break;

                }
        }
        switch (genericHttpService1.getResponseCode()) {
            case 200:
                CustomParser customParser = new CustomParser();
                if(boardGames == null) {
                    boardGames = customParser.getBoardgames(genericHttpService1.getResponse());
                    GridView gridView = (GridView)findViewById(R.id.gridview);
                    BoardGameSelectAdapter boardGameSelectAdapter = new BoardGameSelectAdapter(PubActivity.this, boardGames);
                    gridView.setAdapter(boardGameSelectAdapter);

                    int totalHeight = (boardGames.size() / 3);

                    if (boardGames.size() % 3 != 0) {
                        totalHeight++;
                    }

                    ViewGroup.LayoutParams params = gridView.getLayoutParams();

                    params.height = 150 * totalHeight;

                    gridView.setLayoutParams(params);
                    gridView.requestLayout();

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(PubActivity.this, boardGames.get(position).getName(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void getLocation(LatLng latLng) {
        if(latLng != null){
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng bucharest = new LatLng(44.426767,26.102538);
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( bucharest, 15.0f) );
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }
}
