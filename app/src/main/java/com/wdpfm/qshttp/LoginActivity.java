package com.wdpfm.qshttp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.song.http.QSHttp;
import org.song.http.framework.HttpException;
import org.song.http.framework.QSHttpCallback;
import org.song.http.framework.ResponseParams;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("登录");
        final String TAG="登录";
        QSHttp.init(getApplication());
        final EditText myUsername = findViewById(R.id.logUsername);
        final EditText myPassword = findViewById(R.id.logPassword);
        final TextView myTextview = findViewById(R.id.logTextView);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button goRegister = findViewById(R.id.goRegister);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=myUsername.getText().toString();
                String password=myPassword.getText().toString();
                if (!username.isEmpty()&&!password.isEmpty()){
                    UserBean user=new UserBean(username, Md5.encrypt(password));
                    String url = "https://hb5.api.okayapi.com/?s=App.User.Login"+Key.key;
                    QSHttp.postJSON(url)
                            .jsonBody(user)
                            .jsonModel(LoginRoot.class)
                            .buildAndExecute(new QSHttpCallback<LoginRoot>() {
                                @Override
                                public void onSuccess(ResponseParams response) {
                                    super.onSuccess(response);
                                    myTextview.setText(response.string());
                                }

                                @Override
                                public void onComplete(LoginRoot data) {
                                    switch (data.getLoginData().getErr_code()){
                                        case 0:
                                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                            String uuid=data.getLoginData().getUuid();//全局唯一用户ID
                                            break;
                                        case 1:
                                            Toast.makeText(LoginActivity.this, "登录失败，账号不存在", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 2:
                                            Toast.makeText(LoginActivity.this, "登录失败，密码错误", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(LoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
class LoginData
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

class LoginRoot
{
    private int ret;

    private LoginData data;

    private String msg;

    public void setRet(int ret){
        this.ret = ret;
    }
    public int getRet(){
        return this.ret;
    }
    public void setData(LoginData data){
        this.data = data;
    }
    public LoginData getLoginData(){
        return this.data;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
}


