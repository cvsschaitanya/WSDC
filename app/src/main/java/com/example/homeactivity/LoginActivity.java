package com.example.homeactivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    String username,password;
    LoginActivity Login_Context;
    int x;


    Object ReturnObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main2);

    }

    public void checkCreden(View view){
        new ExtractAccountTask().execute(this);
    }

    class ExtractAccountTask extends AsyncTask<LoginActivity,Void,Object>
    {
        String username,password;
        LoginActivity Login_Context;
        int x;
        boolean done;

        protected void onPreExecute()
        {
            done = false;

            EditText USERNAME = (EditText) findViewById(R.id.username_login);
            EditText PASSWORD = (EditText) findViewById(R.id.password_login);

            username = String.valueOf(USERNAME.getText());
            password = String.valueOf(PASSWORD.getText());
        }


        @Override
        protected Object doInBackground(LoginActivity[] signinObj) {

//            if(android.os.Debug.isDebuggerConnected())
//                android.os.Debug.waitForDebugger();


            Login_Context = signinObj[0];
            Intent intent = Login_Context.getIntent();
            String data = intent.getStringExtra("thisStudent");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Api api = retrofit.create(Api.class);

            Call<List<Student>> call = api.getStudents();
            ReturnObject = null;

            call.enqueue(new Callback<List<Student>>()
            {
                @Override
                public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {

                    x=1;
                    List<Student> studentList = response.body();
                    for(Student s:studentList) {
                        ReturnObject=s;
                        break;
                    }
                }

                @Override
                public void onFailure(Call<List<Student>> call, Throwable t) {
                    x=2;
                    Log.d(TAG, "onFailure: ");
                    ReturnObject = t;
                }
            });

            while(ReturnObject==null);

            return ReturnObject;
        }

        protected void onPostExecute(Object Obj)
        {

            if(Obj instanceof Student)
            {
                if(Obj != null)
                {
                    Intent intent = new Intent(Login_Context,HomeActivity.class);
                    intent.putExtra("thisStudent",Obj.toString());
                    startActivity(intent);
                }
                else
                {
                    new MyToast(Login_Context,"No such User!");
                }
            }
            else
            {
                new MyToast(Login_Context,((Throwable)Obj).getMessage());
            }
        }
    }
}

