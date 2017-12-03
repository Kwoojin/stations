
package apps.com.stations;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TripScheduleActivity extends Activity {
    RbPreference pref;
    ArrayList<ScheduleData> vdata = new ArrayList<ScheduleData>();
    String cateStr="favor";
    EditText secrch_txt;
    String secrch_keyword="";
    String selectDate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_trip_sc);
        pref = new RbPreference(this);

        Intent gt = getIntent();
        start_date =gt.getStringExtra("start");
        end_date =gt.getStringExtra("end");

        station =gt.getStringExtra("station");

        CustomTextView station_name = (CustomTextView)findViewById(R.id.station_name);
        station_name.setText(station);

        selectDate = start_date;
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        setWidget();

        new getBoadrdList().execute();

    }

    String[] datesss;
    CustomTextView date_sc;

    private void setWidget(){
        ImageView reg = (ImageView)findViewById(R.id.reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gt = new Intent(TripScheduleActivity.this , MainActivity.class);
                gt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gt);
                finish();
            }
        });

        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView backs = (ImageView)findViewById(R.id.backs);
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ints = getIntent();
                if(ints.getStringExtra("tag")==null){
                    finish();
                }else if(ints.getStringExtra("tag").equals("last")){
                    finish();
                }else if(ints.getStringExtra("tag").equals("now")){
                    finish();
                }

            }
        });

        int diffDate=0;
        try{
            diffDate =diffOfDate(start_date.replace("-",""),end_date.replace("-",""));
        }catch (Exception e){

        }


        dateArray.add(start_date);
        for(int i =1; i<= diffDate; i ++){
            dateArray.add(addDate(start_date,i));
        }

        datesss = dateArray.toArray(new String[dateArray.size()]);


        date_sc = (CustomTextView)findViewById(R.id.date_sc);
        date_sc.setText("1일차 " +selectDate);
        ImageView  date_img= (ImageView)findViewById(R.id.date_img);
        date_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(TripScheduleActivity.this);
                builder.setTitle("날짜 선택");
                builder.setItems(datesss, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int pos) {
                        dialog.dismiss();


                        int xx = pos+1;
                        date_sc.setText(xx+"일차 " +datesss[pos]);
                        selectDate=datesss[pos];

                        new getBoadrdList().execute();


                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        date_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(TripScheduleActivity.this);
                builder.setTitle("날짜 선택");
                builder.setItems(datesss, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int pos) {
                        dialog.dismiss();

                        int xx = pos+1;
                        date_sc.setText(xx+"일차 " +datesss[pos]);
                        selectDate=datesss[pos];

                        new getBoadrdList().execute();


                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });


    }


    public  String addDate(String dates , int days){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = df.parse(dates);


            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, days);

            return df.format(cal.getTime());


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }



    ArrayList<String> dateArray = new ArrayList<>();

    public  int diffOfDate(String begin, String end) throws Exception
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        Date beginDate = formatter.parse(begin);
        Date endDate = formatter.parse(end);

        long diff = endDate.getTime() - beginDate.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return (int)diffDays;
    }

    String start_date="";
    String end_date="";
    String lines="";
    String station="";
    String cateCode="";

    private class getBoadrdList extends AsyncTask<Void, Void, Void> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(TripScheduleActivity.this, "", "데이터 처리중....", true);
            }

        }

        @Override
        protected Void doInBackground(Void... params) {

            getStore();

            return null;
        }

        private void getStore() {
            vdata.clear();

            DBHelper helper = new DBHelper(TripScheduleActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();


            String upSql="";
            upSql = "SELECT  * from schedule where station ='"+station+"' and reg_date ='"+selectDate+"' and tag='1'";

            upSql = upSql +" and mem_id ='"+pref.getValue(RbPreference.MEM_ID,"")+"' ";

            upSql = upSql +" order by start_time asc";

            Log.d("myLog"  ,"sql " + upSql);


            try {
                Cursor monthCursor;
                monthCursor = db.rawQuery(upSql, null);

                while (monthCursor.moveToNext()) {
                    String idx = monthCursor.getString(0);
                    String s_idx = monthCursor.getString(1);
                    String title = monthCursor.getString(2);
                    String img = monthCursor.getString(3);
                    String reg_date = monthCursor.getString(4);
                    String start_time = monthCursor.getString(5);
                    String end_time = monthCursor.getString(6);
                    String lat = monthCursor.getString(7);
                    String lng = monthCursor.getString(8);
                    String station = monthCursor.getString(9);
                    String tag = monthCursor.getString(10);
                    String alarm = monthCursor.getString(11);




                    vdata.add(new ScheduleData(idx,s_idx, title, img, reg_date, start_time,
                            end_time, lat, lng, station, tag, alarm));
                }
            } catch (Exception e) {
                Log.e("Thread", "select Error", e);

            } finally {
                helper.close();
                db.close();
            }

        }

        protected void onPostExecute(Void result) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }

            ListView s_list = (ListView) findViewById(R.id.s_list);
            GoodsAdapter mAdapter = new GoodsAdapter(TripScheduleActivity.this,
                    R.layout.listview_visit, vdata);

            s_list.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

            setMap();


        }
    }

    GoogleMap map;

    private void setMap() {



        arrayPoints = new ArrayList<LatLng>();
        map.clear();


        for(int i=0; i < vdata.size();i++){
            LatLng position1 = new LatLng(Double.parseDouble(vdata.get(i).getLat()),
                    Double.parseDouble(vdata.get(i).getLng()));
            map.addMarker(new MarkerOptions().position(position1).title(vdata.get(i).getTitle()).snippet(""));
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.RED);
            polylineOptions.width(5);
            arrayPoints.add(position1);
            polylineOptions.addAll(arrayPoints);
            map.addPolyline(polylineOptions);
        }

        if(vdata.size()>0){
            LatLng position1 = new LatLng(Double.parseDouble(vdata.get(0).getLat()),
                    Double.parseDouble(vdata.get(0).getLng()));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position1, 12));

        }

    }
    private ArrayList<LatLng> arrayPoints;




    public class GoodsAdapter extends ArrayAdapter<ScheduleData> {
        private ArrayList<ScheduleData> items;
        ScheduleData fInfo;

        public GoodsAdapter(Context context, int textViewResourseId,
                            ArrayList<ScheduleData> items) {
            super(context, textViewResourseId, items);
            this.items = items;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            fInfo = items.get(position);

            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listview_sc, null);

            CustomTextView name = (CustomTextView) v.findViewById(R.id.name);
            name.setText(fInfo.getTitle()); //

            CustomTextView time = (CustomTextView) v.findViewById(R.id.time);
            time.setText(fInfo.getStart_time()+"~"+fInfo.getEnd_time()); //



            ImageView img = (ImageView) v.findViewById(R.id.img);
            String filename = fInfo.getImg()+".jpg";

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

            v.setTag(position);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer)v.getTag();
                    Intent gt = new Intent(TripScheduleActivity.this , TripUpdateActivity.class);
                    Bundle ex = new Bundle();
                    ex.putSerializable("list" , items.get(pos));
                    gt.putExtras(ex);
                    gt.putExtra("start" , start_date);
                    gt.putExtra("end" , end_date);
                    gt.putExtra("station" , station);
                    startActivityForResult(gt,0);

                }
            });

            return v;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode==RESULT_OK){

            selectDate = start_date;
            date_sc.setText("1일차 " +selectDate);
            new getBoadrdList().execute();
        }
    }
}
