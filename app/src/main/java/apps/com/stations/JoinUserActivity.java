package apps.com.stations;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JoinUserActivity extends Activity {

    EditText id;
    EditText pass;

    EditText name;
    EditText email;
    EditText pass_ok;


    boolean isCheckId = false;
    boolean isEmail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_user);

        id = (EditText) findViewById(R.id.id);
        name = (EditText) findViewById(R.id.name);
        pass = (EditText) findViewById(R.id.pass);
        pass_ok = (EditText) findViewById(R.id.pass_ok);
        email = (EditText) findViewById(R.id.email);

        CustomTextView id_check = (CustomTextView) findViewById(R.id.id_check);
        CustomTextView email_check = (CustomTextView) findViewById(R.id.email_check);

        id_check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(id.getText().toString())) {
                    Toast.makeText(JoinUserActivity.this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkID()) {
                        Toast.makeText(JoinUserActivity.this, "사용 가능한 아이디 입니다.", Toast.LENGTH_SHORT).show();
                        isCheckId = true;
                    } else {
                        Toast.makeText(JoinUserActivity.this, "중복된 아이디", Toast.LENGTH_SHORT).show();
                        isCheckId = false;
                    }
                }

            }
        });

        email_check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(JoinUserActivity.this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkEmail()) {
                        Toast.makeText(JoinUserActivity.this, "사용 가능한 이메일 입니다.", Toast.LENGTH_SHORT).show();
                        isEmail = true;
                    } else {
                        Toast.makeText(JoinUserActivity.this, "중복된 이메일", Toast.LENGTH_SHORT).show();
                        isEmail = false;
                    }
                }

            }
        });

        CustomTextView join = (CustomTextView) findViewById(R.id.join);

        join.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(id.getText().toString())) {
                    Toast.makeText(JoinUserActivity.this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                } else if (!isCheckId) {
                    Toast.makeText(JoinUserActivity.this, "아이디 중복체크를 하세요", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pass.getText().toString())) {
                    Toast.makeText(JoinUserActivity.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pass_ok.getText().toString())) {
                    Toast.makeText(JoinUserActivity.this, "비밀번호를 확인 입력하세요", Toast.LENGTH_SHORT).show();
                } else if (!pass.getText().toString().equals((pass_ok.getText().toString()))) {
                    Toast.makeText(JoinUserActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(JoinUserActivity.this, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(JoinUserActivity.this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                } else if (!isEmail) {
                    Toast.makeText(JoinUserActivity.this, "이메일 중복체크를 하세요", Toast.LENGTH_SHORT).show();
                } else {
                    insertData();
                }

            }
        });


    }

    private boolean checkID() {
        boolean isCheck = false;

        DBHelper helper = new DBHelper(JoinUserActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String upSql = "";

        upSql = "SELECT  * from member where mem_id = '" + id.getText().toString() + "'";
        String mem_name = "";

        try {
            Cursor monthCursor;
            monthCursor = db.rawQuery(upSql, null);


            while (monthCursor.moveToNext()) {
                mem_name = monthCursor.getString(3);


            }
        } catch (Exception e) {
            Log.e("Thread", "select Error", e);

        } finally {
            helper.close();
            db.close();
        }

        if (mem_name.equals("")) {
            isCheck = true;
        } else {
            isCheck = false;

        }

        return isCheck;
    }


    private boolean checkEmail() {
        boolean isCheck = false;

        DBHelper helper = new DBHelper(JoinUserActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String upSql = "";

        upSql = "SELECT  * from member where email = '" + email.getText().toString() + "'";
        String mem_name = "";

        try {
            Cursor monthCursor;
            monthCursor = db.rawQuery(upSql, null);


            while (monthCursor.moveToNext()) {
                mem_name = monthCursor.getString(3);


            }
        } catch (Exception e) {
            Log.e("Thread", "select Error", e);

        } finally {
            helper.close();
            db.close();
        }

        if (mem_name.equals("")) {
            isCheck = true;
        } else {
            isCheck = false;

        }

        return isCheck;
    }

    private void insertData() {

        DBHelper helper = new DBHelper(JoinUserActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();


        try {

            ContentValues row;
            row = new ContentValues();

            row.put("mem_id", id.getText().toString());
            row.put("mem_pass", pass.getText().toString());
            row.put("mem_name", name.getText().toString());
            row.put("email", email.getText().toString());


            db.insert("member", null, row);

        } catch (Exception e) {
            Log.e("Thread", "Insert Error", e);
            Toast.makeText(JoinUserActivity.this, "등록 실패!",
                    Toast.LENGTH_SHORT).show();

        } finally {
            helper.close();
            db.close();
            Toast.makeText(JoinUserActivity.this, "회원가입 완료 되었습니다. 로그인해주세요.", Toast.LENGTH_SHORT).show();
            finish();

        }
    }


}
