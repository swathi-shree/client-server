## COMPSYS725 Assignment 1: SFTP - RFC913

This project implements the Simple File Transfer Protocol described in RFC 913 (https://tools.ietf.org/html/rfc913). This is completed in Java, using Port 1234 as the default.

Author: Swathi Shree
UPI: sshr441    
ID: 623079095

## File Structure
The file structure of this project is as follows:

- cs725
  - src
    - Server.java
    - Client.java
    - Login.java
    - users.txt
  - test
    - kill.txt 
    - kill2.txt
    - toRename.txt
    - rename2.txt
  
## Client Commands
Provided below is the list of commands provided in the RFC913 Protocol.

    <command> : = <cmd> [<SPACE> <args>] <NULL>
    <cmd> : =  USER ! ACCT ! PASS ! TYPE ! LIST ! CDIR
             KILL ! NAME ! DONE ! RETR ! STOR
    <response> : = <response-code> [<message>] <NULL>
    <response-code> : =  + | - |   | !

** RETR and STOR not implemented 

In this implementation, the commands are case sensitive. Therefore, 'USER user1' and 'user user1' will yield different results. To send a command, simply enter the command on the Client console, followed by the ENTER key.

If the cmd sent is not listed above, the server will respond with:
'Unknown option. Please enter a valid command'

Ensure that the cmd is in caps lock (e.g. USER, ACCT)

All commands except USER, ACCT and PASS require the Client to be authenticated. If a command is entered that required authentication and the client is not logged in, the server will respond with:
'Please login to continue.'


## User Details

There are 4 users provided with this project. Their details are:

|  User   |  Acct |  Pass  |   
| ------- |:-----:| ------:|
| guest   |       |        |
| harry   | aut   |  pass  |
| jane    | uoa   |  pass2 |
| bob     | massey | pass3 |

#### guest user provides the easiest login process, it does not require ACCT or PASS 

## Running the Tests

#### NOTE: These tests have been tested on Mac and Ubuntu. Test functionality on either Mac or Ubuntu. 

## Manually interacting between Server and Client
Navigate to cs725/src:

    cd cs725/src

Open two terminals (one for server and one for client)

In Server terminal, compile and run: 
      
    javac Server.java
    java Server

In Client terminal, compile and run:
    
    javac Client.java
    java Client

Now test the commands 

OR use IntelliJ to test by running Server.java then run Client.java
## Commands + Responses

Provided below are the client commands and expected server responses. The format is:

    Client Command
    Reply from Server: Response

Example: 

    USER guest
    Reply from Server :!guest logged in


## Test Cases (To check all the valid commands in one go)

    // LOGIN 
    USER harry --> Reply from Server : +User-id valid, send account and password
    ACCT aut --> Reply from Server : +Account valid, send password
    PASS pass --> Reply from Server : ! Logged in Password is ok and you can begin file transfers.

    // to test TYPE, LIST, CDIR 
    TYPE A --> Reply from Server : +Using Ascii mode
    
    LIST F --> + current connected directory: /Users/swathishree/Desktop/cs725/src
              (preview only, actual result should show 7 results) 
               Client.java
               Login.class 

    LIST V --> + current connected directory: /Users/swathishree/Desktop/cs725/src
              (preview only, actual result should show 7 results) 
              filename: Client.java   filesize: 5672   last write date: 01-09-2021 20:30:04   owner: swathishree

    CDIR .. --> Reply from Server : !Changed working dir to /Users/swathishree/Desktop/cs725

    LIST F test --> + current connected directory: /Users/swathishree/Desktop/cs725/test
                 (preview only, actual result should show 3 results) 
                .DS_Store

    LIST V test --> + current connected directory: /Users/swathishree/Desktop/cs725/test
                    (preview only, actual result should show 3 results) 
                    filename: .DS_Store   filesize: 6148   last write date: 01-09-2021 20:56:40   owner: swathishree

    // to test KILL, NAME,DONE 
    CDIR test --> Reply from Server : !Changed working dir to /Users/swathishree/Desktop/cs725/test

    KILL kill.txt --> Reply from Server : +/Users/swathishree/Desktop/cs725/test/kill.txt deleted

    NAME toRename.txt --> Reply from Server : +File exists, send TOBE

    TOBE newFile.txt --> Reply from Server : +toRename.txt renamed to newFile.txt 

    DONE --> Reply from Server : +closing connection 

## Test Cases


#### USER
Guest login does not require account or password

Command Format:  
USER <user-id>


##### Tests:
    USER guest
    Reply from Server : !guest logged in

If a valid user (harry/jane/bob) logs in : 

    USER harry 
    Reply from Server : +User-id valid, send account and password

    Invalid user-id provided
    USER mark
    Reply from Server : -Invalid user-id, try again


#### ACCT
Command Format:  
ACCT account

##### Tests:
    After entering valid user (i.e. USER harry):
    ACCT aut
    Reply from Server : +Account valid, send password

    Invalid Account Provided:
    ACCT incorrectAcct
    Reply from Server :  -Invalid account, try again

#### PASS
Command Format:  
PASS password

##### Tests:
    If both valid user-id and account have been entered:

    Following (USER harry -> ACCT aut)
    PASS pass
    Reply from Server : !Logged in Password is ok and you can begin file transfers

    If only valid user-id has been provided:

    Following (USER jane)
    PASS pass2
    Reply from Server :  +Password Valid, send account

    Invalid Password Provided:

    PASS red
    Reply from Server : -Wrong password, try again

#### TYPE
Command Format:  
TYPE {A | B | C}

##### Tests:

Requires user to be logged in.

    TYPE A
    Reply from Server: +Using Ascii Mode

    TYPE B
    Reply from Server: +Using Binary Mode

    TYPE C
    Reply from Server: +Using Continous Mode

    TYPE Z
    Reply from Server: -Type not valid

    If user not logged in:
    Reply from Server: Please login to continue
    

#### LIST
Command Format:  
LIST {F | V} directory-path



##### Tests:

The current directory for the first two are : <CURRENT_DIRECTORY> : /Users/swathishree/Desktop/cs725/src

    LIST F
        + current connected directory: /Users/swathishree/Desktop/cs725/src
        Client.java
        Login.class
        Server.class
        Login.java
        Client.class
        users.txt
        Server.java

    LIST V
        + current connected directory: /Users/swathishree/Desktop/cs725/src
        filename: Client.java   filesize: 5672   last write date: 01-09-2021 20:30:04   owner: swathishree
        filename: Login.class   filesize: 1655   last write date: 01-09-2021 19:59:32   owner: swathishree
        filename: Server.class   filesize: 9747   last write date: 01-09-2021 21:43:42   owner: swathishree
        filename: Login.java   filesize: 2520   last write date: 01-09-2021 19:47:09   owner: swathishree
        filename: Client.class   filesize: 3378   last write date: 01-09-2021 21:43:48   owner: swathishree
        filename: users.txt   filesize: 52   last write date: 01-09-2021 15:19:23   owner: swathishree
        filename: Server.java   filesize: 18783   last write date: 01-09-2021 21:43:24   owner: swathishree


Assuming current directory is ../cs725 (e.g. /Users/swathishree/Desktop/cs725/)
If directory is not same as above, change directory by

    CDIR ..
    Reply from Server : !Changed working dir to ../cs725

Now you will be in ../cs725

    LIST F test
        + current connected directory: /Users/swathishree/Desktop/cs725/test
        .DS_Store
        kill.txt
        kill2.txt
        rename2.txt
        toRename.txt

    LIST V src
        + current connected directory: /Users/swathishree/Desktop/cs725/test
        filename: .DS_Store   filesize: 6148   last write date: 01-09-2021 20:56:40   owner: swathishree
        filename: kill.txt   filesize: 7   last write date: 01-09-2021 21:52:07   owner: swathishree
        filename: kill2.txt   filesize: 0   last write date: 01-09-2021 22:36:49   owner: swathishree
        filename: rename2.txt   filesize: 0   last write date: 01-09-2021 22:37:01   owner: swathishree
        filename: toRename.txt   filesize: 8   last write date: 01-09-2021 21:52:07   owner: swathishree


#### CDIR
Command Format:  
CDIR new-directory


##### Tests:
Requires user to be logged in.

    CDIR src
    Reply from Server : !Changed working dir to <CURRENT_DIRECTORY>

    e.g. 
    Reply from Server : !Changed working dir to /Users/swathishree/Desktop/cs725/src
    
    To go back one level 
    CDIR ..
    Reply from Server : !Changed working dir to <CURRENT_DIRECTORY>

    e.g. (goes back one level from src)
    Reply from Server : !Changed working dir to /Users/swathishree/Desktop/cs725

    CDIR non_existent_directory
    Reply from Server : -Can't connect to directory because: directory doesn't exist

    If user-id has been provided but not account/password (immediately after entering USER harry):
    CDIR src
    Reply from Server : +directory ok, send account/password
    Send account and then password to continue (for USER harry -> ACCT aut, PASS pass) 

#### KILL
Command Format:  
KILL file-spec

##### Tests:

This is executed from the "test" folder (CDIR test). Requires user to be logged in. 
To navigate to "test" folder from "src" :
    
    CDIR .. 
    CDIR test

To kill file:

    KILL kill2.txt
    Reply from Server : +<CURRENT_DIRECTORY/kill2.txt> deleted

    e.g.
    Reply from Server : +/Users/swathishree/Desktop/cs725/test/kill2.txt deleted


    KILL random.txt
    Reply from Server : -not deleted because no such file

#### NAME
Command Format:  
NAME old-file-spec

##### Tests:
This is executed from the "test" folder (CDIR test). Requires user to be logged in.
To navigate to "test" folder from "src" :

    CDIR .. 
    CDIR test

To rename:

    NAME rename2.txt
    Reply from Server : +File exists, send TOBE

    TOBE newFile.txt
    Reply from Server : +rename2.txt renamed to newFile.txt

    Try to rename a non existent file
    NAME wrong.txt
    Reply from Server : -Can't find wrong.txt ,NAME command is aborted, don't send TOBE

#### DONE
Command Format:  
DONE

##### Tests:
    DONE
    Reply from Server : +closing connection 
    
