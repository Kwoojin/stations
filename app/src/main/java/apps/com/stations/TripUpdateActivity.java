
package apps.com.stations;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TripUpdateActivity extends Activity {
    RbPreference pref;
    ArrayList<ScheduleData> vdata = new ArrayList<ScheduleData>();
    String cateStr="favor";

    ScheduleData sdata;
    String start_date="";
    String end_date="";

    String station="";
    String[] datesss;
    CustomTextView date_sc;
    String alarmStrList[]={"Off","On"};
    String alarmList[]={"알림없음","알림"};
    String idx="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_update);
        pref = new RbPreference(this);

        Intent gt = getIntent();
        sdata = (ScheduleData)gt.getSerializableExtra("list");

        idx = sdata.getIdx();
        start_date =gt.getStringExtra("start");
        end_date =gt.getStringExtra("end");

        station =gt.getStringExtra("station");

        CustomTextView station_name = (CustomTextView)findViewById(R.id.station_name);
        station_name.setText(sdata.getTitle());

        CustomTextView info = (CustomTextView)findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new getList().execute();

            }
        });


        new getBoadrdList().execute();
        ImageView img = (ImageView) findViewById(R.id.update_img);
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




    }


    private class getList extends AsyncTask<Void, Void, Void> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(TripUpdateActivity.this, "", "데이터 처리중....", true);
            }

        }

        @Override
        protected Void doInBackground(Void... params) {

            getStore();

            return null;
        }

        private void getStore() {

            DBHelper helper = new DBHelper(TripUpdateActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();
  

            String upSql="";
            upSql = "SELECT  * from visit where _id='"+sdata.getS_idx()+"'";



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



                    vdatas= new VisitData(idx, lat, lng, cate, name,
                            line, station, phone, addr, desc1, desc2, hit, favor, img);
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

            Intent gt = new Intent(TripUpdateActivity.this , VisitDetailActivity.class);
            Bundle ex = new Bundle();
            ex.putSerializable("list" , vdatas);
            gt.putExtras(ex);
            startActivity(gt);


        }
    }

    VisitData vdatas;

    private class getBoadrdList extends AsyncTask<Void, Void, Void> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(TripUpdateActivity.this, "", "데이터 처리중....", true);
            }

        }

        @Override
        protected Void doInBackground(Void... params) {

            getStore();

            return null;
        }

        private void getStore() {
            vdata.clear();

            DBHelper helper = new DBHelper(TripUpdateActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();


            String upSql="";
            upSql = "SELECT  * from schedule where _id ='"+idx+"' ";



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



            setWidget();

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
        date_sc.setText(vdata.get(0).getReg_date());
        ImageView  date_img= (ImageView)findViewById(R.id.date_img);
        date_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(TripUpdateActivity.this);
                builder.setTitle("날짜 선택");
                builder.setItems(datesss, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int pos) {
                        dialog.dismiss();
                        date_sc.setText(datesss[pos]);
                        start_time.setText("");
                        end_time.setText("");


                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        date_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(TripUpdateActivity.this);
                builder.setTitle("날짜 선택");
                builder.setItems(datesss, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int pos) {
                        dialog.dismiss();
                        date_sc.setText(datesss[pos]);
                        start_time.setText("");
                        end_time.setText("");


                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });


        start_time = (CustomTextView)findViewById(R.id.start_time);
        start_time.setText(vdata.get(0).getStart_time());
        ImageView  start_img= (ImageView)findViewById(R.id.start_img);

        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(TripUpdateActivity.this, timeSetListener1, 12, 00, false).show();
            }
        });
        start_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(TripUpdateActivity.this, timeSetListener1, 12, 00, false).show();
            }
        });


        end_time = (CustomTextView)findViewById(R.id.end_time);
        end_time.setText(vdata.get(0).getEnd_time());
        ImageView  end_img= (ImageView)findViewById(R.id.end_img);

        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(start_time.getText().toString())){
                    Toast.makeText(TripUpdateActivity.this , "시작시간을 먼저 선택하세요" , Toast.LENGTH_SHORT).show();
                }else{
                    new TimePickerDialog(TripUpdateActivity.this, timeSetListener2, 12, 00, false).show();
                }

            }
        });
        end_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(start_time.getText().toString())){
                    Toast.makeText(TripUpdateActivity.this , "시작시간을 먼저 선택하세요" , Toast.LENGTH_SHORT).show();
                }else{
                    new TimePickerDialog(TripUpdateActivity.this, timeSetListener2, 12, 00, false).show();
                }
            }
        });

        final CustomTextView alarm = (CustomTextView)findViewById(R.id.alarm);
        ImageView  alarm_img= (ImageView)findViewById(R.id.alarm_img);


        if(vdata.get(0).getAlarm().equals("Off")){
            alarm.setText("알림없음");
            alarmStr="Off";
        }else{
            alarm.setText("알림");
            alarmStr="On";
        }


        alarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(TripUpdateActivity.this);
                builder.setTitle("알림 선택");
                builder.setItems(alarmList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int pos) {
                        dialog.dismiss();

                        alarm.setText(alarmList[pos]);
                        alarmStr =alarmStrList[pos];


                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });

        alarm_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(TripUpdateActivity.this);
                builder.setTitle("알림 선택");
                builder.setItems(alarmList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int pos) {
                        dialog.dismiss();

                        alarm.setText(alarmList[pos]);
                        alarmStr =alarmStrList[pos];



                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });

        CustomTextView del = (CustomTextView) findViewById(R.id.del);
        del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder adialog = new AlertDialog.Builder(TripUpdateActivity.this);
                adialog.setMessage("스케줄 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                deleteSC();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = adialog.create();
                alert.setTitle("스케줄 삭제");
                alert.show();

            }
        });


        CustomTextView add = (CustomTextView) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(date_sc.getText().toString())) {
                    Toast.makeText(TripUpdateActivity.this, "여행날짜 선택 하세요", Toast.LENGTH_SHORT).show();
                } else  if (TextUtils.isEmpty(start_time.getText().toString())) {
                    Toast.makeText(TripUpdateActivity.this, "시작시간을 입력하세요", Toast.LENGTH_SHORT).show();
                }else  if (TextUtils.isEmpty(end_time.getText().toString())) {
                    Toast.makeText(TripUpdateActivity.this, "종료시간을 입력하세요", Toast.LENGTH_SHORT).show();
                }else  if (TextUtils.isEmpty(alarm.getText().toString())) {
                    Toast.makeText(TripUpdateActivity.this, "알람을 선택 하세요", Toast.LENGTH_SHORT).show();
                }else{

                    insertSc();
                }


            }
        });

    }
    CustomTextView start_time;
    CustomTextView end_time;
    String alarmStr="";


    private void insertSc() {


        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();


        try {
            ContentValues row;
            row = new ContentValues();

            row.put("title", sdata.getTitle());
            row.put("img", sdata.getImg());
            row.put("reg_date", date_sc.getText().toString());
            row.put("start_time", start_time.getText().toString());
            row.put("end_time", end_time.getText().toString());
            row.put("lat", sdata.getLat());
            row.put("lng", sdata.getLng());
            row.put("station", station);
            row.put("alarm", alarmStr);


            db.update("schedule", row, "_id=?",new String[]{idx});



        } catch (Exception e) {
            Log.e("Thread", "Insert Error", e);

        } finally {
            helper.close();
            db.close();

            setResult(RESULT_OK);
            finish();




        }
    }

    private void deleteSC() {


        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();


        try {


            db.delete("schedule", "_id=?",new String[]{idx});



        } catch (Exception e) {
            Log.e("Thread", "Insert Error", e);

        } finally {
            helper.close();
            db.close();

            setResult(RESULT_OK);
            finish();




        }
    }


    private TimePickerDialog.OnTimeSetListener timeSetListener1 = new TimePickerDialog.OnTimeSetListener() {



        @Override

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            // TODO Auto-generated method stub

            String min="";

            if(hourOfDay<10){
                min="0"+hourOfDay;
            }else{
                min = ""+hourOfDay;
            }

            String minutes="";
            if(minute<10){
                minutes="0"+minute;
            }else{
                minutes = ""+minute;
            }

            if(checkTime(min+":"+minutes)){
                start_time.setText(min+":"+minutes);

            }else{
                Toast.makeText(TripUpdateActivity.this, "해당 시간에 스케줄이 있습니다.", Toast.LENGTH_SHORT).show();
            }



        }

    };

    private boolean checkTime(String nowTime){
        boolean isCheck=true;

        DBHelper helper = new DBHelper(TripUpdateActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();




        String upSql="";
        upSql = "SELECT  * from schedule ";

        upSql = upSql +" where reg_date ='"+date_sc.getText().toString()+"' " ;

        Log.d("myLog"  ,"sql " + upSql);


        try {
            Cursor monthCursor;
            monthCursor = db.rawQuery(upSql, null);

            while (monthCursor.moveToNext()) {

                String start_time = monthCursor.getString(5);
                String end_time = monthCursor.getString(6);

                int start = Integer.parseInt(start_time.replace(":",""));
                int end= Integer.parseInt(end_time.replace(":",""));
                int now = Integer.parseInt(nowTime.replace(":",""));


                if( (now>=start) && (now <=end) ){
                    isCheck=false;
                    break;

                }



            }
        } catch (Exception e) {
            Log.e("Thread", "select Error", e);

        } finally {
            helper.close();
            db.close();
        }

        return isCheck;

    }


    private TimePickerDialog.OnTimeSetListener timeSetListener2 = new TimePickerDialog.OnTimeSetListener() {



        @Override

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            // TODO Auto-generated method stub

            String min="";

            if(hourOfDay<10){
                min="0"+hourOfDay;
            }else{
                min = ""+hourOfDay;
            }

            String minutes="";
            if(minute<10){
                minutes="0"+minute;
            }else{
                minutes = ""+minute;
            }

            if(checkTime(min+":"+minutes)){
                end_time.setText(min+":"+minutes);

            }else{
                Toast.makeText(TripUpdateActivity.this, "해당 시간에 스케줄이 있습니다.", Toast.LENGTH_SHORT).show();
            }





        }

    };


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












}
