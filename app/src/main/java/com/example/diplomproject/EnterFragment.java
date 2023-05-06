package com.example.diplomproject;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterFragment extends Fragment {

    EditText inputLogin;
    EditText inputPassword;
    TextView registrationLink;
    Button enterButton;

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

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = inputLogin.getText().toString();
                String password = inputPassword.getText().toString();
                System.out.println(login);

                networkApi.CheckUserData(login.trim()).enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        Toast.makeText(getActivity(), "User find", Toast.LENGTH_SHORT).show();
                        UserData user = response.body();
                        System.out.println(response.body());
                        Log.i("ON RESPONSE", user.toString());
                    }

                    @Override
                    public void onFailure(Call<UserData> call, Throwable t) {
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                        System.out.println(t.getMessage());
                        Logger.getLogger(getActivity().getClass().getName()).log(Level.SEVERE, "Error find", t);
                    }
                });

               /* networkApi.getAllUsers().enqueue(new Callback<List<UserData>>() {
                    @Override
                    public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                        System.out.println(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<UserData>> call, Throwable t) {

                    }
                });*/


               startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

        registrationLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView, new RegisterFragment())
                        .commit();
            }
        });
    }
}
