package com.calci.shruthivs.clcltr;

import android.content.Intent;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public String previousVal="", currentVal="", op="";
    public boolean flag=false;

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


        if(buttonText.equals("+") || buttonText.equals("-") || buttonText.equals("*") || buttonText.equals("/") ){
            if(currentVal.equals("") && !flag ) return;
            //make the screen blink for button press
            Animation startAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinking_animation);
            tv.startAnimation(startAnim);
            //tv.clearAnimation();

            op=buttonText;

            if(!flag ){
                previousVal=currentVal;
                currentVal="";
                flag=true;
            }
        }
        else {
            currentVal+=buttonText;
            flag=false;
            tv.setText(currentVal);
        }
    }

    public void clearScreen(View view){
        TextView tv = (TextView) findViewById(R.id.result);
        currentVal="";
        previousVal="";
        op="";
        flag=false;
        tv.setText("0");
    }

    public void calculateResult(View view){

        Double value1=0.0, value2=0.0, finalResult=0.0;

        try {
            value1 = Double.valueOf(previousVal);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        try {
            value2 = Double.valueOf(currentVal);
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
                    currentVal="";
                    previousVal="";
                    op="";
                    flag=false;
                    return;
                }
                break;
        }
        showResult(finalResult);
    }

    public void showResult(Double fRes){

        fRes = Math.round(fRes * 10000000000D) / 10000000000D;

        String fR="";

        Double tempRes=0.0;
        tempRes = fRes - fRes.intValue();

        if(tempRes == 0.0)   fR = Integer.toString(fRes.intValue());
        else fR = Double.toString(fRes);

        TextView tv = (TextView) findViewById(R.id.result);
        tv.setText(fR);
        previousVal=fR;
        currentVal="";
        op="";
        flag=true;
    }

    /**
     * @param view
     * Converts input into its negative form.
     */
    public void reverseSign(View view){
        TextView tv = (TextView) findViewById(R.id.result);
        if(currentVal!="") {
            Double temp = -1 * Double.valueOf(currentVal);
            currentVal = Double.toString(temp);
            tv.setText(currentVal);
        }
        else{
            Double temp = -1 * Double.valueOf(previousVal);
            previousVal = Double.toString(temp);
            tv.setText(previousVal);
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}