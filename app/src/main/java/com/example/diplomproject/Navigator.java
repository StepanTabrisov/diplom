package com.example.diplomproject;

import androidx.annotation.DrawableRes;

public interface Navigator {
    void SetIcon(@DrawableRes int resId);
    void DeleteIcon();
    void FindFragmentInStack(String tag);
    void CreateFragment(String tag, String ret_tag, String title, UserData userData);
    UserData getUser();
}
