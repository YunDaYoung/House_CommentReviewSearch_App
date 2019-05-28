package com.example.mainpage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static final String PREF_USER_NAME = "username";
    static final String PREF_USER_MAIL = "usermail";
    static final String PREF_USER_CHECK = "usercheck";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 메일 정보 저장
    public static void setUserMail(Context ctx, String userMail) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_MAIL, userMail);
        editor.commit();
    }

    // 저장된 메일 정보 가져오기
    public static String getUserMail(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_MAIL, "");
    }

    // 이름 정보 저장
    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }
    // 이름 정보 가져오기
    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    // 체크 정보 저장
    public static void setUserCheck(Context ctx, String userCheck) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_CHECK, userCheck);
        editor.commit();
    }
    // 체크 정보 가져오기
    public static String getUserCheck(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_CHECK ,"");
    }
    // 로그아웃

    public static void clearUserName(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }

}