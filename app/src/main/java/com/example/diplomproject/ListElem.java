package com.example.diplomproject;

public class ListElem {

    public String name;         //имя файла/папки
    public String size;         // размер файла
    public int imageResource;   // ресурс изображения
    public int type;            // тип 0 - файл, 1 - папка

    public ListElem(String name, String size, int type, int imageResource){
        this.name = name;
        this.size = size;
        this.type = type;
        this.imageResource = imageResource;
    }

}
