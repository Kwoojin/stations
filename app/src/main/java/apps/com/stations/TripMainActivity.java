package apps.com.stations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TripMainActivity extends Activity {
    RbPreference pref;
    ArrayList<VisitData> vdata = new ArrayList<VisitData>();
    String cateStr="favor";
    EditText secrch_txt;
    String secrch_keyword="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_trip_main);
        pref = new RbPreference(this);



        Intent gt = getIntent();
        start_date =gt.getStringExtra("start");
        end_date =gt.getStringExtra("end");
        lines =gt.getStringExtra("line");
        station =gt.getStringExtra("station");

        setWidget();
        CustomTextView station_name = (CustomTextView)findViewById(R.id.station_name);
        station_name.setText(station);

        new getBoadrdList().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();

        DBHelper helper = new DBHelper(TripMainActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();


        try {
            db.delete("schedule", "tag=?",new String[]{"0"});

        } catch (Exception e) {
            Log.e("Thread", "select Error", e);

        } finally {
            helper.close();
            db.close();
        }



}

    private void setWidget(){

        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        ImageView sc = (ImageView)findViewById(R.id.sc);
        sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gt = new Intent(TripMainActivity.this , TripScheduleActivity.class);
                gt.putExtra("start" , start_date);
                gt.putExtra("end" , end_date);
                gt.putExtra("station" , station);
                startActivity(gt);
            }
        });

        ImageView reg = (ImageView)findViewById(R.id.reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gt = new Intent(TripMainActivity.this , MainActivity.class);
                gt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gt);
                finish();
            }
        });





        secrch_txt = (EditText)findViewById(R.id.secrch_txt);
        secrch_txt.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        secrch_txt.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    secrch_keyword = secrch_txt.getText().toString();
                    InputMethodManager keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    new getBoadrdList().execute();

                }
                return false;
            }
        });




        ImageView del = (ImageView)findViewById(R.id.del);
        del.setOnClickListener(new View.OnClickListener() { //검색어 삭제
            @Override
            public void onClick(View v) {
                secrch_txt.setText("");
            }
        });



        ImageView all = (ImageView)findViewById(R.id.all);
        all.setOnClickListener(new View.OnClickListener() { //검색어 삭제
            @Override
            public void onClick(View v) {

                secrch_txt.setText("");
                secrch_keyword = secrch_txt.getText().toString();
                cateCode="";
                new getBoadrdList().execute();
            }
        });

        ImageView food = (ImageView)findViewById(R.id.food);
        food.setOnClickListener(new View.OnClickListener() { //검색어 삭제
            @Override
            public void onClick(View v) {

                secrch_txt.setText("");
                secrch_keyword = secrch_txt.getText().toString();
                cateCode="식사";
                new getBoadrdList().execute();
            }
        });

        ImageView cafe = (ImageView)findViewById(R.id.cafe);
        cafe.setOnClickListener(new View.OnClickListener() { //검색어 삭제
            @Override
            public void onClick(View v) {

                secrch_txt.setText("");
                secrch_keyword = secrch_txt.getText().toString();
                cateCode="카페";
                new getBoadrdList().execute();
            }
        });

        ImageView lesier = (ImageView)findViewById(R.id.lesier);
        lesier.setOnClickListener(new View.OnClickListener() { //검색어 삭제
            @Override
            public void onClick(View v) {

                secrch_txt.setText("");
                secrch_keyword = secrch_txt.getText().toString();
                cateCode="레저";
                new getBoadrdList().execute();
            }
        });

        ImageView nature = (ImageView)findViewById(R.id.nature);
        nature.setOnClickListener(new View.OnClickListener() { //검색어 삭제
            @Override
            public void onClick(View v) {

                secrch_txt.setText("");
                secrch_keyword = secrch_txt.getText().toString();
                cateCode="자연";
                new getBoadrdList().execute();
            }
        });

        ImageView culture = (ImageView)findViewById(R.id.culture);
        culture.setOnClickListener(new View.OnClickListener() { //검색어 삭제
            @Override
            public void onClick(View v) {

                secrch_txt.setText("");
                secrch_keyword = secrch_txt.getText().toString();
                cateCode="문화";
                new getBoadrdList().execute();
            }
        });


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
                mProgressDialog = ProgressDialog.show(TripMainActivity.this, "", "데이터 처리중....", true);
            }

        }

        @Override
        protected Void doInBackground(Void... params) {

            getStore();

            return null;
        }

        private void getStore() {
            vdata.clear();

            DBHelper helper = new DBHelper(TripMainActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase(); // DB를 오픈한다


            String upSql="";
            upSql = "SELECT  * from visit where station ='"+station+"' ";


            if(secrch_keyword.equals("")){


            }else{
                upSql = upSql +" and name like '%" + secrch_keyword +"%'";
            }

            if(cateCode.equals("")){


            }else{
                upSql = upSql +" and cate ='"+cateCode+"' ";
            }


            upSql = upSql +" order by favor desc";

            Log.d("myLog"  ,"sql " + upSql);


            try {
                Cursor monthCursor;
                monthCursor = db.rawQuery(upSql, null);

                while (monthCursor.moveToNext()) {
                    String idx = monthCursor.getString(0);
                    String lat = monthCursor.getString(1);
                    String lng = monthCursor.getString(2);
                    String cate = monthCursor.getString(3);
                    String name = monthCursor.getString(4);
                    String line = monthCursor.getString(5);
                    String station = monthCursor.getString(6);
                    String phone = monthCursor.getString(7);
                    String addr = monthCursor.getString(8);
                    String desc1 = monthCursor.getString(9);
                    String desc2 = monthCursor.getString(10);
                    String hit = monthCursor.getString(11);
                    String favor = monthCursor.getString(12);
                    String img = monthCursor.getString(13);



                    vdata.add(new VisitData(idx, lat, lng, cate, name,
                            line, station, phone, addr, desc1, desc2, hit, favor, img));
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




            GoodsAdapter mAdapter = new GoodsAdapter(TripMainActivity.this,
                    R.layout.listview_visit, vdata); //리스트뷰에 연결

            s_list.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();


        }
    }

    public class GoodsAdapter extends ArrayAdapter<VisitData> {
        private ArrayList<VisitData> items;
        VisitData fInfo;

        public GoodsAdapter(Context context, int textViewResourseId,
                            ArrayList<VisitData> items) {
            super(context, textViewResourseId, items);
            this.items = items;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            fInfo = items.get(position);

            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listview_visit, null);

            CustomTextView name = (CustomTextView) v.findViewById(R.id.name);
            name.setText(fInfo.getName()); //

            CustomTextView desc = (CustomTextView) v.findViewById(R.id.desc);
            desc.setText(fInfo.getDesc1()); //



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
                        Intent gt = new Intent(TripMainActivity.this , TripDetailActivity.class);
                        Bundle ex = new Bundle();
                        ex.putSerializable("list" , items.get(pos));
                        gt.putExtras(ex);
                        gt.putExtra("start" , start_date);
                        gt.putExtra("end" , end_date);
                        gt.putExtra("station" , station);
                        startActivity(gt);

                }
            });

            return v;
        }
    }
}
