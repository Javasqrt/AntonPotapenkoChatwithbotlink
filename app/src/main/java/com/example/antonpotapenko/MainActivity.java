package com.example.antonpotapenko;


import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URI;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static int MAX_MESSAGE_LENGHT = 100;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("messages");
    public Button sendmessage;
    public EditText inputmessage;
    public TextView textbot;
    RecyclerView messagerecycle;

    ArrayList<String> messages = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendmessage = findViewById(R.id.send);
        inputmessage = findViewById(R.id.input_message);
        messagerecycle = findViewById(R.id.message_recycler);
        textbot = findViewById(R.id.textbot);

        messagerecycle.setLayoutManager(new LinearLayoutManager(this));

        final DataAdapter dataAdapter = new DataAdapter(this,messages);
        messagerecycle.setAdapter(dataAdapter);
        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mssg = inputmessage.getText().toString();
                textbot.setText("ItsBot: Good job! Click on this message to go to advertising");
                if(mssg.equals("")){
                    inputmessage.setError("Please,write a message!");
                    return;
                }
                if(mssg.length()> MAX_MESSAGE_LENGHT){
                    inputmessage.setError("So long message!");
                    return;
                }
                else {
                   textbot.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=_Hg_v2AG7Lw"));
                           startActivity(browserIntent);

                       }
                   });
                }
                myRef.push().setValue(mssg);
                inputmessage.setText("");
            }
        });
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String mssg = dataSnapshot.getValue(String.class);
                messages.add(mssg);
                dataAdapter.notifyDataSetChanged();
                messagerecycle.smoothScrollToPosition(messages.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}

