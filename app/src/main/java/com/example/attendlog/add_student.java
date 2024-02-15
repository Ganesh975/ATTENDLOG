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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class add_student extends AppCompatActivity {

    Button butconfirm;

    EditText stusino,stupino,stuname;
    ProgressDialog progressDialog;
    String newStudentCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);


        butconfirm=this.findViewById(R.id.but_addstud);
        stusino=this.findViewById(R.id.stu_sino);
        stupino=this.findViewById(R.id.stu_pino);
        stuname=this.findViewById(R.id.stu_name);

        progressDialog=new ProgressDialog(this);

        progressDialog.show();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference coll=firestore.collection("class");

        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
        boolean haslogedin=sharedPreferences.getBoolean("hasClassLogIn",false);
        String s_classid=sharedPreferences.getString("classid",null);

        coll.document(s_classid).collection("students").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                int studentCount = queryDocumentSnapshots.size() + 1;
                newStudentCount = String.valueOf(studentCount);
                stusino.setText(newStudentCount);

                progressDialog.cancel();

                // Now 'newStudentCount' contains the incremented count as a string
            }
        });



        progressDialog.cancel();


        butconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();







                String s_stusino=stusino.getText().toString();
                String s_stupino=stupino.getText().toString();
                String s_stuname=stuname.getText().toString();


                if (!s_stupino.equals("") && !s_stusino.equals("")) {





// Assuming coll is your Firestore instance
                    DocumentReference studentRef = coll.document(s_classid).collection("students").document(s_stupino);

                    studentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    // Document already exists, handle accordingly (e.g., show an error message)
                                    Toast.makeText(add_student.this, "Student with ID already exists!", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Document does not exist, add a new student
                                    coll.document(s_classid).collection("students")
                                            .document(s_stupino)
                                            .set(new studentmodel(s_stusino, s_stupino, s_stuname, Integer.parseInt(s_stusino)))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Student added successfully
                                                    Toast.makeText(add_student.this, "Student added successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Handle failure
                                                    Toast.makeText(add_student.this, "Error adding student: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                // Handle failure
                                Toast.makeText(add_student.this, "Error checking document: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });




                    coll.document(s_classid).collection("students").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            int size=queryDocumentSnapshots.size();

                            stusino.setText(String.valueOf(size+1));
                            stuname.setText("");
                            stupino.setText("");


                        }
                    });

                    coll.document(s_classid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            coll.document(s_classid).update("studentcount",String.valueOf(Integer.valueOf(documentSnapshot.getString("studentcount"))+1));
//                            Integer.valueOf(documentSnapshot.getString("studentcount"))+1)


                            progressDialog.cancel();

                        }
                    });



                }else{

                    Toast.makeText(add_student.this, "FILL THE DETAILS", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
            }

        });

        progressDialog.cancel();


    }
}