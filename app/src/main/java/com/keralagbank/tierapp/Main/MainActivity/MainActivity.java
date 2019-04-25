package com.keralagbank.tierapp.Main.MainActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.keralagbank.tierapp.Common.CommonFunctions;
import com.keralagbank.tierapp.R;

public class MainActivity extends AppCompatActivity {

    CommonFunctions myCF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myCF = new CommonFunctions();
        final Button button = findViewById(R.id.btnSubmit);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSubmitClick();
            }
        });
    }

    private void onSubmitClick()
    {
        MainActivity_Bus objBus = new MainActivity_Bus();
        EditText txtNo1 = findViewById(R.id.txtNo1);
        EditText txtNo2 = findViewById(R.id.txtNo2);
        EditText txtNo3 = findViewById(R.id.txtResult);
        String op = objBus.CallWebservice(txtNo1.getText().toString(),txtNo2.getText().toString(),"D3",this);
        txtNo3.setText("Loading");


    }
}
