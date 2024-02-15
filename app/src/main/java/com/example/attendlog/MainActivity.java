package com.example.attendlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    Button login,register;

    public static String user;
    public static  String U_LOGIN;

    FirebaseAuth fauth;

    EditText classid,classpassword;
    TextView pass_reset;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        login=findViewById(R.id.class_login);
        register=findViewById(R.id.class_signup);
        pass_reset=this.findViewById(R.id.pass_reset);


        fauth=FirebaseAuth.getInstance();

        classid=findViewById(R.id.class_id);
        classpassword=findViewById(R.id.class_password);
        progressDialog=new ProgressDialog(this);



//        getSupportActionBar().hide();
//        Handler hn=new Handler();
//        hn.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
//                boolean haslogedin=sharedPreferences.getBoolean("hasClassLogIn",false);
//
//
//
//                if(haslogedin){
//
//                    Intent in=new Intent(MainActivity.this,home.class);
//                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //clear backstack
//                    in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    startActivity(in);
//                    finish();
//
//                }
//                else {
//                    Intent in = new Intent(MainActivity.this, MainActivity.class);
//                    startActivity(in);
//                    finish();
//                }
//
//            }
//        },2000);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                String s_classid=classid.getText().toString();
                String s_classpassword=classpassword.getText().toString();



          if (s_classid.equals("boss")) {
                    Intent in = new Intent(MainActivity.this, admin.class);
                    startActivity(in);
                    finish();

                }


                if (!s_classid.equals("") && !s_classpassword.equals("")){
                    progressDialog.show();
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    CollectionReference coll=firestore.collection("class");

                coll.document(s_classid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot db) {


                            String pass=String.valueOf(db.get("classpassword"));
                            if(pass.equals(s_classpassword)){
                                progressDialog.cancel();

                                Intent intent=new Intent(MainActivity.this,home.class);


                                SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putBoolean("hasClassLogIn",true);
                                editor.putString("classid",s_classid);
                                editor.commit();



                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //clear backstack
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);


                                startActivity(intent);
                                finish();
                            }else{
                                progressDialog.cancel();
                                Toast.makeText(MainActivity.this, "ENTERED PASSWORD IS WRONG", Toast.LENGTH_SHORT).show();
                            }

                        }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();
                        Toast.makeText(MainActivity.this, "ENTER VALID CLASS ID ", Toast.LENGTH_SHORT).show();
                    }
                });}
                else{
                    Toast.makeText(MainActivity.this, "ENTER THE CREDENTIALS", Toast.LENGTH_SHORT).show();
                }












            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, class_signup.class);
                startActivity(in);







            }
        });
        pass_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s_classid = classid.getText().toString();
                if (!s_classid.equals("")) {





                        progressDialog.show();
                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                        CollectionReference coll=firestore.collection("class");

                        coll.document(s_classid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot db) {


                               String class_email=db.getString("classemail");
                                fauth.sendPasswordResetEmail(class_email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.cancel();
                                        Toast.makeText(MainActivity.this, "EMAIL SENT", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.cancel();
                                        Toast.makeText(MainActivity.this, " CLASS ID NOT FOUND ", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }


                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(MainActivity.this, "ENTER VALID CLASS ID ", Toast.LENGTH_SHORT).show();
                            }
                        });





                }
                else{
                    Toast.makeText(MainActivity.this, "PLEASE ENTER EMAIL TO SENT LINK ", Toast.LENGTH_SHORT).show();
                }
            }

        });



    }
}