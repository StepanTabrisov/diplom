package com.example.diplomproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements
         ITitle, ICurrentFragment, Navigator {

    private BottomNavigationView bottomNavigation;
    private Toolbar toolbarNavigation;
    private FileSystemFragment lastFragment;
    private FileSystemFragment nextFragment;
    private SettingsFragment settingsFragment;

    public UserData userData = new UserData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Bundle arg = getIntent().getExtras();
        if(arg!=null){
            userData = arg.getParcelable(UserData.class.getSimpleName());
        }

        settingsFragment = new SettingsFragment();
        nextFragment = new FileSystemFragment(userData);
        lastFragment = nextFragment;

        toolbarNavigation = findViewById(R.id.nav_toolbar);
        toolbarNavigation.setTitle("Файлы");
        setSupportActionBar(toolbarNavigation);

        bottomNavigation = findViewById(R.id.bottomNavigationView);
        ChangeFragment(nextFragment, "parent");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onStart() {
        super.onStart();

        //навигация снизу
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    ChangeFragment(nextFragment);
                    return true;
                case R.id.settings:
                    ChangeFragment(settingsFragment);
                    return true;
            }
            return false;
        });

        //навигация сверху
        toolbarNavigation.setNavigationOnClickListener(v -> {
            FindFragmentInStack(nextFragment.ret_tag);
            bottomNavigation.setSelectedItemId(R.id.home);
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
    public void CreateFragment(String tag, String ret_tag, String title, UserData userData) {
        lastFragment = nextFragment;
        nextFragment = new FileSystemFragment(tag, ret_tag, title, userData);
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
