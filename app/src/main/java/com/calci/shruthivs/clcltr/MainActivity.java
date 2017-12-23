package com.calci.shruthivs.clcltr;

import android.content.Intent;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public String val1="", val2="", op="";
    public boolean flag=true;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void displayFormula(View view) {
        TextView tv = (TextView) findViewById(R.id.result);

        Button b = (Button)findViewById(view.getId());
        String buttonText = b.getText().toString();

        Log.v("MainActivity", "buttonText:  " + buttonText);
        Log.v("MainActivity", "val1:  " + val1);
        Log.v("MainActivity", "op:  " + op);
        Log.v("MainActivity", "val2:  " + val2);

        if(buttonText.equals("+") || buttonText.equals("-") || buttonText.equals("*") || buttonText.equals("/")){
            if(val1.equals("")) return;
            op=buttonText;
            flag=false;
            tv.setText(val1+op);
        }
        else if(flag && op.equals("")){
            val1+=buttonText;
            tv.setText(val1);
        }
        else if( !flag ){
            val2+=buttonText;
            tv.setText(val1+op+val2);
        }
    }

    public void clearScreen(View view){
        TextView tv = (TextView) findViewById(R.id.result);
        val1="";
        val2="";
        op="";
        flag=true;
        tv.setText("0");
    }

    public void calculateResult(View view){

        Double value1=0.0, value2=0.0, finalResult=0.0;

        try {
            value1 = Double.valueOf(val1);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        try {
            value2 = Double.valueOf(val2);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        switch (op){
            case "+": finalResult = value1 + value2;
                break;
            case "-": finalResult = value1 - value2;
                break;
            case "*": finalResult = value1 * value2;
                break;
            case "/": if(value2 != 0)   finalResult = value1 / value2;
                else{
                    TextView tv = (TextView) findViewById(R.id.result);
                    tv.setText("DivisionbyZeroError!!");
                    return;
                }
                break;
        }
        showResult(finalResult);
    }

    public void showResult(Double fRes){

        fRes = Math.round(fRes * 1000000D) / 1000000D;

        String fR="";

        Double tempRes=0.0;
        tempRes = fRes - fRes.intValue();

        if(tempRes == 0.0)   fR = Integer.toString(fRes.intValue());
        else fR = Double.toString(fRes);

        TextView tv = (TextView) findViewById(R.id.result);
        tv.setText(fR);
        val1=fR;
        val2="";
        op="";
        flag=true;
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}