package com.angelo_silvestre.docunoteapplication;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {
    private static final String Tag = "MainActivity";
    private static final int  SPLASH_SCREEN = 500;


    Button backBtn, signUpBtn;
    EditText name, email, password, cPassword;
    TextView nameErr, emailErr, passwordErr, cPasswordErr;

    private static String passName = "";
    private static String passEmail = "";
    private static String passPassword = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();


        backBtn.setOnClickListener(view -> new Handler().postDelayed(() -> {
            Intent intent = new Intent(SignUp.this,MainActivity.class);
            startActivity(intent);

            finish();
        },SPLASH_SCREEN));

        signUpBtn.setOnClickListener(view -> {
            isValidName();
            isValidEmail();
            isValidPassword();
            isValidCPassword();

            if(isValidName() && isValidEmail() && isValidPassword() && isValidCPassword()){
                new Handler().postDelayed(() -> {

                    // Todo : Save the user data to the firebase database
                    setPassName(String.valueOf(name.getText()));
                    setPassEmail(String.valueOf(email.getText()));
                    setPassPassword(String.valueOf(password.getText()));
                    Intent intent = new Intent(SignUp.this,MainActivity.class);
                    startActivity(intent);

                    finish();
                },SPLASH_SCREEN);
            } else {
                Toast.makeText(SignUp.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private Boolean isValidAllInput(){
//        isValidName();
//        isValidEmail();
//        isValidPassword();
//        isValidCPassword();
//        return isValidName() && isValidEmail() && isValidPassword() && isValidCPassword();
//
//
//    }

    @SuppressLint("SetTextI18n")
    private Boolean isValidName(){
        String pattern = "[a-z A-Z]+";

        if(name.getText().toString().isEmpty()){
            nameErr.setText("Name is required");
            return false;
        }
        else if(!name.getText().toString().matches(pattern)){
            nameErr.setText("Name must be letters only");
            return false;
        }else{

            nameErr.setText("");
            return true;
        }
    }

    @SuppressLint("SetTextI18n")
    private Boolean isValidEmail(){
        String pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(email.getText().toString().isEmpty()){
            emailErr.setText("Email is required");
            return false;
        } else if(!email.getText().toString().matches(pattern)){
            emailErr.setText("Email must be valid");
            return false;
        }else{
            emailErr.setText("");
            return true;
        }
    }

    @SuppressLint("SetTextI18n")
    public Boolean isValidPassword() {
        String pass = password.getText().toString();
        String upperCaseChars = "(.*[A-Z].*)";
        String lowerCaseChars = "(.*[a-z].*)";
        String numbers = "(.*[0-9].*)";
        String specialChars = "(.*[@!#*$%].*$)";
        boolean isValid = true;

        if (pass.isEmpty()) {
            passwordErr.setText("Password is required");
            isValid = false;
        }
        else if (pass.length() > 20 || pass.length() < 8)
        {
            passwordErr.setText("Password must be less than 20 and more than 8 characters in length.");
            isValid = false;
        }

        else if (!pass.matches(upperCaseChars ))
        {
            passwordErr.setText("Password must have atleast one uppercase character");
            isValid = false;
        }

        else if (!pass.matches(lowerCaseChars ))
        {
            passwordErr.setText("Password must have atleast one lowercase character");
            isValid = false;
        }

        else if (!pass.matches(numbers ))
        {
            passwordErr.setText("Password must have atleast one number");
            isValid = false;
        }

        else if (!pass.matches(specialChars ))
        {
            passwordErr.setText("Password must have atleast one special character among @#$%");
            isValid = false;
        }

        else  {
            passwordErr.setText("");
        }
        return isValid;
    }

    @SuppressLint("SetTextI18n")
    public Boolean isValidCPassword() {
        String pass = password.getText().toString();
        String cPass = cPassword.getText().toString();
        boolean isValid = true;
        if (cPass.isEmpty()) {
            cPasswordErr.setText("Confirm Password is required");
            isValid = false;
        }
        else if (!pass.equals(cPass))
        {
            cPasswordErr.setText("Password and confirm password is not match");
            isValid = false;
        }
        else  {
            cPasswordErr.setText("");
        }
        return isValid;
    }
//
//    @SuppressLint("SetTextI18n")
//    private Boolean emptyFields(){
//        boolean emptyInp = false;
//
//        String nameStr = name.getText().toString();
//        String emailStr = email.getText().toString();
//        String passwordStr = password.getText().toString();
//        String cPasswordStr = cPassword.getText().toString();
//
//
//        if(nameStr.isEmpty() || emailStr.isEmpty() || passwordStr.isEmpty() || cPasswordStr.isEmpty()){
//
//            if(nameStr.isEmpty()) {
//                Log.d(Tag,"Name is empty");
//                nameErr.setText("Name is required");
//                emptyInp = true;
//            } else {
//                Log.d(Tag,"Name is not empty");
//                nameErr.setText("");
//            }
//            if(emailStr.isEmpty()){
//                Log.d(Tag,"Email is empty");
//                emailErr.setText("Email is required");
//                emptyInp = true;
//            } else  {
//                Log.d(Tag,"Email is not empty");
//                emailErr.setText("");
//            }
//            if(passwordStr.isEmpty()){
//                Log.d(Tag,"Password is empty");
//                passwordErr.setText("Password is required");
//                emptyInp = true;
//            } else{
//                Log.d(Tag,"Password is not empty");
//                passwordErr.setText("");
//
//            }
//
//            if(cPasswordStr.isEmpty()){
//                Log.d(Tag,"Confirm Password is empty");
//                cPasswordErr.setText("Confirm password is required");
//                emptyInp = true;
//            } else {
//                Log.d(Tag,"Confirm Password is not empty");
//                cPasswordErr.setText("");
//            }
//        } else {
//            emptyInp = false;
//        }
//
//        return emptyInp;
//
//    }


    private void initViews(){
        Log.d(Tag, "InitViews: Started");

        // BUTTONNS
        backBtn = findViewById(R.id.btn_go_back_to_login_ca);
        signUpBtn = findViewById(R.id.create_account_btn);

        // EDIT TEXT
        name = findViewById(R.id.name_edtTxt);
        email = findViewById(R.id.email_edtTxt);
        password = findViewById(R.id.password_edtTxt);
        cPassword = findViewById(R.id.cpassword_edtTxt);

        // TEXT VIEW ERROR
        nameErr = findViewById(R.id.name_err_txtView);
        emailErr = findViewById(R.id.email_err_txtView);
        passwordErr = findViewById(R.id.password_err_txtView);
        cPasswordErr = findViewById(R.id.cpassword_err_txtView);



    }


    public String getPassName() {
        return passName;
    }

    public void setPassName(String passName) {
        this.passName = passName;
    }

    public String getPassEmail() {
        return passEmail;
    }

    public void setPassEmail(String passEmail) {
        this.passEmail = passEmail;
    }

    public String getPassPassword() {
        return passPassword;
    }

    public void setPassPassword(String passPassword) {
        this.passPassword = passPassword;
    }
}