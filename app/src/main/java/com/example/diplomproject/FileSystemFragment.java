package com.example.diplomproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FileSystemFragment extends Fragment implements View.OnClickListener, RecyclerAdapter.OnItemSelectedListener {

    FloatingActionButton add;
    FloatingActionButton addFile;
    FloatingActionButton addFolder;

    public String tag;
    public String ret_tag;
    public String tempTitle;
    public ArrayList<Unit> children = new ArrayList<>();

    FileSystemFragment(){

        tag = "parent";
    }

    public FileSystemFragment(String tag, String ret_tag, String tempTitle){
        this();
        this.tag = tag;
        this.ret_tag = ret_tag;
        this.tempTitle = tempTitle;
    }

    //RecyclerView recyclerView;

    ArrayList <ListElem> list = new ArrayList<ListElem>();
    RecyclerAdapter adapter;

    boolean open = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fs_fragment, container, false);
        /*
        Toolbar nav = getActivity().findViewById(R.id.nav_toolbar);
        nav.setNavigationIcon(null);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(nav);
        activity.getSupportActionBar().setTitle("Файлы");
        */
        add = view.findViewById(R.id.f_add_fab);
        addFile = view.findViewById(R.id.f_add_file_fab);
        addFolder = view.findViewById(R.id.f_add_folder_fab);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setHasFixedSize(true);
        // создаем адаптер
        adapter = new RecyclerAdapter(getContext(), list,  this);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if(tag == "parent"){
            //установка заголовка
            ((ITitle)getActivity()).ChangeTitle("Файлы");
            //удаление иконки назад
            ((Navigator)getActivity()).DeleteIcon();
        }else{
            ((ITitle)getActivity()).ChangeTitle(tempTitle);
            ((Navigator)getActivity()).SetIcon(R.drawable.ic_baseline_arrow_back_24);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        add.setOnClickListener(this);
        addFile.setOnClickListener(this);
        addFolder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.f_add_fab:
                this.ShowButton();
                break;
            case R.id.f_add_file_fab:
                this.AddFileButton();
                break;
            case R.id.f_add_folder_fab:
                this.AddFolderButton();
                break;
        }
    }

    @Override
    public void onItemSelected(int position) {
        String name = list.get(position).name;
        Toast.makeText(getActivity(),"Выбран элемент :" + name + " Индекс: " + position,Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onMenuAction(int position, MenuItem item) {
        String name = list.get(position).name;

        switch (item.getItemId()) {
            case R.id.delete_menu:
                Toast.makeText(getActivity(),"Удалить элемент :" + name + " Индекс: " + position,Toast.LENGTH_SHORT).show();
                break;
            case R.id.rename_menu:
                Toast.makeText(getActivity(),"Переименовать элемент :" + name + " Индекс: " + position,Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onLoadAction(int position) {
        String name = list.get(position).name;
        Toast.makeText(getActivity(),"Скачать элемент :" + name + " Индекс: " + position,Toast.LENGTH_SHORT).show();
    }

    void AddFolderButton(){
        Toast.makeText(getActivity(),"добавить папку",Toast.LENGTH_SHORT).show();

        list.add(new ListElem("Folder", "12kb", 1, R.drawable.folder2));
        adapter.notifyDataSetChanged();

        addFile.setVisibility(View.INVISIBLE);
        addFolder.setVisibility(View.INVISIBLE);
        open = false;
    }

    void AddFileButton(){
        Toast.makeText(getActivity(),"добавить файл",Toast.LENGTH_SHORT).show();

        list.add(new ListElem("File", "12kb", 0, R.drawable.file));
        adapter.notifyDataSetChanged();

        addFile.setVisibility(View.INVISIBLE);
        addFolder.setVisibility(View.INVISIBLE);
        open = false;
    }

    void ShowButton(){
        if(open){
            addFile.setVisibility(View.INVISIBLE);
            addFolder.setVisibility(View.INVISIBLE);
            open = false;
        }
        else {
            addFile.setVisibility(View.VISIBLE);
            addFolder.setVisibility(View.VISIBLE);
            open = true;
        }
    }

}
