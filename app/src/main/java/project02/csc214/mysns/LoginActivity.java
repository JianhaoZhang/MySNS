package project02.csc214.mysns;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import project02.csc214.mysns.model.UserAccount;

public class LoginActivity extends AppCompatActivity{

    private static final String STATUS = "IsLogged";
    private static final String DBNAME = "MainDB";
    private static final String USER = "CurrentUser";
    private static final int RC_REGIST = 8;
    private EditText mEmail;
    private EditText mPassword;
    private Button mLogin;
    private Button mRegist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mLogin = (Button) findViewById(R.id.sign_in);
        mRegist = (Button) findViewById(R.id.register);

        mLogin.setOnClickListener(new View.OnClickListener(){

            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, DBNAME).allowMainThreadQueries().build();
            @Override
            public void onClick(View v){
                Intent result = new Intent();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                if (email!=null && password!=null){
                    ArrayList<UserAccount> target_list = (ArrayList<UserAccount>) db.userAccountDao().findByEmail(email);
                    db.close();
                    if (target_list.size()>0){
                        String targetpw = target_list.get(0).getPassword();
                        if (password.equals(targetpw)){
                            result.putExtra(STATUS,true);
                            result.putExtra(USER,target_list.get(0).getUid());
                            setResult(RESULT_OK,result);
                            finish();
                        }else{
                            Toast toast = Toast.makeText(LoginActivity.this,"Login Failed, please check password",Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }else{
                        Toast toast = Toast.makeText(LoginActivity.this,"Account not found, please register first",Toast.LENGTH_LONG);
                        toast.show();
                    }
                }else{
                    Toast toast = Toast.makeText(LoginActivity.this,"Invalid Input",Toast.LENGTH_LONG);
                    toast.show();
                }


            }

        });

        mRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(register,RC_REGIST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (resultCode == RESULT_OK && requestCode == RC_REGIST) {
            Toast toast = Toast.makeText(LoginActivity.this,"Register successful, please login",Toast.LENGTH_LONG);
            toast.show();
        }
        if (resultCode == RESULT_CANCELED && requestCode == RC_REGIST) {
            Toast toast = Toast.makeText(LoginActivity.this,"Register failed",Toast.LENGTH_LONG);
            toast.show();
        }

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}

