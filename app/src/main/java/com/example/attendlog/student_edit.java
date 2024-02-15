package com.example.attendlog;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class student_edit extends AppCompatActivity {

    Button butdelete,butdone;
    EditText stusino,stupino,stuname;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit);


        butdelete=this.findViewById(R.id.but_delete);
        butdone=this.findViewById(R.id.but_done);


        Intent intent = getIntent();
        String studentSino = intent.getStringExtra("student sino");
        String studentPino = intent.getStringExtra("student pino");
        String studentName = intent.getStringExtra("student name");


        stusino=this.findViewById(R.id.stu_sino);
        stupino=this.findViewById(R.id.stu_pino);
        stuname=this.findViewById(R.id.stu_name);


        stusino.setText(studentSino);
        stupino.setText(studentPino);
        stuname.setText(studentName);

        progressDialog=new ProgressDialog(this);

        butdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s_stusino=stusino.getText().toString();
                String s_stupino=stupino.getText().toString();
                String s_stuname=stuname.getText().toString();


                if (!s_stuname.equals("") && !s_stupino.equals("") && !s_stusino.equals("")) {

                    progressDialog.show();
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    CollectionReference coll=firestore.collection("class");

                    SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
                    boolean haslogedin=sharedPreferences.getBoolean("hasClassLogIn",false);
                    String s_classid=sharedPreferences.getString("classid",null);


                    coll.document(s_classid)
                            .collection("students")
                            .document(s_stupino)
                            .update(
                                    "stusino", s_stusino,
                                    "stupino", s_stupino,
                                    "stuname", s_stuname,"stusino_numeric",Integer.valueOf(s_stusino)
                            )
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Document successfully updated
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle the error
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });

                    Toast.makeText(student_edit.this, "STUDENT MODIFIED", Toast.LENGTH_SHORT).show();

                    finish();

                    onBackPressed();

                    progressDialog.cancel();
                }else{
                    progressDialog.cancel();
                    Toast.makeText(student_edit.this, "FILL THE DETAILS", Toast.LENGTH_SHORT).show();
                }

            }
        });
        progressDialog=new ProgressDialog(this);

        butdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String s_stusino=stusino.getText().toString();
                String s_stupino=stupino.getText().toString();
                String s_stuname=stuname.getText().toString();


                if (!s_stuname.equals("") && !s_stupino.equals("") && !s_stusino.equals("")) {


                progressDialog.show();
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                CollectionReference coll=firestore.collection("class");

                SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
                boolean haslogedin=sharedPreferences.getBoolean("hasClassLogIn",false);
                String s_classid=sharedPreferences.getString("classid",null);


                // Assuming coll is your Firestore collection reference
                coll.document(s_classid)
                        .collection("students")
                        .document(s_stupino)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Document successfully deleted
                                Toast.makeText(student_edit.this, "STUDENT DELETED SUCCESSFULLY ", Toast.LENGTH_SHORT).show();


                                coll.document(s_classid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        coll.document(s_classid).update("studentcount",String.valueOf(Integer.valueOf(documentSnapshot.getString("studentcount"))-1));
                                        progressDialog.cancel();
                                        finish();
                                    }
                                });

                                // Finish the current activity to go back to the previous activity

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle the error
                                Toast.makeText(student_edit.this, "UNABLE TO DELETE STUDENT", Toast.LENGTH_SHORT).show();
                            }
                        });
            }else{
                progressDialog.cancel();
                Toast.makeText(student_edit.this, "FILL THE STUDENT PINO TO DELETE", Toast.LENGTH_SHORT).show();
            }


            }
        });

    }
}