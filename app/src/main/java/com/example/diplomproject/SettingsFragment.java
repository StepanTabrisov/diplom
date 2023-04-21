package com.example.diplomproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        /*Toolbar nav = getActivity().findViewById(R.id.nav_toolbar);
        nav.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        //nav.setTitle("Настройки");
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(nav);*/

        ((ITitle)getActivity()).ChangeTitle("Настройки");

        //activity.getSupportActionBar().setTitle("Настройки");
        //activity.getSupportActionBar().setIcon(R.drawable.ic_baseline_arrow_back_24);

        /*nav.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragment_container, new FileSystemFragment()).commit();


            }
        });*/
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //((ICurrentFragment)getActivity()).SetFragment(this);

        //((Navigator)getActivity()).SetIcon(R.drawable.ic_baseline_arrow_back_24);
        ((Navigator)getActivity()).DeleteIcon();
    }
}
