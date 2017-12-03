package apps.com.stations;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.navdrawer.SimpleSideDrawer;

import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;


public class MainActivity extends Activity  {

    RbPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        pref = new RbPreference(this);

        if (pref.getValue(RbPreference.isFirst, true)) {
            new getExcelData().execute();
        }

        setWidget();





    }

    private void setWidget(){
        Button btn01 = (Button)findViewById(R.id.btn01);
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref.getValue(RbPreference.ISLOGIN,false)){
                    Intent gt = new Intent(MainActivity.this, NowTripActivity.class);
                    gt.putExtra("now" ,"now");
                    startActivity(gt);
                }else{
                    Intent gt = new Intent(MainActivity.this, LogInActivity.class);
                    startActivityForResult(gt,1);

                }

            }
        });




        Button btn02 = (Button)findViewById(R.id.btn02);
        btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref.getValue(RbPreference.ISLOGIN,false)){
                    Intent gt = new Intent(MainActivity.this, TripStartActivity.class);
                    startActivity(gt);
                }else{
                    Intent gt = new Intent(MainActivity.this, LogInActivity.class);

                    startActivityForResult(gt,1);


                }

            }
        });

        Button btn03 = (Button)findViewById(R.id.btn03);
        btn03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gt = new Intent(MainActivity.this, VisitActivity.class);
                startActivity(gt);
            }
        });



        mNav = new SimpleSideDrawer(this);
        Button btn_menu = (Button) findViewById(R.id.menu);
        mNav.setRightBehindContentView(R.layout.menu_right);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav.toggleRightDrawer();
            }
        });



        CustomTextView mypage = (CustomTextView)findViewById(R.id.mypage);
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav.toggleRightDrawer();

                if(pref.getValue(RbPreference.ISLOGIN,false)){
                    Intent gt = new Intent(MainActivity.this, UpdateUserActivity.class);
                    startActivity(gt);
                }else{
                    Intent gt = new Intent(MainActivity.this, LogInActivity.class);
                    startActivityForResult(gt,1);

                }

            }
        });


        CustomTextView now_course = (CustomTextView)findViewById(R.id.now_course);
        now_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav.toggleRightDrawer();

                if(pref.getValue(RbPreference.ISLOGIN,false)){
                    Intent gt = new Intent(MainActivity.this, NowTripActivity.class);
                    gt.putExtra("now" ,"now");
                    startActivity(gt);
                }else{
                    Intent gt = new Intent(MainActivity.this, LogInActivity.class);
                    startActivityForResult(gt,1);

                }

            }
        });

        CustomTextView last_course = (CustomTextView)findViewById(R.id.last_course);
        last_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav.toggleRightDrawer();

                if(pref.getValue(RbPreference.ISLOGIN,false)){
                    Intent gt = new Intent(MainActivity.this, LastTripActivity.class);
                    startActivity(gt);
                }else{
                    Intent gt = new Intent(MainActivity.this, LogInActivity.class);
                    startActivityForResult(gt,1);

                }

            }
        });

        CustomTextView notice = (CustomTextView)findViewById(R.id.notice);
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gt = new Intent(MainActivity.this, NoticeActivity.class);
                startActivity(gt);

            }
        });




        logout = (CustomTextView)findViewById(R.id.logout);
        if(pref.getValue(RbPreference.ISLOGIN,false)){
            logout.setText("로그아웃");
        }else{

            logout.setText("로그인");
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav.toggleRightDrawer();
                if(pref.getValue(RbPreference.ISLOGIN,false)){
                    AlertDialog.Builder adialog = new AlertDialog.Builder(MainActivity.this);
                    adialog.setMessage("로그아웃 하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    pref.put(RbPreference.ISLOGIN,false);


                                    logout.setText("로그인");

                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = adialog.create();
                    alert.setTitle("로그아웃");
                    alert.show();

                }else{
                    Intent gt = new Intent(MainActivity.this, LogInActivity.class);
                    startActivityForResult(gt,1);
                }

            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            logout.setText("로그아웃");
        }
    }

    CustomTextView logout;

    SimpleSideDrawer mNav;

    ProgressDialog mProgressDialog;

    private class getExcelData extends AsyncTask<Void, Void, Void> {
        // DB로 복사

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(MainActivity.this, "",
                    "데이터 복사 중....", true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            getExcelData();

            return null;
        }

        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();
            pref.put(RbPreference.isFirst, false);

        }
    }

    private void getExcelData() {
        Workbook workbook = null;
        Sheet sheet = null;

        DBHelper helper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();


        try {
            InputStream is = getBaseContext().getResources().getAssets()
                    .open("visit.xls");
            workbook = Workbook.getWorkbook(is);

            if (workbook != null) {





                sheet = workbook.getSheet(0);

                if (sheet != null) {

                    int nMaxColumn = 14; // 총 칼럼개수 14개
                    int nRowStartIndex = 1;// 시작 row 는 1
                    int nRowEndIndex = sheet.getColumn(nMaxColumn - 1).length - 1;
                    int nColumnStartIndex = 0; // 시작 col 은 0

                    Log.d("myLog" ,"nRowEndIndex " + nRowEndIndex);

                    nRowEndIndex=328; //엑셀의 마지막 번호

                    for (int nRow = nRowStartIndex; nRow <= nRowEndIndex; nRow++) {

                        ContentValues row;
                        row = new ContentValues();

                        row.put("lat", sheet.getCell(nColumnStartIndex, nRow)
                                .getContents());
                        row.put("lng",
                                sheet.getCell(nColumnStartIndex + 1, nRow)
                                        .getContents());

                        row.put("cate",
                                sheet.getCell(nColumnStartIndex + 2, nRow)
                                        .getContents());

                        row.put("name",
                                sheet.getCell(nColumnStartIndex + 3, nRow)
                                        .getContents());

                        Log.d("myLog" ,"name " + sheet.getCell(nColumnStartIndex + 3, nRow)
                                .getContents());

                        row.put("line",
                                sheet.getCell(nColumnStartIndex + 4, nRow)
                                        .getContents());
                        row.put("station",
                                sheet.getCell(nColumnStartIndex + 5, nRow)
                                        .getContents());

                        row.put("phone",
                                sheet.getCell(nColumnStartIndex + 6, nRow)
                                        .getContents());
                        row.put("addr",
                                sheet.getCell(nColumnStartIndex + 7, nRow)
                                        .getContents());
                        row.put("desc1",
                                sheet.getCell(nColumnStartIndex + 8, nRow)
                                        .getContents());

                        row.put("desc2",
                                sheet.getCell(nColumnStartIndex + 9, nRow)
                                        .getContents());


                        row.put("hit",
                                sheet.getCell(nColumnStartIndex + 10, nRow)
                                        .getContents());

                        row.put("favor",
                                sheet.getCell(nColumnStartIndex + 11, nRow)
                                        .getContents());

                        row.put("img",
                                sheet.getCell(nColumnStartIndex + 12, nRow)
                                        .getContents());



                        db.insert("visit", null, row);

                    }

                }



            }

            else {

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("myLog" , "e " +e.toString());
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

}


