package com.example.exampoint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

public class EnterRoll extends AppCompatActivity {

    EditText et_roll_no;
    Button btn_serach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_roll);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("Exam Point");
        // getSupportActionBar().hide();

        et_roll_no = findViewById(R.id.et_roll_no);
        et_roll_no.setText("1425621432");

        btn_serach = findViewById(R.id.btn_serach);
        btn_serach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_roll_no = findViewById(R.id.et_roll_no);
                System.out.println(et_roll_no.getText().toString());
                /*Intent i = new Intent(EnterRoll.this, FindSemester.class);
                startActivity(i);*/
                getDataFromServer(App.API_URL+"students/check", et_roll_no.getText().toString());
            }
        });
    }

    public void getDataFromServer(String url, String student_roll ){
        StringRequest request = new StringRequest(Request.Method.GET, url+"?student_roll="+et_roll_no.getText().toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    //System.out.println(response);
                    JSONObject result = new JSONObject(response);
                    System.out.println(result);
                    App.student_ID=result.getString("student_id");
                    Intent i = new Intent(EnterRoll.this, FindSemester.class);
                    startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                et_roll_no.setError("Roll no not found");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String>hasmap =new HashMap<>();
                hasmap.put("student_roll ", student_roll);
                return hasmap;
            }
        };

        Volley.newRequestQueue(EnterRoll.this).add(request);
    }
}