package com.example.attendlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class class_signup extends AppCompatActivity {

    Button register;






    ProgressDialog progressDialog;




    FirebaseFirestore firebaseFirestore;

    FirebaseAuth firebaseAuth;


    EditText classcourse,classbranch,classid,classsection,classpassword,classemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_signup2);

        register=findViewById(R.id.but_signup);
        classemail=findViewById(R.id.class_email);
        classcourse=this.findViewById(R.id.class_course);
        classbranch=this.findViewById(R.id.class_branch);
        classid=this.findViewById(R.id.class_id);
        classsection=this.findViewById(R.id.class_section);
        classpassword=this.findViewById(R.id.class_password);


        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        register=this.findViewById(R.id.but_signup);
        firebaseFirestore=FirebaseFirestore.getInstance();




        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                String s_classemail=classemail.getText().toString();
                String s_classcourse=classcourse.getText().toString();
                String s_classbranch=classbranch.getText().toString();
                String s_classid=classid.getText().toString();
                String s_classsection=classsection.getText().toString();
                String s_classpassword=classpassword.getText().toString();


                if(!s_classemail.equals("") && !s_classid.equals("") && !s_classcourse.equals("") && !s_classbranch.equals("") && !s_classsection.equals("") && !s_classpassword.equals("") ) {


                    firebaseAuth.createUserWithEmailAndPassword(s_classemail, s_classpassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            firebaseFirestore.collection("class/").document(s_classid).set(new classmodel(s_classemail, s_classcourse, s_classbranch, s_classid, s_classsection, s_classpassword,"0"));
                            progressDialog.cancel();

                            Toast.makeText(class_signup.this, "Class Registered Successfully", Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.U_LOGIN, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("hasClassLogIn", true);
                            editor.putString("classid", s_classid);
                            editor.commit();

                            Intent in = new Intent(class_signup.this, home.class);
                            startActivity(in);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.cancel();
                            Toast.makeText(class_signup.this, "UNABLE TO CREATE USER " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    Toast.makeText(class_signup.this, "FILL THE CREDENTIALS", Toast.LENGTH_SHORT).show();
                }





            }
        });
    }
}