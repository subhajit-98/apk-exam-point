package com.example.exampoint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ShowSingleResult extends AppCompatActivity {

    TextView et_show_message;
    ImageView iv_back_btn;
    TextView tv_show_title;
    ExpandableListView elv_show_sem_marks;

    public List<String> listGroup;
    public HashMap<String, List<String>> listItem, scoreSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_result);
        getSupportActionBar().hide();

        Intent i = getIntent();
        String semester_name = i.getStringExtra("semester_name");
        String semester_id = i.getStringExtra("semester");

        tv_show_title = findViewById(R.id.tv_show_title);
        tv_show_title.setText(semester_name);

        iv_back_btn = findViewById(R.id.iv_back_btn);
        iv_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getDataFromServer(App.API_URL+"marks/all-marks", semester_id);

        elv_show_sem_marks = findViewById(R.id.elv_show_sem_marks);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        scoreSubject = new HashMap<>();
    }

    public void getDataFromServer(String url, String Semester_id){
        StringRequest request = new StringRequest(Request.Method.GET, url+"?student_roll="+App.student_ID+"&semester="+Semester_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    Iterator iterator   = result.keys();
                    listGroup = new ArrayList<>();
                    while(iterator.hasNext()) {
                        String key = (String) iterator.next();
                        listGroup.add(key);
                    }
                    listItem = new HashMap<>();
                    for (int i=0; i < listGroup.size(); i++){
                        JSONObject subject_list_json = result.getJSONObject(listGroup.get(i));
                        Iterator subjects = subject_list_json.keys();
                        List<String>has_map_list = new ArrayList<>();
                        List<String>score = new ArrayList<>();
                        while(subjects.hasNext()){
                            String key = (String) subjects.next();
                            has_map_list.add(key);
                            score.add(subject_list_json.getString(key));
                        }
                        listItem.put(listGroup.get(i), has_map_list);
                        scoreSubject.put(listGroup.get(i), score);
                    }
                    /*System.out.println(listItem);
                    System.out.println(scoreSubject);*/
                    listViewAdapter obj = new listViewAdapter();
                    elv_show_sem_marks.setAdapter(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Message", "Error exit");
            }
        });
        Volley.newRequestQueue(ShowSingleResult.this).add(request);
    }

    public class listViewAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return listGroup.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return listItem.get(listGroup.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return listGroup.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return listItem.get(listGroup.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            et_show_message = findViewById(R.id.et_show_message);
            et_show_message.setVisibility(View.GONE);
            String listGroup_data = (String) getGroup(groupPosition);
            if(convertView == null){
                LayoutInflater inf = getLayoutInflater();
                convertView = inf.inflate(R.layout.internal_title, null);
                TextView tv_data = convertView.findViewById(R.id.tv_show_internal_title);
                tv_data.setText(listGroup_data);

                Log.i("Goup possition", String.valueOf(groupPosition));
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            String listGroup_data = (String) getChild(groupPosition, childPosition);
            String show_score = (String) scoreSubject.get(listGroup.get(groupPosition)).get(childPosition);
            /*if(convertView == null){
                LayoutInflater inf = getLayoutInflater();
                convertView = inf.inflate(R.layout.details_marks, null);
                TextView tv_data = convertView.findViewById(R.id.tv_subject_show);
                tv_data.setText(listGroup_data);
                TextView tv_mark_show = convertView.findViewById(R.id.tv_mark_show);
                tv_mark_show.setText(show_score);
                System.out.println(Collections.singletonList(scoreSubject));
                System.out.println(show_score);
                Log.i("Group", String.valueOf(groupPosition));
                Log.i("Child", String.valueOf(childPosition));
                Log.i("Grouplist", String.valueOf(listItem));
                Log.i("Score", String.valueOf(scoreSubject));
            }*/
            LayoutInflater inf = getLayoutInflater();
            convertView = inf.inflate(R.layout.details_marks, null);
            TextView tv_data = convertView.findViewById(R.id.tv_subject_show);
            tv_data.setText(listGroup_data);
            TextView tv_mark_show = convertView.findViewById(R.id.tv_mark_show);
            tv_mark_show.setText(show_score);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}

//https://www.youtube.com/watch?v=E4rrh_WnKMw
//https://www.youtube.com/watch?v=iMoN414EfSQ