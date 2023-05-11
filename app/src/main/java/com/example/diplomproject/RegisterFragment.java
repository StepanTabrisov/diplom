package com.example.diplomproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    EditText inputName;
    EditText inputLogin;
    EditText inputEmail;
    EditText inputPassword;
    EditText inputAcceptPassword;
    Button regButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);
        inputName = view.findViewById(R.id.r_input_name);
        inputLogin = view.findViewById(R.id.r_input_login);
        inputEmail = view.findViewById(R.id.r_input_email);
        inputPassword = view.findViewById(R.id.r_input_password);
        inputAcceptPassword = view.findViewById(R.id.r_input_accept_password);
        regButton = view.findViewById(R.id.registration_button);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        RetrofitService retrofitService  = new RetrofitService();
        NetworkApi networkApi = retrofitService.getRetrofit().create(NetworkApi.class);

        regButton.setOnClickListener(v -> {

            String name = inputName.getText().toString();
            String login = inputLogin.getText().toString();
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();

            UserData userData = new UserData();
            userData.SetName(name);
            userData.SetLogin(login);
            userData.SetEmail(email);
            userData.SetPassword(password);

            //Правильный вариант (финальный) раскомментировать !
            /*try {
                Call<Boolean> call = networkApi.RegUser(userData);
                Response<Boolean> response = call.execute();
                if(Boolean.TRUE.equals(response.body())){
                    startActivity(new Intent(getContext(), MainActivity.class));
                } else {
                    // Вывод ошибки на экран
                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            //неправильнй вариант
            networkApi.RegistrationUser(userData).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(getActivity(), "Users add success", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
            startActivity(new Intent(getContext(), MainActivity.class));
        });
    }
}
