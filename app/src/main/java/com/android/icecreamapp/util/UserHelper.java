package com.android.icecreamapp.util;


import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserHelper {

    public static final String NAME = "Name";

    public static final String EMAIL = "Email";

    public static final String BIRTHDAY = "Birthday";

    public static final String PHONE_NUMBER = "Phone Number";

    public static final String HOBBY_INTEREST = "Hobby & Interest";

    public static final int SELECT_PICTURE_AVATAR = 2000;

    public static final int SELECT_PICTURE_COVER = 2100;

    public static boolean isValidEmail(String email){
            String emailPattern = "\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b";
            Pattern p = Pattern.compile(emailPattern);
            Matcher m = p.matcher(email);
            return m.matches();
    }

    public static void displayMessageToast(Context context, String displayMessage){
        Toast.makeText(context, displayMessage, Toast.LENGTH_LONG).show();
    }
}
