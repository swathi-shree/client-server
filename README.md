## COMPSYS725 Assignment 1: SFTP - RFC913

This project implements the Simple File Transfer Protocol described in RFC 913 (https://tools.ietf.org/html/rfc913). This is completed in Java, using Port 115 as the default.

Author: Swathi Shree
UPI: sshr441    
ID: 623079095

## File Structure
The file structure of this project is as follows:

- ass1-sat-src
  - .idea
  - accounts
  - out
  - src
  - Server.java
  - Client.java
  - Login.java
  - 
## Client Commands
Provided below is the list of commands provided in the RFC913 Protocol.

    <command> : = <cmd> [<SPACE> <args>] <NULL>
    <cmd> : =  USER ! ACCT ! PASS ! TYPE ! LIST ! CDIR
             KILL ! NAME ! DONE ! RETR ! STOR
    <response> : = <response-code> [<message>] <NULL>
    <response-code> : =  + | - |   | !

In this implementation, the commands are case sensitive. Therefore, 'USER user1' and 'user user1' will yield different results. To send a command, simply enter the command on the Client console, followed by the ENTER key.

If the cmd sent is not listed above, the server will respond with:
'Unknown option. Please enter a valid command'

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

#### NOTE: Must Run these tests BEFORE manually interacting between the server and client, otherwise the expected output will not match the received output and the tests will fail

#### NOTE-2: These tests have been tested on Mac and Linux. Test functionality on either Mac or Linux. 

## Manually interacting between Server and Client


## Test Cases


#### USER
Guest login does not require account or password

Command Format:  
USER <user-id>


##### Tests:
    USER guest
        !guest logged in

If a valid user (harry/jane/bob) logs in : 

    USER harry 
        +User-id valid, send account and password

    Invalid user-id provided
    USER mark
        -Invalid user-id, try again


#### ACCT
Command Format:  
ACCT account

##### Tests:
    After entering valid user (i.e. USER harry) and account
    ACCT aut
        +Account valid, send password

    Invalid Account Provided
    ACCT incorrectAcct
        -Invalid account, try again

#### PASS
Command Format:  
PASS password

##### Tests:
    If both valid user-id and account have been entered:
    Following (USER harry -> ACCT aut)
    PASS pass
        !Logged in Password is ok and you can begin file transfers

    If only valid user-id has been provided
    Following (USER jane)
    PASS pass2
        +Password Valid, send account

    Invalid Password Provided
    PASS red
        -Wrong password, try again

#### TYPE
Command Format:  
TYPE {A | B | C}

##### Tests:
    TYPE A
        +Using Ascii Mode

    TYPE B
        +Using Binary Mode

    TYPE C
        +Using Continous Mode

    TYPE Z
        -Type not valid

#### LIST
Command Format:  
LIST {F | V} directory-path

<CURRENT_DIRECTORY> : /Users/swathishree/Desktop/ass1-sat

##### Tests:
    LIST F
        +<CURRENT_DIRECTORY>
        list-of-files

    LIST F src
        +<CURRENT_DIRECTORY\src>
        Client.java
        Login.java
        users.txt
        Server.java

    LIST V
        +<CURRENT_DIRECTORY>
        list-of-files   Size:   LastModified:
