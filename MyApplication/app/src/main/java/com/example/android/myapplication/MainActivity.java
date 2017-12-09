package com.example.android.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnCreateDatabase;
    private Button btnInsertDatabase;
    private ListView IvPelople;
    private Button btnSlectAllData;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreateDatabase = (Button) findViewById(R.id.btnCreateButton);
        btnCreateDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText etDBName = new EditText(MainActivity.this);
                etDBName.setHint("DB명을 입력하세요.");

                // Dialog로 Database의 이름을 받는다.
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Database 이름을 입력하세요.")
                        .setMessage("Database 이름을 입력하세요")
                        .setView(etDBName)
                        .setPositiveButton("생성", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (etDBName.getText().toString().length() > 0) {
                                    dbHelper = new DBHelper(
                                            MainActivity.this,
                                            etDBName.getText().toString(),
                                            null, 1);
                                    dbHelper.testDB();
                                }
                            }
                        })
                        .create()
                        .show();
            }
        });

        btnInsertDatabase = (Button) findViewById(R.id.btninsertButton);
        btnInsertDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText etName = new EditText(MainActivity.this);
                etName.setHint("이름을 입력하세요");

                final EditText etAge = new EditText(MainActivity.this);
                etAge.setHint("나이를 입력해주세요");

                final EditText etPhone = new EditText(MainActivity.this);
                etPhone.setHint("전화번호를 입력해주세요");

                layout.addView(etName);
                layout.addView(etAge);
                layout.addView(etPhone);

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("정보를 입력하세요").setView(layout)
                        .setPositiveButton("등록", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = etName.getText().toString();
                                String age = etAge.getText().toString();
                                String phone = etPhone.getText().toString();
                                if (dbHelper == null) {
                                    dbHelper = new DBHelper(MainActivity.this, "TEST", null, 1);
                                }
                                Person person = new Person();
                                person.setName(name);
                                person.setAge(age);
                                person.setPhone(phone);
                                dbHelper.addPerson(person);
                            }
                        })
                        .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();

            }
        });

        IvPelople = (ListView) findViewById(R.id.IvPeople);
        btnSlectAllData = (Button) findViewById(R.id.btnSelectAllData);
        btnSlectAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ListView를 보여준다.
                IvPelople.setVisibility(View.VISIBLE);
                // DB Helper가 Null이면 초기화 시켜준다.
                if( dbHelper == null ) {
                dbHelper = new DBHelper(MainActivity.this, "TEST", null, 1);
                }

            // 1. Person 데이터를 모두 가져온다.
            List people = dbHelper.getAllPersonData();
            // 2. ListView에 Person 데이터를 모두 보여준다.
            IvPelople.setAdapter(new PersonListAdapter(people, MainActivity.this));


            }
        });


    }

    private class PersonListAdapter extends BaseAdapter {

        private List people;
        private Context context;

        public PersonListAdapter(List people, Context context) {
            this.people = people;
            this.context = context;
        }

        @Override
        public int getCount() {
            return this.people.size();
        }

        @Override
        public Object getItem(int position) {
            return this.people.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Holder holder = null;

            if (convertView == null) {
                // convertView가 없으면 초기화합니다.
                convertView = new LinearLayout(context);
                ((LinearLayout) convertView).setOrientation(LinearLayout.HORIZONTAL);

                TextView tvId = new TextView(context);
                tvId.setPadding(10, 0, 20, 0);
                tvId.setTextColor(Color.rgb(0, 0, 0));

                TextView tvName = new TextView(context);
                tvName.setPadding(20, 0, 20, 0);
                tvName.setTextColor(Color.rgb(0, 0, 0));

                TextView tvAge = new TextView(context);
                tvAge.setPadding(20, 0, 20, 0);
                tvAge.setTextColor(Color.rgb(0, 0, 0));

                TextView tvPhone = new TextView(context);
                tvPhone.setPadding(20, 0, 20, 0);
                tvPhone.setTextColor(Color.rgb(0, 0, 0));

                ((LinearLayout) convertView).addView(tvId);
                ((LinearLayout) convertView).addView(tvName);
                ((LinearLayout) convertView).addView(tvAge);
                ((LinearLayout) convertView).addView(tvPhone);

                holder = new Holder();
                holder.tvId = tvId;
                holder.tvName = tvName;
                holder.tvAge = tvAge;
                holder.tvPhone = tvPhone;

                convertView.setTag(holder);
            } else {

                // convertView가 있으면 홀더를 꺼냅니다.
                holder = (Holder) convertView.getTag();
            }
            // / 한명의 데이터를 받아와서 입력합니다.
            Person person = (Person) getItem(position);
            holder.tvId.setText(person.get_id() + "");
            holder.tvName.setText(person.getName());
            holder.tvAge.setText(person.getAge() + "");
            holder.tvPhone.setText(person.getPhone());

            return convertView;
        }
    }

    /**
     * 홀더
     */
    private class Holder {
        public TextView tvId;
        public TextView tvName;
        public TextView tvAge;
        public TextView tvPhone;
    }
}



