package com.example.capstonemap;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.capstonemap.user.UserDto;
import com.example.capstonemap.user.UserRepository;

public class SignUpDialog extends Dialog {
    static UserRepository userRepository=new UserRepository();
    public SignUpDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 바 제거
        setContentView(R.layout.dialog_signup);

        // 다이얼로그를 풀스크린으로 설정
        if (getWindow() != null) {
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        EditText usernameEditText = findViewById(R.id.signup_username_edit_text);
        EditText passwordEditText = findViewById(R.id.signup_password_edit_text);
        Button signUpButton = findViewById(R.id.signup_button);

        // 회원가입 버튼 클릭 시
        signUpButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            UserDto userDto = new UserDto(username, password);

            // 간단한 회원가입 로직
            if (!username.isEmpty() && !password.isEmpty()) {
                saveLogin(userDto);
                Toast.makeText(getContext(), "회원가입 성공! 이제 로그인하세요.", Toast.LENGTH_SHORT).show();
                dismiss(); // 회원가입 다이얼로그 닫기
                new LoginDialog(getContext()).show(); // 로그인 다이얼로그 다시 표시
            } else {
                Toast.makeText(getContext(), "아이디 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void saveLogin(UserDto userDto){

        if(userDto != null){
            //리파지토리, 스프링과 연동해서 경로를 만들면 저장함
            userRepository.saveLogin(userDto,
                    () -> System.out.println("User saved successfully!"),
                    () -> System.out.println("Error saving user")
            );
        }else{
            System.out.println("null route");
        }
    }
}
