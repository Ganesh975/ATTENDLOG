package com.example.attendlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import io.grpc.internal.DnsNameResolver;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class class_settings extends AppCompatActivity {

    Button addstudent;

    ListView li;
    ArrayList<String> stupino,stusino,stuname;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_settings);


        addstudent=findViewById(R.id.but_addstudent);
        li=findViewById(R.id.student_list);

        stusino=new ArrayList<>();
        stuname=new ArrayList<>();
        stupino=new ArrayList<>();

        progressDialog=new ProgressDialog(this);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference coll=firestore.collection("class");

        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
        boolean haslogedin=sharedPreferences.getBoolean("hasClassLogIn",false);
        String s_classid=sharedPreferences.getString("classid",null);

        Query query = firestore.collection("class").document(s_classid).collection("students").orderBy("stusino_numeric", Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    progressDialog.show();
                    QuerySnapshot document = task.getResult();
                    if (!document.isEmpty()) {
                        for(DocumentSnapshot doc:document){
                            stupino.add(doc.getString("stupino"));
                            stuname.add(doc.getString("stuname"));
                            stusino.add(doc.getString("stusino"));
                        }

                        progressDialog.cancel();

                        class_settings.CustomBaseAdapter cust=new class_settings.CustomBaseAdapter(getApplicationContext(),stusino,stupino,stuname);
                        li.setAdapter(cust);
                        progressDialog.cancel();



                    }
                    progressDialog.cancel();

                    }
            }
        });

        progressDialog.cancel();




        addstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(class_settings.this, add_student.class);
                startActivity(in);
            }
        });
    }

    class CustomBaseAdapter extends BaseAdapter {
        Context context;

        ArrayList<String> l_stupino,l_stusino,l_stuname;


        LayoutInflater inflater;

        public CustomBaseAdapter(Context applicationContext, ArrayList<String> stusino, ArrayList<String> stupino,ArrayList<String> stuname) {
            this.context=applicationContext;

            this.l_stusino=stusino;
            this.l_stupino=stupino;
            this.l_stuname=stuname;
            inflater=LayoutInflater.from(applicationContext);
        }

        @Override
        public int getCount() {
            return l_stuname.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=inflater.inflate(R.layout.student_pin_list,null);
            TextView stusino=view.findViewById(R.id.stu_sino);
            TextView stupino=view.findViewById(R.id.stu_pino);
            TextView stuname=view.findViewById(R.id.stu_name);

            stusino.setText(l_stusino.get(i));
            stupino.setText(l_stupino.get(i));
            stuname.setText(l_stuname.get(i));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(class_settings.this,student_edit.class);
                    intent.putExtra("student sino",l_stusino.get(i));
                    intent.putExtra("student pino",l_stupino.get(i));
                    intent.putExtra("student name",l_stuname.get(i));

                    startActivity(intent);
                }
            });
            return view;
        }


    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}

