package com.example.diplomproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
