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

        ((ITitle)getActivity()).ChangeTitle("Настройки");
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
