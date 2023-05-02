package SuperLi.src.BusinessLogic;

import java.security.InvalidParameterException;

public class Contact {
    private String name;
    private String phoneNumber;
    private String email;

    private static boolean isEmailValid(String email)
    {
        if(email.length() < 7) //email at least needs to be X@X.com
            return false;
        if(!email.substring(email.length()-4,email.length()).equals(".com")|| !email.contains("@"))
            return false;
        return true;
    }

    private static boolean isPhoneNumberValid(String phone)//private
    {

        if (phone.length() != 10)
            return false;
        if (!phone.substring(0,2).equals("05"))
            return false;
        for (int i=0;i<phone.length();i++)
        {
            if(!Character.isDigit(phone.charAt(i)))
                return false;
        }
        return true;
    }
    public Contact(String name, String phone,String email)
    {
        if(!isPhoneNumberValid(phone))
            throw new InvalidParameterException("phone number is'nt valid");
        if (!isEmailValid(email))
            throw new InvalidParameterException("email address is'nt valid");
        if (!(name.matches("[a-zA-Z]+")))
            throw new InvalidParameterException("Name is not valid");
        this.name = name;
        this.phoneNumber = phone;
        this.email = email;
    }

    public String GetName() {
        return this.name;
    }
    public String GetPhoneNumber() {
        return this.phoneNumber;
    }
    public String GetEmail() {
        return this.email;
    }
    public void SetEmail(String newEmail)
    {
        if(isEmailValid(newEmail))
            this.email = newEmail;
    }

    public boolean equals(Contact other)
    {
        return this.GetPhoneNumber().equals(other.GetPhoneNumber());
    }

    public String toString()
    {
        return "SuperLi.src.BusinessLogic.Contact name: " + name + ", Phone: " + phoneNumber + ", Email: " + email;
    }

}
