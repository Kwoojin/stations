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

public class VisitDetailActivity extends Activity{
    RbPreference pref;
    VisitData sdata;
    int density;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_detail);
        pref = new RbPreference(this);

        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

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

        Intent gt = getIntent();
        sdata = (VisitData)gt.getSerializableExtra("list");

        setWidget();



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

        map.addMarker(new MarkerOptions().position(position1).title("위치").snippet(""));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position1, 15));


    }






}
