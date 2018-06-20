package android.bg.ro.boardgame.fragments;

import android.app.Fragment;
import android.bg.ro.boardgame.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class SearchFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.fragment_view_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Bucharest and move the camera
        LatLng bucharest = new LatLng(44.426767,26.102538);
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( bucharest, 14.0f) );
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }
}
