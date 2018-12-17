package Model;

public class KeyManager
{
    public static int getFlightNumber(String input)
    {
        String temp = null;
        int ret = 0;
        temp = input.replaceAll("(C)","");
        temp = temp.replaceAll("(A)","");

        if(temp.length() != 3)
        {
            return -1;
        }
        else
        {
            ret = Integer.parseInt(temp);
            ret = ret - 805;
        }
        return ret;
    }

    public static String convertFlightNum(int number)
    {
        String temp = "CA" + String.valueOf(805 + number);
        return temp;
    }

    public static String generateConfirmationNumber(int confNum)
    {
        String temp = null;
        int num = confNum + 95032;
        temp = "CLR" + String.valueOf(num);
        return temp;
    }
    public static int decryptConfirmationNumber(String number)
    {
        String temp;
        int ret = 0;
        temp = number.replaceAll("(CLR)","");
        if(temp.length() != 5)
        {
            return 0;
        }
        ret = Integer.parseInt(temp);
        ret = ret -95032;
        System.out.println(ret);
        return ret;
    }

}
