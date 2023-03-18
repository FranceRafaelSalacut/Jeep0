package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RouteSelect extends AppCompatActivity {

    DatabaseHelper myDB;
    Button btnFindRoutes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_select);
        myDB = new DatabaseHelper(this);
        btnFindRoutes = (Button) findViewById(R.id.button_findroutes);
        FindRoutes();
    }

    public void FindRoutes(){
        TextView txt =(TextView) findViewById(R.id.routeList);
        btnFindRoutes.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor res = myDB.getAllData();
                        if(res.getCount() == 0){
                            //ShowMsg("Error", "No data yet");
                            Toast.makeText(RouteSelect.this, "No data yet",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        StringBuffer bfr = new StringBuffer();
                        while(res.moveToNext()){
                            bfr.append("ID: " + res.getString(0) + "\n");
                            bfr.append("Code: " + res.getString(1) + "\n");
                            bfr.append("Location ID: " + res.getString(2) + "\n");
                            bfr.append("Location: " + res.getString(3) + "\n\n");
                        }
                        txt.setText(bfr.toString());
                        //ShowMsg("Data", bfr.toString());
                    }
                }
        );
    }

//    public void ShowMsg(String title, String msg){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(true);
//        builder.setTitle(title);
//        builder.setMessage(msg);
//        builder.show();
//    }
}