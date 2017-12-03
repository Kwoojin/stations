package apps.com.stations;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class TripDetailActivity extends Activity{
    RbPreference pref;
    VisitData sdata;
    int density;

    String start_date="";
    String end_date="";
    String station="";
    String now_date="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_trip_detail);
        pref = new RbPreference(this);

        Intent gt = getIntent();

        if(gt.getStringExtra("tag")==null){
            Button back = (Button) findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();

                }
            });

            ImageView cancel = (ImageView) findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();

                }
            });




            sdata = (VisitData)gt.getSerializableExtra("list");

            start_date =gt.getStringExtra("start");
            end_date =gt.getStringExtra("end");

            station =gt.getStringExtra("station");



            ImageView add = (ImageView) findViewById(R.id.add);
            add.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent gt = new Intent(TripDetailActivity.this , TripAddActivity.class);
                    Bundle ex = new Bundle();
                    ex.putSerializable("list" , sdata);
                    gt.putExtras(ex);
                    gt.putExtra("start" , start_date);
                    gt.putExtra("end" , end_date);
                    gt.putExtra("station" , station);

                    startActivity(gt);

                }
            });


            ScrollView scroll = (ScrollView)findViewById(R.id.my_scroll);
            scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            scroll.setFocusable(true);
            scroll.setFocusableInTouchMode(true);
            scroll.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.requestFocusFromTouch();
                    return false;
                }
            });



            setWidget();

        }else{
            Intent gts = new Intent(TripDetailActivity.this , TripResultActivity.class);
            gts.putExtra("station" , gt.getStringExtra("station"));
            gts.putExtra("start" , gt.getStringExtra("start"));
            gts.putExtra("end" , gt.getStringExtra("end"));
            gts.putExtra("now_date" , gt.getStringExtra("now_date"));


            startActivity(gts);
            finish();
        }




    }


    private void setWidget(){
        CustomTextView store_name = (CustomTextView)findViewById(R.id.name);
        store_name.setText(sdata.getName());

        CustomTextView store_addr = (CustomTextView)findViewById(R.id.store_addr);
        store_addr.setText(sdata.getAddr());

        CustomTextView store_tel = (CustomTextView)findViewById(R.id.store_tel);
        store_tel.setText(sdata.getPhone());


        CustomTextView region = (CustomTextView)findViewById(R.id.region);
        region.setText(sdata.getLine()+"/"+sdata.getStation());

        CustomTextView cate = (CustomTextView)findViewById(R.id.cate);
        cate.setText(sdata.getCate());
        CustomTextView shorts = (CustomTextView)findViewById(R.id.shorts);
        shorts.setText(sdata.getDesc1());

        CustomTextView desc = (CustomTextView)findViewById(R.id.desc);
        desc.setText(sdata.getDesc2());


        ImageView img = (ImageView) findViewById(R.id.img);
        String filename = sdata.getImg()+".jpg";

        try {

            // get input stream
            InputStream ims = getAssets().open(filename);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            img.setImageDrawable(d);
        }
        catch(IOException ex) {
        }




        ImageView call = (ImageView) findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+sdata.getPhone()));
                startActivity(intent);



            }
        });

        setMap();



    }

    GoogleMap map;

    private void setMap() {


        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        LatLng position1 = new LatLng(Double.parseDouble(sdata.getLat()),
                Double.parseDouble(sdata.getLng()));

        map.addMarker(new MarkerOptions().position(position1).title("업체위치").snippet(""));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position1, 15));


    }






}
