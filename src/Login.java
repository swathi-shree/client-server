import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class Login {
    static String user;
    static String account = "";
    static String password = "";
    int userFoundInLine;
    static String fileName = "src/users.txt";

    boolean userLogged = false;
    boolean correctPassword = false;

    private static Scanner x;

    public boolean checkLogin(String inputUser) {
     //   System.out.println("login:checklogin:inputuser" + inputUser);
        if(inputUser.equals("guest")){
            userLogged = true;
        } else {
            if (!userLogged) {

                if (Objects.equals(user, inputUser)) {
                    userLogged = false;

                }
            }
        }
        return userLogged;
    }


 /*   public boolean passwordEntered() {
        boolean isPassCorrect = false;
        if(correctPassword) {
            isPassCorrect = true;
        }
        return isPassCorrect;
    }
*/
/*    public boolean alreadyInAccount() {
        if(account == ""){
            return false;
        }
        return true;
    } */

  /* public boolean validPassword(String password) {

        boolean found = false;
        try {
            String tempPassword = "";
       //     x = new Scanner(new File(fileName));
       //     x.useDelimiter("[,\n]");
            Scanner scr = new Scanner(new File(fileName));
            scr.useDelimiter("[,\n]");

            int count = 0;
            while(count<userFoundInLine) {
                x.nextLine();
                count++;
            }
            x.next();
            x.next();
            tempPassword = x.next();

            if(tempPassword.trim().equals(password.trim())) {
                correctPassword = true;
                found = true;
            }

            x.close();
        }catch(Exception e) {
            System.out.println("Error");
        }
        return found;
    }

    */


    public boolean checkUser(String user) {
        boolean found = false;
        boolean guest = false;
        userLogged = false;
        userFoundInLine = 0;
        this.user = user;
        try {
            System.out.println("login:checkuser:try");
            Scanner scr = new Scanner(new File(fileName));
            String userRecord;
            String username;
            String account;
            String password;

            while(scr.hasNextLine() && !found) {
           //     System.out.println("login:checkuser:while: ");
                userRecord= scr.next();

                userFoundInLine = userFoundInLine + 1;

                if (userRecord.equals("guest")&&user.equals("guest")) {
              //      System.out.println("login:checkuser:while:if");
                    guest = true;
                    found = true;
                    this.user = user;
                    this.account = "";
                    this.password = "";
                    //loggedIn = true;
                    break;
                }
                else {
                    if (!userRecord.equals("guest")) {
                 //       System.out.println("login:checkuser:elseif: " + userRecord);
                        String[] loginRecord = userRecord.split(",");
                        username = loginRecord[0];
                        account = loginRecord[1];
                        password = loginRecord[2];
                        if (username.equals(user)) {
                            found = true;
                            this.account = account;
                            this.password = password;

                            //loggedIn = true;
                            break;
                        }
                    }
                }
            }
           // System.out.println("userfoundline:"+ userFoundInLine);

            scr.close();

        }catch(Exception e) {
            System.out.println(e);
        }
        return found;
    }
}
