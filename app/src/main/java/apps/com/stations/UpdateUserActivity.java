package apps.com.stations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateUserActivity extends Activity {

    CustomTextView id;
    EditText pass;

    EditText name;
    CustomTextView email;
    EditText pass_ok;



    RbPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_update);

        pref = new RbPreference(this);

        id = (CustomTextView) findViewById(R.id.id);
        id.setText(pref.getValue(RbPreference.MEM_ID,""));
        name = (EditText) findViewById(R.id.name);
        pass = (EditText) findViewById(R.id.pass);
        pass_ok = (EditText) findViewById(R.id.pass_ok);
        email = (CustomTextView) findViewById(R.id.email);



        CustomTextView join = (CustomTextView) findViewById(R.id.join);

        join.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(id.getText().toString())) {
                    Toast.makeText(UpdateUserActivity.this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(pass.getText().toString())) {
                    Toast.makeText(UpdateUserActivity.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pass_ok.getText().toString())) {
                    Toast.makeText(UpdateUserActivity.this, "비밀번호를 확인 입력하세요", Toast.LENGTH_SHORT).show();
                } else if (!pass.getText().toString().equals((pass_ok.getText().toString()))) {
                    Toast.makeText(UpdateUserActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(UpdateUserActivity.this, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(UpdateUserActivity.this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                }  else {
                    updateData();
                }

            }
        });

        new loginTask().execute();


    }

    private class loginTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog mProgressDialog;

        String mem_name ="";
        String mem_email  ="";
        String mem_pass="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(UpdateUserActivity.this, "", "데이터 처리중....", true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            getList();

            return null;
        }

        private void getList()  {
            DBHelper helper = new DBHelper(UpdateUserActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();

            String upSql="";

            upSql = "SELECT  * from member where mem_id = '"+id.getText().toString()+"'";

            try {
                Cursor monthCursor;
                monthCursor = db.rawQuery(upSql, null);


                while (monthCursor.moveToNext()) {
                    mem_pass = monthCursor.getString(2);
                    mem_name = monthCursor.getString(3);

                    mem_email = monthCursor.getString(4);



                }
            } catch (Exception e) {
                Log.e("Thread", "select Error", e);

            } finally {
                helper.close();
                db.close();
            }
        }

        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();
            email.setText(mem_email);
            name.setText(mem_name);
            pass.setText(mem_pass);
            pass_ok.setText(mem_pass);

        }
    }


    private void updateData() {

        DBHelper helper = new DBHelper(UpdateUserActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {

            ContentValues row;
            row = new ContentValues();


            row.put("mem_pass", pass.getText().toString());
            row.put("mem_name", name.getText().toString());


            db.update("member", row, "mem_id=?",new String[]{id.getText().toString()});



        } catch (Exception e) {
            Log.e("Thread", "Insert Error", e);
            Toast.makeText(UpdateUserActivity.this, "수정 실패!",
                    Toast.LENGTH_SHORT).show();

        } finally {
            helper.close();
            db.close();
            Toast.makeText(UpdateUserActivity.this, "정보 수정 완료 되었습니다. ", Toast.LENGTH_SHORT).show();
            finish();

        }
    }


}
