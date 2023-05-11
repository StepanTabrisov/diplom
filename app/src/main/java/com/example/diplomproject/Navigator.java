package com.example.diplomproject;

import androidx.annotation.DrawableRes;

public interface Navigator {
    public void SetIcon(@DrawableRes int resId);
    public void DeleteIcon();
    public void FindFragmentInStack(String tag);
    public void CreateFragment(String tag, String ret_tag, String title);
}
