package company.my.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String textField;
    private boolean isOperationUsed=false;
    private char Operation = ' ';

    private boolean hasTwoOperands=false;
    private double a = 0;
    private int razradA=0;
    private int razradAfterDotA=1;
    private boolean dotA=false;

    private double b = 0;
    private int razradB=0;
    private int razradAfterDotB=1;
    private boolean dotB=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.toolbar_name));
        toolbar.setSubtitle(getString(R.string.toolbar_primary_text));
        setListener();
//        btn.setAnimation(myAnim);
        //setSupportActionBar(toolbar);
    }

    private void addNumber(int n){
        if (isOperationUsed == false){
            if(dotA == false){

                a =  (a*Math.pow(10,razradA))+n;
                razradA++;
            }
            else {

                a+= n/Math.pow(10,razradAfterDotA);
                razradAfterDotA++;
            }
        }
        else {
            if(dotB == false){
                b = b*Math.pow(10,razradB)+n;
                razradB++;
                hasTwoOperands = true;
            }
            else{
                b+= n/Math.pow(10,razradAfterDotB);
                razradAfterDotB++;
            }
        }
    }
    private void addSymbol(String symbol){
        if (textField==null) {
            textField=symbol;
            addNumber(Integer.parseInt(symbol));
            return;
        }
        if (symbol.equals("0")){
            if (textField.length() == 1){
                if (textField.indexOf("0") == 0) return;
            }
            else {
                if(isOperationUsed == true && dotB == false){
                    if (textField.length() -1 == textField.lastIndexOf("0")){
                        return;
                    }
                }
            }
        }
        addNumber(Integer.parseInt(symbol));
        textField += symbol;
    }
    private boolean isOperation(){
        if (textField.lastIndexOf("+") == textField.length()-1 || textField.lastIndexOf("-") == textField.length()-1 ||
                textField.lastIndexOf("*") == textField.length()-1 || textField.lastIndexOf("/") == textField.length()-1)
            return true;
        else return false;

    }
    private boolean isDot(){
        if(textField.lastIndexOf(".") == textField.length()-1) return true;
        else
            return false;
    }
    private void addDot(){
        if(textField.length() == 0) return;
        if (textField.length() -1 == textField.lastIndexOf(".")){
            return;
        }
        textField += ".";
        if(isOperationUsed == false) dotA = true;
        else
            dotB = true;
    }
    private void delete(){
        if (textField == null || textField.length() == 0) return;
        if (isDot() == true) {
            if (isOperationUsed == true) dotB=false;
            else
                dotA=false;
            textField = textField.substring(0,textField.length()-1);
            return;
        }
        if(isOperation() == true){
            Operation = ' ';
            isOperationUsed=false;
            textField = textField.substring(0,textField.length()-1);
            return;
        }
        if(textField.lastIndexOf("0") == textField.length()-1) {
            textField = textField.substring(0,textField.length()-1);
            return;
        }
        if (textField.length() == 1){
            a=0;
            textField = "";
            return;
        }

        if (isOperationUsed == false){
            if (dotA == false) {
                razradA--;
                if(razradA == 0) {
                    a=0;
                    textField = "";
                    return;
                }
            }
            else{
                razradAfterDotA--;
            }
            a = Double.parseDouble(textField.substring(0, textField.length() - 1));
        }
        else {
            if(dotB == false){
                razradB--;
                if (razradB == 0){
                    b=0;
                    hasTwoOperands = false;
                    textField = textField.substring(0,textField.length()-1);
                    return;
                }
            }
                else{
                 razradAfterDotB--;
               }
        }
        b = Double.parseDouble(textField.substring(textField.indexOf(Operation)+1,textField.length()));
        textField = textField.substring(0,textField.length()-1);
    }
    private void addOperation(char s){
        if (textField.length() == 0) return;
        Operation = s;
        isOperationUsed = true;
        textField += s;
    }

    private void answer(){

        if (isOperationUsed == false){
            Toast toast = Toast.makeText(MainActivity.this, "Нужна хотя бы одна операция", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if(hasTwoOperands == false){
            Toast toast = Toast.makeText(MainActivity.this, "Нужнен ещё один операнд", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        switch (Operation){
            case '+':
                a += b;
                break;
            case '-':
                a -= b;
                break;
            case '*':
                a *= b;
                break;
            case '/':
                if (b == 0) {
                    Toast toast = Toast.makeText(MainActivity.this, "На 0 делить нельзя", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                a /= b;
                break;
        }

        if (a % 1 == 0)  {
            int ab = (int) a;
            textField = Integer.toString(ab);
            isOperationUsed = false;
            Operation = ' ';
            razradA = textField.length();
            razradAfterDotA = 1;
            dotA = false;
            b = 0;
            razradAfterDotB = 1;
            razradB = 0;
            dotB = false;
            return;
        }

        textField = Double.toString(a);
        if (textField.indexOf(".") == -1) {
            razradAfterDotA = 1;
            dotA = false;
            razradA = textField.length();
        }
        else {
            dotB = true;
            razradAfterDotA = textField.length() - textField.indexOf(".");
            razradA = textField.length()-razradAfterDotA;
        }
        b=0;
        razradAfterDotB = 1;
        razradB = 0;
        isOperationUsed = false;
        Operation = ' ';
        dotB = false;
    }

    @Override
    public void onClick(View view) {


       final Animation myAnim=AnimationUtils.loadAnimation(this, R.anim.myanim);
        view.startAnimation(myAnim);


        TextView textView = (TextView) findViewById(R.id.text);
        switch (view.getId()) {
            case R.id.button0:
                addSymbol("0");
                break;
            case R.id.button1:
                addSymbol("1");
                break;
            case R.id.button2:
                addSymbol("2");
                break;
            case R.id.button3:
                addSymbol("3");
                break;
            case R.id.button4:
                addSymbol("4");
                break;
            case R.id.button5:
                addSymbol("5");
                break;
            case R.id.button6:
                addSymbol("6");
                break;
            case R.id.button7:
                addSymbol("7");
                break;
            case R.id.button8:
                addSymbol("8");
                break;
            case R.id.button9:
                addSymbol("9");
                break;
            case R.id.buttonDot:
                addDot();
                break;
            case R.id.buttonDel:
                delete();
                break;
            case R.id.buttonAdd:
                addOperation('+');
                break;
            case R.id.buttonMul:
                addOperation('*');
                break;
            case R.id.buttonSub:
                addOperation('-');
                break;
            case R.id.buttonDiv:
                addOperation('/');
                break;
            case R.id.buttonEqual:
                answer();
                break;
            case R.id.buttonUndo:
                textField = "";

                isOperationUsed = false;
                Operation = ' ';
                a = 0;
                razradA = 0;
                razradAfterDotA = 1;
                dotA = false;
                b = 0;
                razradAfterDotB = 1;
                razradB = 0;
                dotB = false;
                break;
            default:break;
        }
        textView.setText(textField);

    }

    public void setListener()
    {
        findViewById(R.id.buttonUndo).setOnClickListener(this);
        findViewById(R.id.button0).setOnClickListener(this);
        findViewById(R.id.buttonDiv).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
        findViewById(R.id.buttonAdd).setOnClickListener(this);
        findViewById(R.id.buttonDel).setOnClickListener(this);
        findViewById(R.id.buttonDot).setOnClickListener(this);
        findViewById(R.id.buttonEqual).setOnClickListener(this);
        findViewById(R.id.buttonMul).setOnClickListener(this);
        findViewById(R.id.buttonSub).setOnClickListener(this);

    }

}
