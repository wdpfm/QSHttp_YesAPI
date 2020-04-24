package com.wdpfm.qshttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.song.http.QSHttp;
import org.song.http.framework.HttpException;
import org.song.http.framework.QSHttpCallback;
import org.song.http.framework.ResponseParams;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("注册");
        final String TAG="注册";
        QSHttp.init(getApplication());
        final EditText myUsername = findViewById(R.id.regUsername);
        final EditText myPassword = findViewById(R.id.regPassword);
        final TextView myTextview = findViewById(R.id.regTextView);
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=myUsername.getText().toString();
                String password=Md5.encrypt(myPassword.getText().toString());
                if (!username.isEmpty()&&!password.isEmpty()){
                    UserBean user=new UserBean(username,password);
                    String url = "https://hb5.api.okayapi.com/?s=App.User.Register"+Key.key;
                    QSHttp.postJSON(url)
                            .jsonBody(user)
                            .jsonModel(RegisterRoot.class)
                            .buildAndExecute(new QSHttpCallback<RegisterRoot>() {
                                @Override
                                public void onSuccess(ResponseParams response) {
                                    super.onSuccess(response);
                                    myTextview.setText(response.string());
                                }

                                @Override
                                public void onComplete(RegisterRoot data) {
                                    switch (data.getRegisterData().getErr_code()){
                                        case 0:
                                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                            String uuid=data.getRegisterData().getUuid();//全局唯一用户ID
                                            finish();
                                            break;
                                        case 1:
                                            Toast.makeText(RegisterActivity.this, "用户已注册，不能重复注册", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }

                                @Override
                                public void onFailure(HttpException e) {
                                    super.onFailure(e);
                                    myTextview.setText(e.toString());
                                }
                            });
                }else{
                    Toast.makeText(RegisterActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

class RegisterData
{
    private int err_code;

    private String err_msg;

    private String uuid;

    private String token;

    private String role;

    public void setErr_code(int err_code){
        this.err_code = err_code;
    }
    public int getErr_code(){
        return this.err_code;
    }
    public void setErr_msg(String err_msg){
        this.err_msg = err_msg;
    }
    public String getErr_msg(){
        return this.err_msg;
    }
    public void setUuid(String uuid){
        this.uuid = uuid;
    }
    public String getUuid(){
        return this.uuid;
    }
    public void setToken(String token){
        this.token = token;
    }
    public String getToken(){
        return this.token;
    }
    public void setRole(String role){
        this.role = role;
    }
    public String getRole(){
        return this.role;
    }
}

class RegisterRoot
{
    private int ret;

    private RegisterData data;

    private String msg;

    public void setRet(int ret){
        this.ret = ret;
    }
    public int getRet(){
        return this.ret;
    }
    public void setData(RegisterData data){
        this.data = data;
    }
    public RegisterData getRegisterData(){
        return this.data;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
}