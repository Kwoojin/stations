package apps.com.stations;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
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

public class TripAddActivity extends Activity {
    RbPreference pref;
    ArrayList<VisitData> vdata = new ArrayList<VisitData>();

    VisitData sdata;
    String start_date="";
    String end_date="";

    String station="";
    String[] datesss;
    CustomTextView date_sc;
    String alarmStrList[]={"Off","On"};
    String alarmList[]={"알림없음","알림"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_add);
        pref = new RbPreference(this);

        Intent gt = getIntent();
        sdata = (VisitData)gt.getSerializableExtra("list");

        start_date =gt.getStringExtra("start");
        end_date =gt.getStringExtra("end");

        station =gt.getStringExtra("station");


        CustomTextView station_name = (CustomTextView)findViewById(R.id.station_name);
        station_name.setText(sdata.getName());
        ImageView img = (ImageView) findViewById(R.id.add_img);
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

        setWidget();

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
        ImageView  date_img= (ImageView)findViewById(R.id.date_img);
        date_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(TripAddActivity.this);
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


                AlertDialog.Builder builder = new AlertDialog.Builder(TripAddActivity.this);
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
        final ImageView  start_img= (ImageView)findViewById(R.id.start_img);

        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(date_sc.getText().toString())){
                    Toast.makeText(TripAddActivity.this , "날짜를 먼저 선택하세요" , Toast.LENGTH_SHORT).show();
                }else{
                    new TimePickerDialog(TripAddActivity.this, timeSetListener1, 12, 00, false).show();
                }

            }
        });
        start_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(date_sc.getText().toString())){
                    Toast.makeText(TripAddActivity.this , "날짜를 먼저 선택하세요" , Toast.LENGTH_SHORT).show();
                }else{
                    new TimePickerDialog(TripAddActivity.this, timeSetListener1, 12, 00, false).show();
                }
            }
        });


        end_time = (CustomTextView)findViewById(R.id.end_time);
        ImageView  end_img= (ImageView)findViewById(R.id.end_img);

        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(date_sc.getText().toString())){
                    new TimePickerDialog(TripAddActivity.this, timeSetListener2, 12, 00, false).show();
                }else if(TextUtils.isEmpty(start_time.getText().toString())){
                    Toast.makeText(TripAddActivity.this , "시작시간을 먼저 선택하세요" , Toast.LENGTH_SHORT).show();
                }else{
                    new TimePickerDialog(TripAddActivity.this, timeSetListener2, 12, 00, false).show();
                }

            }
        });
        end_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(date_sc.getText().toString())){
                    new TimePickerDialog(TripAddActivity.this, timeSetListener2, 12, 00, false).show();
                }else if(TextUtils.isEmpty(start_time.getText().toString())){
                    Toast.makeText(TripAddActivity.this , "시작시간을 먼저 선택하세요" , Toast.LENGTH_SHORT).show();
                }else{
                    new TimePickerDialog(TripAddActivity.this, timeSetListener2, 12, 00, false).show();
                }
            }
        });

        final CustomTextView alarm = (CustomTextView)findViewById(R.id.alarm); //알람설정
        ImageView  alarm_img= (ImageView)findViewById(R.id.alarm_img);

        alarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(TripAddActivity.this);
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


                AlertDialog.Builder builder = new AlertDialog.Builder(TripAddActivity.this);
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

        ImageView cancel = (ImageView) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });


        ImageView add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(date_sc.getText().toString())) {
                    Toast.makeText(TripAddActivity.this, "여행날짜 선택 하세요", Toast.LENGTH_SHORT).show();
                } else  if (TextUtils.isEmpty(start_time.getText().toString())) {
                    Toast.makeText(TripAddActivity.this, "시작시간을 입력하세요", Toast.LENGTH_SHORT).show();
                }else  if (TextUtils.isEmpty(end_time.getText().toString())) {
                    Toast.makeText(TripAddActivity.this, "종료시간을 입력하세요", Toast.LENGTH_SHORT).show();
                }else  if (TextUtils.isEmpty(alarm.getText().toString())) {
                    Toast.makeText(TripAddActivity.this, "알람을 선택 하세요", Toast.LENGTH_SHORT).show();
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
            row.put("s_idx", sdata.getIdx());
            row.put("title", sdata.getName());
            row.put("img", sdata.getImg());
            row.put("reg_date", date_sc.getText().toString());
            row.put("start_time", start_time.getText().toString());
            row.put("end_time", end_time.getText().toString());
            row.put("lat", sdata.getLat());
            row.put("lng", sdata.getLng());
            row.put("station", station);
            row.put("tag", "1");
            row.put("alarm", alarmStr);
            row.put("mem_id", pref.getValue(RbPreference.MEM_ID,""));
            row.put("start_date", start_date);
            row.put("end_date", end_date);



            db.insert("schedule", null, row);



        } catch (Exception e) {
            Log.e("Thread", "Insert Error", e);

        } finally {
            helper.close();
            db.close();



            if(alarmStr.equals("On")){
                registAlarm(start_time.getText().toString()+ ":00" , date_sc.getText().toString());
            }


            Intent gt = new Intent(TripAddActivity.this , TripDetailActivity.class);

            gt.putExtra("tag" , "pass");
            gt.putExtra("station" , station);
            gt.putExtra("start" , start_date);
            gt.putExtra("end" , end_date);
            gt.putExtra("now_date" , date_sc.getText().toString());
            gt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(gt);
            finish();

        }
    }

    private int getUnioqueIdx() {
        String idx = "";

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String upSql = "SELECT  _id  FROM schedule order by _id desc Limit 1";

        try {
            Cursor monthCursor;
            monthCursor = db.rawQuery(upSql, null);

            while (monthCursor.moveToNext()) {
                idx = monthCursor.getString(0);

            }
        } catch (Exception e) {
            Log.e("Thread", "select Error", e);

        } finally {
            helper.close();
            db.close();
        }

        return Integer.parseInt(idx);
    }


    private void registAlarm(String time,String dateStr)
    {
        int id = getUnioqueIdx();

        Intent intent = new Intent(TripAddActivity.this, AlarmRecever.class);

        intent.putExtra("idx", id);


        PendingIntent sender = PendingIntent.getBroadcast(
                TripAddActivity.this, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(dateStr + " " + time);


            long atime = System.currentTimeMillis();

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            long minusTime = 60*1000*60;


            long btime = cal.getTimeInMillis()-minusTime;

            if (atime > btime){

            }else{
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()-minusTime, sender);
            }




        } catch (ParseException e) {
            e.printStackTrace();
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
                Toast.makeText(TripAddActivity.this, "해당 시간에 스케줄이 있습니다.", Toast.LENGTH_SHORT).show();
            }




        }

    };

    private boolean checkTime(String nowTime){
        boolean isCheck=true;

        DBHelper helper = new DBHelper(TripAddActivity.this);
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
                Toast.makeText(TripAddActivity.this, "해당 시간에 스케줄이 있습니다.", Toast.LENGTH_SHORT).show();
            }



        }

    };


    public  String addDate(String dates , int days){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = df.parse(dates);

            // 날짜 더하기
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
