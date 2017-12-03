package apps.com.stations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LogInActivity extends Activity {
    EditText id;
    EditText pass;

    RbPreference pref;

    boolean isCheck=false;
    boolean isCheckPass=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref= new RbPreference(this);

        id = (EditText) findViewById(R.id.id);
        pass = (EditText) findViewById(R.id.pass);



        Button login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(id.getText().toString())) {
                    Toast.makeText(LogInActivity.this, "아이디 입력하세요", Toast.LENGTH_SHORT).show();
                }else  if (TextUtils.isEmpty(pass.getText().toString())) {
                    Toast.makeText(LogInActivity.this , "비밀번호를 입력하세요" ,Toast.LENGTH_SHORT).show();
                }else{

                    new loginTask().execute();
                }

            }
        });


        Button join_user = (Button)findViewById(R.id.join_user);
        join_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent gt = new Intent(LogInActivity.this , JoinUserActivity.class);
              startActivity(gt);

            }
        });

        final ImageView save_id = (ImageView)findViewById(R.id.save_id);
        isCheck = pref.getValue(RbPreference.saveId,false);
        if(isCheck){
            save_id.setBackgroundResource(R.drawable.check_on);
            id.setText(pref.getValue(RbPreference.MEM_ID,""));
        }else{
            save_id.setBackgroundResource(R.drawable.check);
        }

        save_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCheck=!isCheck;

                if(isCheck){
                    save_id.setBackgroundResource(R.drawable.check_on);
                }else{
                    save_id.setBackgroundResource(R.drawable.check);
                }

                pref.put(RbPreference.saveId,isCheck);

            }
        });

        final ImageView save_pass = (ImageView)findViewById(R.id.save_pass);
        isCheckPass = pref.getValue(RbPreference.savePass,false);
        if(isCheckPass){
            save_pass.setBackgroundResource(R.drawable.check_on);
            pass.setText(pref.getValue(RbPreference.MEM_PASS,""));
        }else{
            save_pass.setBackgroundResource(R.drawable.check);
        }

        save_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCheckPass=!isCheckPass;

                if(isCheckPass){
                    save_pass.setBackgroundResource(R.drawable.check_on);
                }else{
                    save_pass.setBackgroundResource(R.drawable.check);
                }

                pref.put(RbPreference.savePass,isCheckPass);

            }
        });




    }



    private class loginTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog mProgressDialog;

        String mem_name ="";
        String email  ="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(LogInActivity.this, "", "로그인 처리중....", true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            getList();

            return null;
        }

        private void getList()  {
            DBHelper helper = new DBHelper(LogInActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();

            String upSql="";

            upSql = "SELECT  * from member where mem_id = '"+id.getText().toString() +"' and mem_pass = '"+pass.getText().toString() +"' ";

            try {
                Cursor monthCursor;
                monthCursor = db.rawQuery(upSql, null);


                while (monthCursor.moveToNext()) {
                     mem_name = monthCursor.getString(3);

                     email = monthCursor.getString(4);
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
            if (mem_name.equals("")) {
                Toast.makeText(LogInActivity.this , "로그인 실패" ,Toast.LENGTH_SHORT).show();

            } else {
                pref.put(RbPreference.MEM_ID , id.getText().toString());
                pref.put(RbPreference.MEM_PASS, pass.getText().toString());

                pref.put(RbPreference.ISLOGIN,true);

                setResult(RESULT_OK);
                finish();

            }

        }
    }



}
