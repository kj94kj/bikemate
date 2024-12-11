package com.example.capstonemap;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.capstonemap.routes.RouteRepository;
import com.example.capstonemap.user.UserDto;
import com.example.capstonemap.user.UserRepository;

public class LoginDialog extends Dialog {
    static UserRepository userRepository=new UserRepository();
    static public UserDto currentUserDto;

    public LoginDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 바 제거
        setContentView(R.layout.dialog_login);

        // 다이얼로그를 풀스크린으로 설정
        if (getWindow() != null) {
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }

        // UI 요소 연결 및 이벤트 처리
        EditText usernameEditText = findViewById(R.id.username_edit_text);
        EditText passwordEditText = findViewById(R.id.password_edit_text);
        Button loginButton = findViewById(R.id.login_button);
        Button signUpButton = findViewById(R.id.signup_button);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            UserDto userDto = new UserDto(username, password);

            if(userDto != null){
                //리파지토리, 스프링과 연동해서 경로를 만들면 저장함
                userRepository.login(userDto,
                        userId -> {
                            // 로그인 성공 시 실행되는 코드
                            System.out.println("Login successfully!");
                            userDto.setId(userId); // userDto에 userId를 설정
                            MapsActivity.isLogined=true;
                            MapsActivity.userDto = userDto; // MapsActivity에 userDto 저장
                            currentUserDto=userDto;
                            dismiss(); // 다이얼로그 닫기
                        },
                        () -> {
                            // 로그인 실패 시 실행되는 코드
                            System.out.println("Error login");
                        }
                );
            }

            // 로그인 로직 처리// 다이얼로그 닫기
        });

        signUpButton.setOnClickListener(v -> {
            dismiss(); // 다이얼로그 닫기
            new SignUpDialog(getContext()).show(); // 회원가입 다이얼로그 호출
        });
    }
}
