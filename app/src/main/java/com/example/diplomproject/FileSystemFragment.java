package com.example.diplomproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.util.Log;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HTTP;

public class FileSystemFragment extends Fragment
        implements View.OnClickListener, RecyclerAdapter.OnItemSelectedListener {

    private FloatingActionButton add;
    private FloatingActionButton addFile;
    private FloatingActionButton addFolder;

    public String tag;
    public String ret_tag;
    public String tempTitle;

    public ArrayList<ListElem> list = new ArrayList<>();
    public ArrayList<Unit> children = new ArrayList<>();
    public RecyclerAdapter adapter;

    private boolean open = false;
    private boolean changed = false;
    public UserData userData;

    FileSystemFragment(){
        super(R.layout.parent_fs_fragment);
        tag = "parent";
        ret_tag = "";
        tempTitle = "Файлы";
        //userData = requireActivity().getIntent().getExtras().getParcelable(UserData.class.getSimpleName());
        //InitList(tempTitle);
    }

    FileSystemFragment(UserData userData){
        super(R.layout.parent_fs_fragment);
        tag = "parent";
        ret_tag = "";
        tempTitle = "Файлы";
        this.userData = userData;
        InitList(tempTitle);
    }

    public FileSystemFragment(String tag, String ret_tag, String tempTitle, UserData userData){
        this.tag = tag;
        this.ret_tag = ret_tag;
        this.tempTitle = tempTitle;
        this.userData = userData;
        InitList(tempTitle);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fs_fragment, container, false);
        add = view.findViewById(R.id.f_add_fab);
        addFile = view.findViewById(R.id.f_add_file_fab);
        addFolder = view.findViewById(R.id.f_add_folder_fab);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerAdapter(getContext(), list,  this); //Создаем и устанавливаем адаптер
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Objects.equals(tag, "parent")){
            Objects.requireNonNull((ITitle)getActivity()).ChangeTitle("Файлы"); //установка заголовка
            ((Navigator)getActivity()).DeleteIcon();                                    //удаление иконки назад
        }else{
            Objects.requireNonNull((ITitle)getActivity()).ChangeTitle(tempTitle);
            ((Navigator)getActivity()).SetIcon(R.drawable.ic_baseline_arrow_back_24);
        }
        add.setOnClickListener(this);
        addFile.setOnClickListener(this);
        addFolder.setOnClickListener(this);
    }

    // обработка нажатий на кнопки
    @SuppressLint("NonConstantResourceId")
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
        ListElem curr = list.get(position);
        if(curr.type == 1){
            Objects.requireNonNull((ICurrentFragment)getActivity()).SetFragment(this);
            if(children.get(position).created){
                ((Navigator)getActivity()).FindFragmentInStack(children.get(position).tag);
            }else{
                ((Navigator)getActivity()).CreateFragment(children.get(position).tag, children.get(position).ret_tag, curr.name, userData);
                children.get(position).created = true;
            }
        }
    }

    //обработка нажатий на меню элемента
    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    public void onMenuAction(int position, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_menu:
                if(list.get(position).type == 1){
                    children.remove(position);
                }
                list.remove(position);
                adapter.notifyDataSetChanged();
                break;
            case R.id.rename_menu:
                RenameCustomDialog(position);
                break;
        }
    }

    //обработка нажатия на кнопку скачать
    @Override
    public void onLoadAction(int position) {
        String name = list.get(position).name;
        downloadFile(name);
    }

    public void downloadFile(String filename){
        RetrofitService retrofitService  = new RetrofitService();
        NetworkApi networkApi = retrofitService.getRetrofit().create(NetworkApi.class);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        networkApi.downloadFile(userData.GetLogin(), filename).enqueue(new Callback<ResponseBody>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(saveFile(filename, Objects.requireNonNull(response.body()))){
                            new Handler(Looper.getMainLooper()).post(() -> {
                                Toast.makeText(getActivity(), "Файл загружен", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public boolean saveFile(String filename, ResponseBody body){
        File newFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            byte[] fileReader = new byte[4096];
            inputStream = body.byteStream();
            outputStream = new FileOutputStream(newFile);

            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) break;
                outputStream.write(fileReader, 0, read);
            }
            outputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            try{
                inputStream.close();
                if (outputStream != null) {
                    outputStream.close();
                }
                return false;
            }catch (IOException a){
                e.printStackTrace();
                return false;
            }
        }
    }

    //добавление папки
    public void AddFolderButton(){
        AddFolderCustomDialog();                //диалог для ввода названия папки
        addFile.setVisibility(View.INVISIBLE);
        addFolder.setVisibility(View.INVISIBLE);
        open = false;
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri uri = Objects.requireNonNull(result.getData()).getData();

                        Cursor returnCursor = requireContext().getContentResolver().query(uri, null, null, null, null);
                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                        returnCursor.moveToFirst();

                        String name = returnCursor.getString(nameIndex);
                        long size = returnCursor.getLong(sizeIndex);
                        int resourceImage = GetImageRes(name);

                        UploadFile(uri, name);

                        list.add(new ListElem(name, ListElem.getReadableFileSize(size), 0, resourceImage));
                        list.sort(new SortListItems());
                        adapter.notifyDataSetChanged();
                        returnCursor.close();
                        changed = true;
                    }
                }
            });

    private void UploadFile(Uri uri, String name) {
        File file = new File(RealPathUtil.getRealPath(requireContext(), uri));

        RetrofitService retrofitService  = new RetrofitService();
        NetworkApi networkApi = retrofitService.getRetrofit().create(NetworkApi.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(uri)), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", name, requestFile);

        networkApi.uploadFile(userData.GetLogin(), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.code() == 200){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(getActivity(), "Файл отправлен!", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //добавление файла
    void AddFileButton(){
        Toast.makeText(getActivity(),"добавить файл",Toast.LENGTH_SHORT).show();
        ChangeUI(View.INVISIBLE, false);
        Intent myFiles = new Intent(Intent.ACTION_GET_CONTENT);
        myFiles.setType("*/*");
        myFiles.addCategory(Intent.CATEGORY_OPENABLE);
        mStartForResult.launch(myFiles);
    }

    // показ кнопок
    void ShowButton(){
        if(open)
            ChangeUI(View.INVISIBLE, false);
        else
            ChangeUI(View.VISIBLE, true);
    }

    private void ChangeUI(int invisible, boolean b) {
        addFile.setVisibility(invisible);
        addFolder.setVisibility(invisible);
        open = b;
    }

    // диалог для добавления папки
    @SuppressLint("NotifyDataSetChanged")
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
            list.add(new ListElem(name_folder, "12kb", 1, R.drawable.folder_icon2)); // добавление папки в список
            list.sort(new SortListItems());
            adapter.notifyDataSetChanged();
            children.add(new Unit(UUID.randomUUID().toString(), tag, false));
            changed = true;
            dialog.cancel();
        });
        cancel.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }

    // диалог
    @SuppressLint("NotifyDataSetChanged")
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
            list.get(position).name = name_folder;  // изменение названия
            adapter.notifyDataSetChanged();
            if(children.get(position).created){
                Objects.requireNonNull((ITitle)getActivity()).ChangeFragmentTitle(name_folder, children.get(position).tag);
            }
            dialog.cancel();
        });
        cancel.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }

    // получение иконки
    int GetImageRes(String name){
        if (name.endsWith("png")) return R.drawable.png_icon;
        if (name.endsWith("doc")) return R.drawable.doc_icon;
        if (name.endsWith("mp3")) return R.drawable.mp3_icon;
        if (name.endsWith("mp4")) return R.drawable.mp4_icon;
        if (name.endsWith("pdf")) return R.drawable.pdf_icon;
        if (name.endsWith("ppt")) return R.drawable.ppt_icon;
        if (name.endsWith("txt")) return R.drawable.txt_icon;
        if (name.endsWith("xls")) return R.drawable.xls_icon;
        return 0;
    }

    void InitList(String title){
        RetrofitService retrofitService  = new RetrofitService();
        NetworkApi networkApi = retrofitService.getRetrofit().create(NetworkApi.class);
        networkApi.GetDataList(userData.GetLogin(), title).enqueue(new Callback<Fields>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<Fields> call, Response<Fields> response) {
                    if(response.body()!=null){
                        list.addAll(response.body().list);
                        adapter.notifyDataSetChanged();
                        IntStream.range(0, list.size()).forEach(i -> children.add(new Unit(UUID.randomUUID().toString(), tag, false)));
                    }
                }
                @Override
                public void onFailure(Call<Fields> call, Throwable t) {
                    t.printStackTrace();
                }
            });
    }

    @Override
    public void onStop() {
        super.onStop();
        if(changed){
            changed = false;
            Fields a = new Fields();
            a.tempTitle = tempTitle;
            a.list = list;
            RetrofitService retrofitService  = new RetrofitService();
            NetworkApi networkApi = retrofitService.getRetrofit().create(NetworkApi.class);
            networkApi.SaveDataList(userData.GetLogin(), a).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    System.out.println(response.body());
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}
