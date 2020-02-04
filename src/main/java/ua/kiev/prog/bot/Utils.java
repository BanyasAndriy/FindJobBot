package ua.kiev.prog.bot;

import org.apache.commons.validator.EmailValidator;

public class Utils {
    public static boolean isValidEmailAddress(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
    public static boolean isValidName(String name) {

        if (name.length()<2){
            return false;
        }

        boolean hasNumberInName = !name.matches(".*\\d.*");


        return hasNumberInName;
    }


}
