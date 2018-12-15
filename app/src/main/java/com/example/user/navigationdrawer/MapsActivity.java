package com.example.user.navigationdrawer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;




    public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

        private GoogleMap mMap;
        private DatabaseReference ref;
        public Double latitude;
        public  Double longitude;
        public  Marker mark , bustop1,bustop2,bustop3;
        public LatLng busstop11,busstop22,busstop33;
        public  String bus1,bus2,bus3;



        public Location locationA = new Location("point A");
        public Location locationB = new Location("point B");
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setMaxZoomPreference(18);
            mMap.setMinZoomPreference(12);
            //Log.d("myTag", "wow loc working");
            LatLng sydney = new LatLng(23.177269,80.019778);
            locationA.setLatitude(23.177269);
            locationA.setLongitude(80.019778);
            mark =    mMap.addMarker(new MarkerOptions().position(sydney).title("INITIAL POSITION"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            //  mMap.setMapType(mMap.MAP_TYPE_SATELLITE);

            busstop11 = new LatLng(23.161172,79.948856);
            busstop22 = new LatLng(23.154749,79.952181);
            busstop33 = new LatLng(23.163134,79.952292);


     /*   mMap.addCircle(new CircleOptions()
                .radius(15)
                .center(sydney)
                .fillColor(0x550000FF)
                .strokeWidth(0f));*/

            contupdates();
        }

        private void contupdates(){


            ref = FirebaseDatabase.getInstance().getReference().child("Location");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot sp) {



                    longitude = sp.child("longitude").getValue(Double.class);
                    latitude = sp.child("latitude").getValue(Double.class);
                   // Log.d("working","omg");



                    if(latitude !=null && longitude!=null) {
                        mark.remove();
                        LatLng location = new LatLng(latitude, longitude);
                        locationB.setLatitude(latitude);
                        locationB.setLongitude(longitude);
                        double distance = locationA.distanceTo(locationB)/1000;
                        String dist = Double.toString(distance);
                      //  String sd= Double.toString(latitude);
                      //  Log.d("location tagggg",sd);
                        //   Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                        final Context context = getApplicationContext();
                        String adr = getLocationAddress(context ,latitude,longitude);

                        mark=  mMap.addMarker(new MarkerOptions().position(location).snippet("BUS"+"\n"+"\n"+"distance from clg :"+dist+"\n"+"\n"+"address :"+adr));
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                LinearLayout info = new LinearLayout(context);
                                info.setOrientation(LinearLayout.VERTICAL);
                                TextView title = new TextView(context);
                                title.setTextColor(Color.BLACK);
                                title.setGravity(Gravity.CENTER);
                                title.setTypeface(null , Typeface.BOLD);
                                // title.getText(marker.getTitle());
                                TextView snippet = new TextView(context);
                                snippet.setTextColor(Color.GRAY);
                                snippet.setText(marker.getSnippet());

                                info.addView(title);
                                info.addView(snippet);
                                return info;
                            }
                        });


                        bus1 = getLocationAddress(context ,23.161172,79.948856);

                        bustop1= mMap.addMarker(new MarkerOptions().position(busstop11).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).snippet("STOP 2"+"\n"+"\n"+"TIME -- "+"4:05,6:05,8:05"+"\n"+"\n"+"address :"+bus1));
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                LinearLayout info = new LinearLayout(context);
                                info.setOrientation(LinearLayout.VERTICAL);
                                TextView title = new TextView(context);
                                title.setTextColor(Color.BLACK);
                                title.setGravity(Gravity.CENTER);
                                title.setTypeface(null, Typeface.BOLD);
                                // title.getText(marker.getTitle());
                                TextView snippet = new TextView(context);
                                snippet.setTextColor(Color.GRAY);
                                snippet.setText(marker.getSnippet());

                                info.addView(title);
                                info.addView(snippet);
                                return info;
                            }
                        });

                        bus2 = getLocationAddress(context ,23.154749,79.952181);
                        bustop2 = mMap.addMarker(new MarkerOptions().position(busstop22).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).snippet("STOP 3"+"\n"+"\n"+"TIME -- "+"4:25,6:25,8:25"+"\n"+"\n"+"address :"+bus2));
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                LinearLayout info = new LinearLayout(context);
                                info.setOrientation(LinearLayout.VERTICAL);
                                TextView title = new TextView(context);
                                title.setTextColor(Color.BLACK);
                                title.setGravity(Gravity.CENTER);
                                title.setTypeface(null, Typeface.BOLD);
                                // title.getText(marker.getTitle());
                                TextView snippet = new TextView(context);
                                snippet.setTextColor(Color.GRAY);
                                snippet.setText(marker.getSnippet());

                                info.addView(title);
                                info.addView(snippet);
                                return info;
                            }
                        });

                        bus3 = getLocationAddress(context ,23.163134,79.952292);

                        bustop3 = mMap.addMarker(new MarkerOptions().position(busstop33).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).snippet("STOP 1"+"\n"+"\n"+"TIME -- "+"4:00,6:00,8:00"+"\n"+"\n"+"address :"+bus3));
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                LinearLayout info = new LinearLayout(context);
                                info.setOrientation(LinearLayout.VERTICAL);
                                TextView title = new TextView(context);
                                title.setTextColor(Color.BLACK);
                                title.setGravity(Gravity.CENTER);
                                title.setTypeface(null, Typeface.BOLD);
                                // title.getText(marker.getTitle());
                                TextView snippet = new TextView(context);
                                snippet.setTextColor(Color.GRAY);
                                snippet.setText(marker.getSnippet());

                                info.addView(title);
                                info.addView(snippet);
                                return info;
                            }
                        });

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                     /*   mMap.addCircle(new CircleOptions()
                                .radius(15)
                                .center(location)
                                .fillColor(0x550000FF)
                                .strokeWidth(0f));*/

                    }




                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("bad tag", "Failed to read value.");
                }
            });

        }


        private String getLocationAddress(Context context,double lat,double longi)
        {
            String addr="";
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(lat,longi,1);
                if(addresses !=null  && addresses.size() >0)
                {
                    addr = addresses.get(0).getAddressLine(0);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return (addr);

        }



    }







