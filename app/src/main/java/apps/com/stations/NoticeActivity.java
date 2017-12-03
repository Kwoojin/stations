package apps.com.stations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.ArrayList;

public class NoticeActivity extends Activity {
    RbPreference pref;
    ArrayList<BoardData> storedata = new ArrayList<BoardData>();
    String theme="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
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





    private class getBoadrdList extends AsyncTask<Void, Void, Void> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(NoticeActivity.this, "", "데이터 처리중....", true);
            }

        }

        @Override
        protected Void doInBackground(Void... params) {

            getStore();
            return null;
        }

        private void getStore() {
            storedata.clear();

            storedata.add(new BoardData("1","[공지]역이요 소개","역이요는 여행 스케줄을 제작할 수 있는 어플로써 테마별로 여행지들을 제공하고 사용자가 원하는 여행지들을 원하는 시간대에 추가해 스케줄을 직접 제작합니다. 사용자가 선택한 날짜만큼 요일별로 스케줄을 제작하고 여행코스가 완성됩니다. 해당되는 날짜에 완성된 여행 코스의 일정대로 사용자의 취향에 맞게 여행을 다녀올 수 있습니다."
                    ,"관리자","2017.03.01" ));

            storedata.add(new BoardData("1","[업데이트]영동선 추가","새롭게 영동선이 추가되어 해맞이 장소로 유명한 정동진을 포함하여 여러 여행지가 추가되었습니다."
                    ,"관리자","2017.05.06" ));

            storedata.add(new BoardData("1","[공지]역이요 서비스 정기정검","안녕하세요 역이요 운영 팀입니다.\n" +
                    "서비스 품질 향상을 위해 시스템 점검 작업이 있을 예정입니다. \n" +
                    "점검시간동안 역이요 서비스를 이용할 수 없는 점 양해 부탁드립니다."
                    ,"관리자","2017.06.16" ));
            storedata.add(new BoardData("1","[공지]회원정보 보안 강화를 위한 비밀번호 변경 안내","역이요 회원님들의 개인정보를 안전하게 보호하기 위해 비밀번호를 바꿔주시길 바랍니다."
                    ,"관리자","2017.06.24" ));

            storedata.add(new BoardData("1","[공지]회원가입 및 로그인 절차 변경 안내","안녕하세요. 역이요 운영 팀 입니다. 역이요 앱 업데이트에서 회원가입 및 로그인 절차가 개편되어 안내드립니다. 더 편리하게 역이요를 이용하실 수 있도록 회원가입 절차를 개선하였습니다."
                    ,"관리자","2017.07.20" ));

        }

        protected void onPostExecute(Void result) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }

            ExpandableListView elv_list = (ExpandableListView) findViewById(R.id.elv_list);

            elv_list.setAdapter(new NoticeExpandableAdapter(NoticeActivity.this, storedata));
            if (storedata.size() > 0) {
                elv_list.expandGroup(0);
            }



        }
    }



}
