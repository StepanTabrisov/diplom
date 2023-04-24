package com.example.diplomproject;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
//import android.widget.Toolbar;

import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements
         ITitle, ICurrentFragment, Navigator {

    BottomNavigationView bottomNavigation;
    Toolbar toolbarNavigation;
    FileSystemFragment lastFragment;
    FileSystemFragment nextFragment;
    SettingsFragment settingsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        settingsFragment = new SettingsFragment();
        nextFragment = new FileSystemFragment();
        lastFragment = nextFragment;

        toolbarNavigation = findViewById(R.id.nav_toolbar);
        toolbarNavigation.setTitle("Файлы");
        setSupportActionBar(toolbarNavigation);

        bottomNavigation = findViewById(R.id.bottomNavigationView);
        ChangeFragment(nextFragment, "parent");
    }

    @Override
    protected void onStart() {
        super.onStart();

        //навигация снизу
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        ChangeFragment((FileSystemFragment) nextFragment);
                        return true;
                    case R.id.settings:
                        ChangeFragment((SettingsFragment) settingsFragment);
                        return true;
                }
                return false;
            }
        });

        //навигация сверху
        toolbarNavigation.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindFragmentInStack(nextFragment.ret_tag);
                bottomNavigation.setSelectedItemId(R.id.home);
            }
        });
    }

    //поиск фрагмента INavigator
    @Override
    public void FindFragmentInStack(String tag) {
        lastFragment = nextFragment;
        nextFragment = (FileSystemFragment) getSupportFragmentManager().findFragmentByTag(tag);
        ChangeFragment(nextFragment, nextFragment.tag);
    }

    //создание фрагмента INavigator
    @Override
    public void CreateFragment(String tag, String ret_tag, String title) {
        lastFragment = nextFragment;
        nextFragment = new FileSystemFragment(tag, ret_tag, title);
        ChangeFragment(nextFragment, nextFragment.tag);
    }

    //смена фрагмента на экране
    public void ChangeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    /*public void ChangeFragment(FileSystemFragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
    public void ChangeFragment(SettingsFragment fr){
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, fr)
                .addToBackStack(null)
                .commit();
    }*/

    //смена фрагмента на экране
    public void ChangeFragment(FileSystemFragment fragment, String tag){
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    //смена заголовка
    @Override
    public void ChangeTitle(String newTitle) {
        toolbarNavigation.setTitle(newTitle);
    }

    @Override
    public void ChangeFragmentTitle(String newTitle, String tag) {
        nextFragment = (FileSystemFragment) getSupportFragmentManager().findFragmentByTag(tag);
        nextFragment.tempTitle = newTitle;
    }

    //смена фрагмента
    @Override
    public void SetFragment(FileSystemFragment fr) {
        lastFragment = fr;
    }

    //установка иконки назад
    @Override
    public void SetIcon(int resId) {
        toolbarNavigation.setNavigationIcon(resId);
    }

    //удаление иконки назад
    @Override
    public void DeleteIcon() {
        toolbarNavigation.setNavigationIcon(null);
    }

}
