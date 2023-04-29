package com.example.diplomproject;

import java.text.DecimalFormat;

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

    public static String getReadableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
