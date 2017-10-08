# Repository
This repository is the result of a bachelor degree project. The goal of the project was to implement and test different schemaless ways of using a relational database. Docker were used to make two containers, one for MySQL and one for the benchmarking tool YCSB. This repo contains all the nessesary files to reproduce the performance test done in…

# ycsb-test-bed
Docker files and scripts for performance testing some schemaless storage methods on MySQL

## ycsb folder

### buildrun:
Builds and run the container specified by the first argument.
It also stops and removes any running containers with the same name.
The name must be the same as one of the ycsb folders in this map.
ex. ./buildrun ycsb_key-value

### testScript:
Makes the specified container initialize the database and run a specified workload.
First argument should be the container name and the second should be the workload to run.
ex. ./testScript ycsb_osc workloadb
To run this script the specified ycsb container must be running.
Furthermore a mysql container by the name "mysql", and with the properties specified in 
the db.properties file must also be running.

### Dockerfile:
Specifies how the ycsb container should be built.

### workloads:
Map that contains the different workloads that can be run

### db.properties:
Configurations for ycsb.

### ycsb_*:
Contains the two files changed to make the ycsb-jdbc module behave acording to 
the different storage methods that is to be tested.

### ycsb_*/DefaultDBFlavor.java:
Defines the SQL for read, insert, update, delete and scan operations i YCSB.
Changes has been made to all Create*Statement methods.
The methods return a jdbc prepared statement that is later used by the methods
defined in JdbcDBCliend.java file

### ycsb_*/JdbcDBClient.java:
Inherets from YCSB DB class and among other things implements the actual
read, insert, delete, scan and update methods that YCSB will use.
Changes has been made to all of the mentioned methods to correspond to
the usage of the storage method to be tested.

## mysql folder

### buildrun:
Builds and run the mysql container specified by the first argument.
Stops and removes any already running mysql containers.
ex. ./buildrun mysql_jdbc

### mysql_*/Dockerfile
Specifies how the mysql container should be built.
Extends the official mysql image.

 
