package apps.com.stations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class LastTripActivity extends Activity {
    RbPreference pref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_trip);
        pref = new RbPreference(this);

        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        new getBoadrdList().execute();


    }



    ArrayList<LastData> tempdata = new ArrayList<LastData>();

    private class getBoadrdList extends AsyncTask<Void, Void, Void> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(LastTripActivity.this, "", "데이터 처리중....", true);
            }

        }

        @Override
        protected Void doInBackground(Void... params) {

            getStore();

            return null;
        }

        private void getStore() {

                tempdata.clear();

                DBHelper helper = new DBHelper(LastTripActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();




                String upSql="";
                upSql = "SELECT  * from schedule ";

                upSql = upSql +" where mem_id ='"+pref.getValue(RbPreference.MEM_ID,"")+"' " +
                       " group by station ";

                Log.d("myLog"  ,"sql " + upSql);


                try {
                    Cursor monthCursor;
                    monthCursor = db.rawQuery(upSql, null);

                    while (monthCursor.moveToNext()) {
                        String station = monthCursor.getString(9);

                        String start_date = monthCursor.getString(13);
                        String end_date = monthCursor.getString(14);

                        Calendar curTime = Calendar.getInstance();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                        String nows = dateFormat.format(curTime.getTime());

                        if(Integer.parseInt(end_date.replace("-","")) < Integer.parseInt(nows) ){
                            tempdata.add(new LastData(station , start_date,end_date));
                        }



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

            GoodsAdapter mAdapter = new GoodsAdapter(LastTripActivity.this,
                    R.layout.listview_visit, tempdata);

            s_list.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();


        }
    }

    private void getRegDatse(String station){
        ArrayList<String> dateList = new ArrayList<String>();
        dateList.clear();

        DBHelper helper = new DBHelper(LastTripActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();


        String upSql="";
        upSql = "SELECT  * from schedule ";

        upSql = upSql +" where mem_id ='"+pref.getValue(RbPreference.MEM_ID,"")+"' and station ='"+station+"' " +
                "order by reg_date asc";

        Log.d("myLog"  ,"sql " + upSql);


        try {
            Cursor monthCursor;
            monthCursor = db.rawQuery(upSql, null);

            while (monthCursor.moveToNext()) {

                String reg_date = monthCursor.getString(4);



                dateList.add(reg_date);


            }
        } catch (Exception e) {
            Log.e("Thread", "select Error", e);

        } finally {
            helper.close();
            db.close();
        }





        if(dateList.size()>0){
            String lastTime = dateList.get(dateList.size()-1).toString().replace("-","");
        }


    }

    public class GoodsAdapter extends ArrayAdapter<LastData> {
        private ArrayList<LastData> items;
        LastData fInfo;

        public GoodsAdapter(Context context, int textViewResourseId,
                            ArrayList<LastData> items) {
            super(context, textViewResourseId, items);
            this.items = items;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            fInfo = items.get(position);

            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listview_last, null);

            CustomTextView name = (CustomTextView) v.findViewById(R.id.name);
            name.setText(fInfo.getStation()); //

            CustomTextView desc = (CustomTextView) v.findViewById(R.id.desc);
            desc.setText(fInfo.getStart_date() +"~"+ fInfo.getEnd_date());


            v.setTag(position);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer)v.getTag();
                    Intent gt = new Intent(LastTripActivity.this , LastTripScActivity.class);
                    gt.putExtra("station" , items.get(pos).getStation());
                    gt.putExtra("start" , items.get(pos).getStart_date());
                    gt.putExtra("end" , items.get(pos).getEnd_date());
                    gt.putExtra("tag" , "last");
                    startActivity(gt);
                }
            });

            return v;
        }
    }



}
