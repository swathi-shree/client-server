import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class Login {
    static String user;
    static String account = "";
    static String password = "";
    int userInLine;
    static String fileName = "users.txt";

    boolean userLogged = false;

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

    public boolean checkUser(String user) {
     //   boolean userFound = false;
        boolean userFound = false;
        userLogged = false;
        userInLine = 0;
        this.user = user;
        try {
         //   System.out.println("login:checkuser:try");
            Scanner scr = new Scanner(new File(fileName));
            String userRecord;
            String username;
            String account;
            String password;

            while(scr.hasNextLine() && !userFound) {
           //     System.out.println("login:checkuser:while: ");
                userRecord= scr.next();
                userInLine = userInLine + 1;

                if (userRecord.equals("guest")&&user.equals("guest")) {
              //      System.out.println("login:checkuser:while:if");
                    userFound = true;
                    this.user = user;
                    this.account = "";
                    this.password = "";
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
                            userFound = true;
                            this.account = account;
                            this.password = password;
                            break;
                        }
                    }
                }
            }
            scr.close();

        }catch(Exception e) {
            System.out.println(e);
        }
        return userFound;
    }
}
