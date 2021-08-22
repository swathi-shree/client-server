import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class Login {
    static String user;
    static String account = "";
    static String password = "";
    int userFoundInLine;
    static String fileName = "src/users.txt";

    boolean loggedIn = false;
    boolean correctPassword = false;

    private static Scanner x;

    public boolean checkLogin(String inputUser) {
        if (!loggedIn) {

            if(Objects.equals(user, inputUser)){
                loggedIn= false;

            }
        }
        return loggedIn;
        /*
        if(Objects.equals(user, inputUser)){
            loggedIn = true;
        }
        else {
            loggedIn= false;
        }
        return loggedIn;

         */
    }


    public boolean passwordEntered() {
        if(correctPassword) {
            return true;
        }
        return false;
    }

    public boolean alreadyInAccount() {
        if(account == ""){
            return false;
        }
        return true;
    }

    public boolean validPassword(String password) {
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
/*
    public boolean checkAccount(String account) {
        //this.account = account;
        boolean found = false;
        try {


            String tempAccount = "";
            Scanner scr = new Scanner(new File(fileName));
            scr.useDelimiter("[,\n]");


            int count = 0;
            System.out.println("userfound:"+ userFoundInLine);
            while(count<userFoundInLine) {
                System.out.println("record:"+ scr.nextLine());

                count++;
            }
            scr.next();
            tempAccount = scr.next();
            System.out.println("tempAccount:"+tempAccount);
            scr.next();

            if(tempAccount.trim().equals(account.trim())) {
                this.account = account;
                found = true;
            }
            scr.close();

        }catch(Exception e) {
            System.out.println("Error");
        }
        return found;
    }
*/







  /*  public boolean checkUser(String user) {
        boolean found = false;
        boolean guest = false;
        userFoundInLine = 0;
        this.user = "";
        try {
          Scanner scr = new Scanner(new File(fileName));
          scr.useDelimiter("[,\n]");
            while(scr.hasNext() && !found) {
                String username = scr.next();
                if(username.equals(user)) {
                    found = true;
                    if(username.equals("guest")) {
                        guest = true;
                    }
                }
                else {
                    userFoundInLine = userFoundInLine + 1 ;
                }
            }
            if(guest) {
                this.user = "guest";
                loggedIn = true;
            }
            scr.close();

        }catch(Exception e) {
            System.out.println("Error");
        }
        return found;
    }*/

    public boolean checkUser(String user) {
        boolean found = false;
        boolean guest = false;
        userFoundInLine = 0;
        this.user = user;
        try {
            //System.out.println("login:checkuser:try");
            Scanner scr = new Scanner(new File(fileName));
            String userRecord;
            String username;
            String account;
            String password;

           // scr.useDelimiter("[,\n]");
            while(scr.hasNextLine() && !found) {
                //   scr.useDelimiter("[,\n]");
                userRecord= scr.next();

                userFoundInLine = userFoundInLine + 1;

                if (userRecord.equals("guest")&&user.equals("guest")) {
                    guest = true;
                    //loggedIn = true;
                    break;
                }
                else {
                    String [] loginRecord = userRecord.split(",");
                    username = loginRecord[0];
                    account  = loginRecord[1];
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
           // System.out.println("userfoundline:"+ userFoundInLine);

            scr.close();

        }catch(Exception e) {
            System.out.println(e);
        }
        return found;
    }
}
