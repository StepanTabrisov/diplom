package com.example.diplomproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterFragment extends Fragment {

    private EditText inputLogin;
    private EditText inputPassword;
    private TextView registrationLink;
    private Button enterButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.authorization_fragment, container, false);
        inputLogin = view.findViewById(R.id.a_input_login);
        inputPassword = view.findViewById(R.id.a_input_password);
        registrationLink = view.findViewById(R.id.a_registration_link);
        enterButton = view.findViewById(R.id.a_enter_button);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        RetrofitService retrofitService  = new RetrofitService();
        NetworkApi networkApi = retrofitService.getRetrofit().create(NetworkApi.class);

        registrationLink.setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, new RegisterFragment())
                .commit());

        enterButton.setOnClickListener(v -> {
            String login = ReadLogin();
            String password = ReadPassword();

            UserData userData = new UserData();
            userData.SetLogin(login);
            userData.SetPassword(password);

            new Thread(() -> {
                try {
                    Call<Boolean> call = networkApi.AuthUser(userData);
                    Response<Boolean> response = call.execute();
                    if(Boolean.TRUE.equals(response.body())){
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.putExtra(UserData.class.getSimpleName(), userData);
                        startActivity(intent);
                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Toast.makeText(getActivity(), "Неверный логин или пароль!", Toast.LENGTH_SHORT).show();
                            inputPassword.setText("");
                            inputLogin.setText("");
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    public String ReadPassword() {
        return inputPassword.getText().toString().trim();
    }

    private String ReadLogin() {
        return inputLogin.getText().toString().trim();
    }
}
