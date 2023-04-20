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
    Fragment lastFragment;
    Fragment tempFragment;
    Fragment settingsFragment;

    boolean open = false;

    public FloatingActionButton add;
    public ExtendedFloatingActionButton addFolder;
    public ExtendedFloatingActionButton addFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        /*if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }*/

        /*add = findViewById(R.id.add_fab);
        addFile = findViewById(R.id.add_file_fab);
        addFolder = findViewById(R.id.add_folder_fab);
        */

        settingsFragment = new SettingsFragment();
        lastFragment = new FileSystemFragment();

        toolbarNavigation = findViewById(R.id.nav_toolbar);
        toolbarNavigation.setTitle("Файлы");
        setSupportActionBar(toolbarNavigation);

        bottomNavigation = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FileSystemFragment()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FileSystemFragment()).commit();
                        return true;
                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                        return true;
                }
                return false;
            }
        });

        toolbarNavigation.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, lastFragment).commit();
                bottomNavigation.setSelectedItemId(R.id.home);
            }
        });
    }


    public Fragment FindFragmentInStack(){

        return new Fragment();
    }

    public void ChangeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FileSystemFragment()).commit();
    }

    //смена заголовка
    @Override
    public void ChangeTitle(String newTitle) {
        toolbarNavigation.setTitle(newTitle);
    }

    //смена фрагмента
    @Override
    public void SetFragment(Fragment fr) {
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
