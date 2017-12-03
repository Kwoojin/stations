package apps.com.stations;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class VisitActivity extends Activity {
    RbPreference pref;
    ArrayList<VisitData> vdata = new ArrayList<VisitData>();
    String cateStr="favor";
    EditText secrch_txt;
    String secrch_keyword="";

    String lineArray[]={"경춘선","경부선","영동선","중앙선"};
    String lineStrings[]={"cate01","cate02","cate03","cate04"};
    String cate01[]={"춘천역","가평역","강촌역"};
    String cate02[]={"안양역","부산역","수원역"};
    String cate03[]={"동해역","정동진역","강릉역"};
    String cate04[]={"용문역","안동역","단양역"};
    String tema[]={"식사","카페","레저","자연","문화"};

    ImageView search_btn;
    String search_line="";
    String search_station="";
    String search_tema="";



    String lineCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_list);
        pref = new RbPreference(this);

        setWidget();


        new getBoadrdList().execute();

    }

    CustomTextView lines;
    CustomTextView station;
    CustomTextView cate;


    private void setWidget(){

        search_btn=(ImageView)findViewById(R.id.imageView3);
        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        ImageView favor_img = (ImageView)findViewById(R.id.favor_img);
//        favor_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cateStr="favor";
//                new getBoadrdList().execute();
//
//            }
//        });
//
//        ImageView recomm_img = (ImageView)findViewById(R.id.recomm_img);
//        recomm_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cateStr="recomm";
//                new getBoadrdList().execute();
//
//            }
//        });

        lines = (CustomTextView) findViewById(R.id.lines);
        station = (CustomTextView) findViewById(R.id.station);
        cate = (CustomTextView)findViewById(R.id.cate);

        station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(lines.getText().toString())){
                    Toast.makeText(VisitActivity.this,"노선을 먼저 선택하세요." , Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(VisitActivity.this);
                    builder.setTitle("역 선택");
                    if(lineCode.equals("cate01")){
                        builder.setItems(cate01, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int pos) {
                                dialog.dismiss();
                                station.setText(cate01[pos]);
                                search_station=cate01[pos];
                            }
                        });
                    }else   if(lineCode.equals("cate02")){
                        builder.setItems(cate02, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int pos) {
                                dialog.dismiss();
                                station.setText(cate02[pos]);
                                search_station=cate02[pos];

                            }
                        });
                    }else   if(lineCode.equals("cate03")){
                        builder.setItems(cate03, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int pos) {
                                dialog.dismiss();
                                station.setText(cate03[pos]);
                                search_station=cate03[pos];

                            }
                        });
                    }else   if(lineCode.equals("cate04")){
                        builder.setItems(cate04, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int pos) {
                                dialog.dismiss();
                                station.setText(cate04[pos]);
                                search_station=cate04[pos];

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
                AlertDialog.Builder builder = new AlertDialog.Builder(VisitActivity.this);
                builder.setTitle("노선선택");
                builder.setItems(lineArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int pos) {
                        dialog.dismiss();
                        lines.setText(lineArray[pos]);
                        lineCode=lineStrings[pos];
                        search_line=lineArray[pos];
                        search_station="";


                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        cate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cate.setText("");
                AlertDialog.Builder builder = new AlertDialog.Builder(VisitActivity.this);
                builder.setTitle("테마선택");
                builder.setItems(tema, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int pos) {
                        dialog.dismiss();
                        cate.setText(tema[pos]);
                        search_tema=tema[pos];
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getBoadrdList1().execute();

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
        ImageView menu_del = (ImageView)findViewById(R.id.imageView4);
        menu_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lines.setText("");
                station.setText("");
                cate.setText("");
                search_line="";
                search_station="";
                search_tema="";
                new getBoadrdList().execute();
            }
        });

    }
    private class getBoadrdList1 extends AsyncTask<Void, Void, Void> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(VisitActivity.this, "", "데이터 처리중....", true);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            getStore();
            return null;
        }

        private void getStore() {
            vdata.clear();

            DBHelper helper = new DBHelper(VisitActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();


            String upSql="";
            if (search_line.equals("")){
                if(search_tema.equals("")) {
                    upSql = "SELECT  * from visit ";
                }else {
                    upSql = "SELECT  * from visit  where cate like '%" + search_tema +"%'";
                }
            }
            if (!search_line.equals("") && search_station.equals("")) {
                if(search_tema.equals("")) {
                    upSql = "SELECT  * from visit where line like '%" + search_line +"%'";
                }else {
                    upSql = "SELECT  * from visit where line like '%" + search_line +"%' AND cate like '%" + search_tema + "%'";
                }
            }
            if (!search_line.equals("") && !search_station.equals("")) {
                if(search_tema.equals("")) {
                    upSql = "SELECT  * from visit where station like '%" + search_station +"%'";
                }else {
                    upSql = "SELECT  * from visit where station like '%" + search_station +"%' AND cate like '%" + search_tema + "%'";
                }
            }


            upSql = upSql +" order by favor desc limit 0 , 60";

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

            GoodsAdapter mAdapter = new GoodsAdapter(VisitActivity.this,
                    R.layout.listview_visit, vdata);

            s_list.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();


        }
    }




    private class getBoadrdList extends AsyncTask<Void, Void, Void> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(VisitActivity.this, "", "데이터 처리중....", true);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            getStore();
            return null;
        }

        private void getStore() {
            vdata.clear();

            DBHelper helper = new DBHelper(VisitActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();


            String upSql="";
            if(secrch_keyword.equals("")){
                upSql = "SELECT  * from visit ";

            }else{
                upSql = "SELECT  * from visit  where name like '%" + secrch_keyword +"%'";
            }

            upSql = upSql +" order by favor desc limit 0 , 60";

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

            GoodsAdapter mAdapter = new GoodsAdapter(VisitActivity.this,
                    R.layout.listview_visit, vdata);

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
                    Intent gt = new Intent(VisitActivity.this , VisitDetailActivity.class);
                    Bundle ex = new Bundle();
                    ex.putSerializable("list" , items.get(pos));
                    gt.putExtras(ex);
                    startActivity(gt);

                }
            });

            return v;
        }
    }




}
