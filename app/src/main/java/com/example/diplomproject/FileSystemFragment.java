package com.example.diplomproject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class FileSystemFragment extends Fragment implements View.OnClickListener, RecyclerAdapter.OnItemSelectedListener {

    FloatingActionButton add;
    FloatingActionButton addFile;
    FloatingActionButton addFolder;

    public String tag;
    public String ret_tag;
    public String tempTitle;

    ArrayList <ListElem> list = new ArrayList<ListElem>();
    RecyclerAdapter adapter;
    boolean open = false;
    public ArrayList<Unit> children = new ArrayList<>();

    FileSystemFragment(){
        super(R.layout.parent_fs_fragment);
        tag = "parent";
        ret_tag = "";
        tempTitle = "Файлы";
    }

    public FileSystemFragment(String tag, String ret_tag, String tempTitle){
        this();
        this.tag = tag;
        this.ret_tag = ret_tag;
        this.tempTitle = tempTitle;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fs_fragment, container, false);

        //add= getActivity().findViewById(R.id.add_fab);
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

        if(tag == "parent"){
            //установка заголовка
            ((ITitle)getActivity()).ChangeTitle("Файлы");
            //удаление иконки назад
            ((Navigator)getActivity()).DeleteIcon();
        }else{
            ((ITitle)getActivity()).ChangeTitle(tempTitle);
            ((Navigator)getActivity()).SetIcon(R.drawable.ic_baseline_arrow_back_24);
        }
    }

    // обработка нажатий на кнопки
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

    //обработка нажатия на элемент списка
    @Override
    public void onItemSelected(int position) {
        String name = list.get(position).name;
        Toast.makeText(getActivity(),"Выбран элемент :" + name + " Индекс: " + position,Toast.LENGTH_SHORT).show();

        ListElem curr = list.get(position);
        if(curr.type == 1){
            ((ICurrentFragment)getActivity()).SetFragment(this);
            if(children.get(position).created){
                ((Navigator)getActivity()).FindFragmentInStack(children.get(position).tag);
            }else{
                ((Navigator)getActivity()).CreateFragment(children.get(position).tag, children.get(position).ret_tag, curr.name);
                children.get(position).created = true;
            }
        }
    }

    //обработка нажатий на меню элемента
    @Override
    public void onMenuAction(int position, MenuItem item) {
        String name = list.get(position).name;
        switch (item.getItemId()) {
            case R.id.delete_menu:
                Toast.makeText(getActivity(),"Удалить элемент :" + name + " Индекс: " + position,Toast.LENGTH_SHORT).show();

                if(list.get(position).type == 1){
                    list.remove(position);
                    children.remove(position);
                }else{
                    list.remove(position);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.rename_menu:
                Toast.makeText(getActivity(),"Переименовать элемент :" + name + " Индекс: " + position,Toast.LENGTH_SHORT).show();
                RenameCustomDialog(position);
                break;
        }
    }

    //обработка нажатия на кнопку скачать
    @Override
    public void onLoadAction(int position) {
        String name = list.get(position).name;
        Toast.makeText(getActivity(),"Скачать элемент :" + name + " Индекс: " + position,Toast.LENGTH_SHORT).show();
    }

    //добавление папки
    public void AddFolderButton(){
        //диалог для ввода названия папки
        AddFolderCustomDialog();
        Toast.makeText(getActivity(),"добавить папку",Toast.LENGTH_SHORT).show();
        addFile.setVisibility(View.INVISIBLE);
        addFolder.setVisibility(View.INVISIBLE);
        open = false;
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //Intent intent = result.getData();
                        // Handle the Intent

                        Uri uri = result.getData().getData();
                        Cursor returnCursor = getContext().getContentResolver().query(uri, null, null, null, null);
                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                        returnCursor.moveToFirst();
                        long size = returnCursor.getLong(sizeIndex);
                        String name = returnCursor.getString(nameIndex);

                        System.out.println(name);
                        System.out.println(uri);
                        int resourceImage = GetImageRes(name);

                        list.add(new ListElem(name, ListElem.getReadableFileSize(size), 0, resourceImage));
                        Collections.sort(list, new SortListItems());
                        adapter.notifyDataSetChanged();
                    }
                }
            });


    //добавление файла
    void AddFileButton(){
        Toast.makeText(getActivity(),"добавить файл",Toast.LENGTH_SHORT).show();

        Intent myFiles = new Intent(Intent.ACTION_GET_CONTENT);
        myFiles.setType("*/*");
        mStartForResult.launch(myFiles);

        addFile.setVisibility(View.INVISIBLE);
        addFolder.setVisibility(View.INVISIBLE);
        open = false;
    }

    // показ кнопок
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

    // диалог для добавления папки
    void AddFolderCustomDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button d_add_folder = dialog.findViewById(R.id.dialog_add_button);
        Button cancel = dialog.findViewById(R.id.dialog_cancel_button);
        EditText nameFolder = dialog.findViewById(R.id.folder_name);
        TextView title = dialog.findViewById(R.id.dialog_text);

        title.setText("Новая папка");

        d_add_folder.setOnClickListener(v -> {
            String name_folder = nameFolder.getText().toString();
            // добавление папки в список
            list.add(new ListElem(name_folder, "12kb", 1, R.drawable.folder_icon2));
            Collections.sort(list, new SortListItems());
            adapter.notifyDataSetChanged();
            children.add(new Unit(UUID.randomUUID().toString(), tag, false));
            dialog.cancel();
        });
        cancel.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }

    // диалог
    void RenameCustomDialog(int position) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button d_add_folder = dialog.findViewById(R.id.dialog_add_button);
        Button cancel = dialog.findViewById(R.id.dialog_cancel_button);
        EditText nameFolder = dialog.findViewById(R.id.folder_name);
        TextView title = dialog.findViewById(R.id.dialog_text);

        title.setText("Переименовать");

        d_add_folder.setOnClickListener(v -> {
            String name_folder = nameFolder.getText().toString();
            // изменение названия
            list.get(position).name = name_folder;
            adapter.notifyDataSetChanged();
            if(children.get(position).created){
                ((ITitle)getActivity()).ChangeFragmentTitle(name_folder, children.get(position).tag);
            }
            dialog.cancel();
        });
        cancel.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }

    // получение иконки
    int GetImageRes(String name){
        int resImage = 0;
        if (name.endsWith("png")) {
            System.out.println("png image");
            resImage = R.drawable.png_icon;
        }
        if(name.endsWith("doc")){
            System.out.println("doc image");
            resImage = R.drawable.doc_icon;
        }
        if(name.endsWith("mp3")){
            System.out.println("mp3 image");
            resImage = R.drawable.mp3_icon;
        }
        if(name.endsWith("mp4")){
            System.out.println("mp4 image");
            resImage = R.drawable.mp4_icon;
        }
        if(name.endsWith("pdf")){
            System.out.println("pdf image");
            resImage = R.drawable.pdf_icon;
        }
        if(name.endsWith("ppt")){
            System.out.println("ppt image");
            resImage = R.drawable.ppt_icon;
        }
        if(name.endsWith("txt")){
            System.out.println("txt image");
            resImage = R.drawable.txt_icon;
        }
        if(name.endsWith("xls")){
            System.out.println("xls image");
            resImage = R.drawable.xls_icon;
        }
        return  resImage;
    }
}
