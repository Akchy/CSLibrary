package com.drc.cslibraryadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText uname,pass;
        uname=(EditText) findViewById(R.id.uname);
        pass=(EditText)findViewById(R.id.pass);
        Button log=(Button) findViewById(R.id.b2);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uname.getText().toString().equals("drc") && pass.getText().toString().equals("drc")){
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Invalid Username or Password",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
