package Model;

public class PasswordValidation
{
    public  static boolean passwordMatch(String password, String confirmPassword)
    {
        if(password.equals(confirmPassword))
        {
            return true;
        }
        return false;
    }

    public static boolean isValidPassword(String password)
    {
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());

        if((!hasUppercase) || (!hasLowercase))
        {
            return false;
        }
        if(password.length() < 5)
        {
            return false;
        }
        return true;
    }

}
