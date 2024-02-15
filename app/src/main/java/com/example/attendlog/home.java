package com.example.attendlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class home extends AppCompatActivity {

    Button takeaddtend,homelogout,classsettings;

    TextView prt_class_id,classcourse,classbranch,classcount;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        takeaddtend=findViewById(R.id.but_takeattend);
        homelogout=findViewById(R.id.home_logout);
        classsettings=findViewById(R.id.but_classsettings);
        progressDialog=new ProgressDialog(this);

        prt_class_id=findViewById(R.id.present_class_id);
        classcourse=findViewById(R.id.class_course);
        classbranch=findViewById(R.id.class_branch);
        classcount=findViewById(R.id.class_count);

        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
        boolean haslogedin=sharedPreferences.getBoolean("hasClassLogIn",false);
        String s_classid=sharedPreferences.getString("classid",null);



        prt_class_id.setText(s_classid);

        progressDialog.show();


        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference coll=firestore.collection("class");


        coll.document(s_classid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                classcourse.setText(documentSnapshot.getString("classcourse"));
                classbranch.setText(documentSnapshot.getString("classbranch"));
                coll.document(s_classid).collection("students").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            int size=queryDocumentSnapshots.size();

                            classcount.setText(String.valueOf(size));



                        }
                    });
                progressDialog.cancel();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                Toast.makeText(home.this, "FAILED TO FETCH DETAILS ", Toast.LENGTH_SHORT).show();
            }
        });

        takeaddtend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(home.this, take_attend.class);
                startActivity(in);
            }
        });

        homelogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("hasClassLogIn",false);
                editor.putString("classid",null);
                editor.commit();
                Intent in = new Intent(home.this, MainActivity.class);
                startActivity(in);
            }
        });

        classsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(home.this, class_settings.class);
                startActivity(in);
            }
        });
    }
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}