package com.mikhailzaitsevfls.locateme.registrationActivity;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class Validate {

    private Validate() {
    }

    private static final Pattern EMAIL_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9+._%\\-]{1,60}@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})");

    private static final Pattern PASSWORD_PATTERN1 = Pattern.compile(".{6,30}"); //at least 6 characters and max 30
    private static final Pattern PASSWORD_PATTERN2 = Pattern.compile("\\w*[A-Z]+\\w*"); //at least 1 upper case letter
    private static final Pattern PASSWORD_PATTERN3 = Pattern.compile("\\w*[0-9]+\\w*"); //at least 1 digit
    private static final Pattern PASSWORD_PATTERN4 = Pattern.compile("\\w*[a-z]+\\w*"); //at least 1 lower case letter
    private static final Pattern PASSWORD_PATTERN5 = Pattern.compile("\\w*(\\s)+\\w*"); //white spaces
    private static final Pattern PASSWORD_PATTERN6 = Pattern.compile("[A-Za-z0-9_]+");// a-z_A-Z_0-9_


    public static String validateEmailAndPassword(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            return "Fields can't be empty";

        } else if (!EMAIL_ADDRESS.matcher(email).matches()) {
            return "Please enter a valid email address";

        } else if (!PASSWORD_PATTERN1.matcher(password).matches()) {
            return "Password should consist of 6 to 30 characters";

        } else if (!PASSWORD_PATTERN3.matcher(password).matches()) {
            return "Password should consist of at least 1 digit";

        } else if (!PASSWORD_PATTERN2.matcher(password).matches()) {
            return "Password should consist of at least 1 upper case letter";

        } else if (!PASSWORD_PATTERN4.matcher(password).matches()) {
            return "Password should consist of at least 1 lower case letter";

        } else if (PASSWORD_PATTERN5.matcher(password).matches()) {
            return "Password shouldn't consist of any white spaces";

        }else if (!PASSWORD_PATTERN6.matcher(password).matches()) {
            return "Password should consist of a-z, A-Z, 0-9, \"_\"";

        } else {
            return null;
        }
    }

    private static final Pattern NAME_PATTERN1 = Pattern.compile(".{4,30}"); //at least 4 characters max 30 characters

    public static String validateNameEmailPasswordSecondPassword(String name, String email, String password, String secondPassword) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(secondPassword)) {
            return "Fields can't be empty";

        } else if (!password.equals(secondPassword)) {
            return "Passwords are different";

        } else if (!NAME_PATTERN1.matcher(name).matches()) {
            return "Name should consist of at least 4 to 30 characters";

        } else if (!PASSWORD_PATTERN6.matcher(name).matches()) {
            return "Name should consist of a-z, A-Z, 0-9, \"_\"";

        } else if (PASSWORD_PATTERN5.matcher(name).matches()) {
            return "Name shouldn't consist of any white spaces";

        }else return validateEmailAndPassword(email, password);

    }
}
