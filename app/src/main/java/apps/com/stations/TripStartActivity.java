package apps.com.stations;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TripStartActivity extends Activity {
    RbPreference pref;
    ArrayList<BoardData> storedata = new ArrayList<BoardData>();
    String theme="";


    String lineArray[]={"경춘선","경부선","영동선","중앙선"};
    String lineStrings[]={"cate01","cate02","cate03","cate04"};
    String cate01[]={"춘천역","가평역","강촌역"};
    String cate02[]={"안양역","부산역","수원역"};
    String cate03[]={"동해역","정동진역","강릉역"};
    String cate04[]={"용문역","안동역","단양역"};

    String lineCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_trip);
        pref = new RbPreference(this);

        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setWidget();


    }

    CustomTextView start_date;
    CustomTextView end_date;

    CustomTextView lines;
    CustomTextView station;

    private void setWidget() {
        ImageView start = (ImageView) findViewById(R.id.start);
        ImageView end = (ImageView) findViewById(R.id.end);

         start_date = (CustomTextView) findViewById(R.id.start_date);
         end_date = (CustomTextView) findViewById(R.id.end_date);
        lines = (CustomTextView) findViewById(R.id.lines);
        station = (CustomTextView) findViewById(R.id.station);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);

                String strCurYear = new SimpleDateFormat("yyyy").format(date);
                String strCurMon = new SimpleDateFormat("MM").format(date);
                String strCurday = new SimpleDateFormat("dd").format(date);

                DatePickerDialog dialog = new DatePickerDialog(TripStartActivity.this, listener, Integer.parseInt(strCurYear),
                        Integer.parseInt(strCurMon) - 1, Integer.parseInt(strCurday));
                dialog.show();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);

                String strCurYear = new SimpleDateFormat("yyyy").format(date);
                String strCurMon = new SimpleDateFormat("MM").format(date);
                String strCurday = new SimpleDateFormat("dd").format(date);

                DatePickerDialog dialog = new DatePickerDialog(TripStartActivity.this, listener1, Integer.parseInt(strCurYear),
                        Integer.parseInt(strCurMon) - 1, Integer.parseInt(strCurday));
                dialog.show();
            }
        });

        station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(TextUtils.isEmpty(lines.getText().toString())){
                  Toast.makeText(TripStartActivity.this,"노선을 먼저 선택하세요." , Toast.LENGTH_SHORT).show();
              }else{
                  AlertDialog.Builder builder = new AlertDialog.Builder(TripStartActivity.this);
                  builder.setTitle("역 선택");
                  if(lineCode.equals("cate01")){
                      builder.setItems(cate01, new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int pos) {
                              dialog.dismiss();
                              station.setText(cate01[pos]);

                          }
                      });
                  }else   if(lineCode.equals("cate02")){
                      builder.setItems(cate02, new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int pos) {
                              dialog.dismiss();
                              station.setText(cate02[pos]);

                          }
                      });
                  }else   if(lineCode.equals("cate03")){
                      builder.setItems(cate03, new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int pos) {
                              dialog.dismiss();
                              station.setText(cate03[pos]);

                          }
                      });
                  }else   if(lineCode.equals("cate04")){
                      builder.setItems(cate04, new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int pos) {
                              dialog.dismiss();
                              station.setText(cate04[pos]);

                          }
                      });
                  }

                  AlertDialog alert = builder.create();
                  alert.show();
              }



            }
        });

        lines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                station.setText("");
                AlertDialog.Builder builder = new AlertDialog.Builder(TripStartActivity.this);
                builder.setTitle("노선선택");
                builder.setItems(lineArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int pos) {
                        dialog.dismiss();
                        lines.setText(lineArray[pos]);
                        lineCode=lineStrings[pos];

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        ImageView reg = (ImageView) findViewById(R.id.reg);
        reg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(start_date.getText().toString())) {
                    Toast.makeText(TripStartActivity.this, "여행시작일을 입력하세요", Toast.LENGTH_SHORT).show();
                } else  if (TextUtils.isEmpty(end_date.getText().toString())) {
                    Toast.makeText(TripStartActivity.this, "여행종료일을 입력하세요", Toast.LENGTH_SHORT).show();
                }else  if (TextUtils.isEmpty(lines.getText().toString())) {
                    Toast.makeText(TripStartActivity.this, "노선을 입력하세요", Toast.LENGTH_SHORT).show();
                }else  if (TextUtils.isEmpty(station.getText().toString())) {
                    Toast.makeText(TripStartActivity.this, "역을 입력하세요", Toast.LENGTH_SHORT).show();
                }else{

                    Intent gt = new Intent(TripStartActivity.this , TripMainActivity.class);
                    gt.putExtra("start" , start_date.getText().toString());
                    gt.putExtra("end" , end_date.getText().toString());
                    gt.putExtra("line" , lines.getText().toString());
                    gt.putExtra("station" , station.getText().toString());

                    startActivity(gt);
                }

            }
        });

    }


    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int mon = monthOfYear + 1;
            int days = dayOfMonth;

            String str1 = "";
            if (mon < 10) {
                str1 = "0" + mon;
            } else {
                str1 = Integer.toString(mon);
            }

            String str2 = "";
            if (days < 10) {
                str2 = "0" + days;
            } else {
                str2 = Integer.toString(days);
            }

            start_date.setText(year + "-" + str1 + "-" + str2);

        }
    };

    private DatePickerDialog.OnDateSetListener listener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int mon = monthOfYear + 1;
            int days = dayOfMonth;

            String str1 = "";
            if (mon < 10) {
                str1 = "0" + mon;
            } else {
                str1 = Integer.toString(mon);
            }

            String str2 = "";
            if (days < 10) {
                str2 = "0" + days;
            } else {
                str2 = Integer.toString(days);
            }

            end_date.setText(year + "-" + str1 + "-" + str2);

        }


    };








}
