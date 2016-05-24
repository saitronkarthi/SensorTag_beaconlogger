README

CONTENTS:
---------
1. Android Project
2. Php files
3. Power Point Presentation
4. README file

APPLICATION NAME: 
-----------------
BeaconLogger 


DESCRIPTION:
------------
This application scans for eddystone urls broadcasted by TI sensor tags and logs the required data(RSSI, Device Address, URL and Time Stamp) 
to the AWS Cloud RDS(MySql) database. From the logged data, it produces the User Trajectory for a time period, indicating User Interest (Time Spent near each Tag).
This application also works in background.

AUTHORS:
--------
Karthikeyan Rajamani
Brijesh Dankhara
Chintan Bhat
Saroj Panda


DATE:
-----
05-06-2016


MINIMUM SYSTEM REQUIREMENTS:
----------------------------
1. Android Studio - 1.5.1
2. Android Phone (API 19 and above)
3. MySQLWorkBench (MySql Client)


WEB SERVER REQUIREMENTS:
------------------------
1. Apache Web Server
2. Current Server Credentials
servername = "mydbinstance1.com52r4wudge.us-west-2.rds.amazonaws.com";
username = "karthik";
password = "k12345678";
dbname = "BeaconLog";

COMPILATION
-----------
1. Open the BeaconLogger project in Android Studio
2. Compile and transfer the application to the Android Phone.


HOW TO USE
----------
1. Open The BeaconLogger Application on the Android Phone
2. Click On SCAN FOR SENSORTAG BEACONS button. This logs the data to the database.
3. Click on GENERATE TRAJECTORY Button
4. Enter Start Date, Start Time, End Date and End Time
5. Click Submit

The application will produce the User Trajectory 


