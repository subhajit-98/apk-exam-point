package com.example.exampoint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FindSemester extends AppCompatActivity {
    ListView lv_semester;
    TextView tv_show_title;
    ImageView iv_back_btn;
    String[] data = new String[9];
    public ArrayList<String> semester_name =new ArrayList();
    public ArrayList<String> semester_id =new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_semester);
        getSupportActionBar().hide();

        iv_back_btn = findViewById(R.id.iv_back_btn);
        iv_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_show_title = findViewById(R.id.tv_show_title);
        tv_show_title.setText(App.student_ID);

        getDataFromServer(App.API_URL+"semester/all-sem", App.student_ID);

        lv_semester = findViewById(R.id.lv_semester);
        ListAdapter obj = new ListAdapter();
        lv_semester.setAdapter(obj);

        lv_semester.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(FindSemester.this, semester_id.get(position), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(FindSemester.this, ShowSingleResult.class);
                i.putExtra("semester", semester_id.get(position));
                i.putExtra("semester_name", semester_name.get(position));
                startActivity(i);
            }
        });
    }

    public void getDataFromServer(String url, String roll_no){
        StringRequest request = new StringRequest(Request.Method.GET, url + "?student_roll=" + roll_no, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    System.out.println(result.getJSONObject("semesters"));
                    for (int i=0; i<result.getJSONObject("semesters").length(); i++){
                        int j = i;
                        data[i]= result.getJSONObject("semesters").getString(String.valueOf(j+1));
                        semester_name.add(result.getJSONObject("semesters").getString(String.valueOf(j+1)));
                        semester_id.add(String.valueOf(j+1));

                        ListAdapter obj = new ListAdapter();
                        lv_semester.setAdapter(obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(FindSemester.this).add(request);
    }

    class ListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            // return data.length;
            return semester_name.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inf = getLayoutInflater();
            View row = inf.inflate(R.layout.list_content, null);
            TextView tv_data = row.findViewById(R.id.tv_data);
            // tv_data.setText(data[position]);
            tv_data.setText(semester_name.get(position));
            return row;
        }
    }
}