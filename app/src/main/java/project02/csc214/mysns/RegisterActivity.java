package project02.csc214.mysns;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import project02.csc214.mysns.model.Profile;
import project02.csc214.mysns.model.UserAccount;

public class RegisterActivity extends AppCompatActivity {

    private static final String DBNAME = "MainDB";
    private static final int RC_REGIST = 8;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mUsername;
    private Button mRegist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        mUsername = (EditText) findViewById(R.id.name_regist);
        mEmail = (EditText) findViewById(R.id.email_regist);
        mPassword = (EditText) findViewById(R.id.password_regist);
        mRegist = (Button) findViewById(R.id.register_regist);

        mRegist.setOnClickListener(new View.OnClickListener() {

            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, DBNAME).allowMainThreadQueries().build();

            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                if (username!=null && email!=null && password!=null){
                    String info = "Info:";
                    if (!email.contains("@")){
                        info = info + "\nPlease enter valid email address";
                    }
                    if (username.length()<4){
                        info = info + "\nPlease enter username at least 4 characters long";
                    }
                    if (password.length()<8){
                        info = info+ "\nPlease enter password at least 8 characters long";
                    }
                    if (email.contains("@")&& username.length()>=4 && password.length()>=8){
                        if (db.userAccountDao().findByEmail(email).size()<1) {
                            UserAccount newuser = new UserAccount(username, email, password);
                            db.userAccountDao().insert(newuser);
                            Gson gson = new Gson();
                            ArrayList<Integer> like_list = new ArrayList<Integer>();
                            like_list.add(db.userAccountDao().findByEmail(email).get(0).getUid());
                            String input = gson.toJson(like_list);
                            db.profileDao().insert(new Profile(" ", " ", " ", " ", " ", " ", input));
                            setResult(RESULT_OK);
                            db.close();
                            finish();
                        }else{
                            Toast toast = Toast.makeText(RegisterActivity.this,"Email already registered",Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }else{
                        Toast toast = Toast.makeText(RegisterActivity.this,info,Toast.LENGTH_LONG);
                        toast.show();

                    }
                }else{
                    Toast toast = Toast.makeText(RegisterActivity.this,"Please fill in all the blanks",Toast.LENGTH_LONG);
                    toast.show();

                }
            }
        });
    }
}
