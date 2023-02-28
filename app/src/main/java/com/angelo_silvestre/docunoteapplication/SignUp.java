package com.angelo_silvestre.docunoteapplication;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUp extends AppCompatActivity {
    private static final String Tag = "MainActivity";
    private static final int  SPLASH_SCREEN = 500;


    Button backBtn, signUpBtn;
    EditText name, email, password, cPassword;
    TextView nameErr, emailErr, passwordErr, cPasswordErr;

    private static String passName = "";
    private static String passEmail = "";
    private static String passPassword = "";


    private FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();

        Objects.requireNonNull(getSupportActionBar()).hide();
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
                    String nameAttr = name.getText().toString();
                    String emailAttr = email.getText().toString().trim();
                    String passwordAttr = password.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(emailAttr,passwordAttr)
                            .addOnCompleteListener(SignUp.this, task -> {
                                if (task.isSuccessful()) {
                                    UserProfile userProfile = new UserProfile(nameAttr,emailAttr,passwordAttr);

                                    FirebaseDatabase.getInstance().getReference("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Log.d(Tag, "onComplete Add the user name: Success");
                                                    }
                                                    else {
                                                        Log.d(Tag, "onComplete Add the user name: Failed");
                                                    }
                                                }
                                            });

                                    Toast.makeText(SignUp.this, "User created successfully", Toast.LENGTH_SHORT).show();
                                    sendEmailVerification();

                                } else {
                                    Toast.makeText(SignUp.this, "Email not registered: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d(Tag, "Error: " + task.getException().getMessage());
                                }
                            });

//                    Intent intent = new Intent(SignUp.this,MainActivity.class);
//                    startActivity(intent);
//
//                    finish();
                },SPLASH_SCREEN);
            } else {
                Toast.makeText(SignUp.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void saveNameOfUser(String name){

    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser!=null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(SignUp.this, task -> {
                if(task.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Verification email sent", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(SignUp.this, MainActivity.class));
                } else {
                    Toast.makeText(SignUp.this, "Email not registered: error found", Toast.LENGTH_SHORT).show();
                    Log.d(Tag, "Error: " + task.getException().getMessage());
                }
            });
        } else {
            Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
        }

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

        if(email.getText().toString().isEmpty()){
                emailErr.setText("Email is required");
            return false;
        } else{
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



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();



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