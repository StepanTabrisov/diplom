package com.example.diplomproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

    }
}
