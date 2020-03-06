# Salem ELA Library Management System
Project for someone I know. A Maven webapp, since i figured going with Java would be the fastest thing for me.

# Built with
Java SDK 8
Maven 3.3.9

# Running with
IntelliJ IDEA configurations. Equivalents (untested) should be:
```
mvn -X -e clean compile package
mvn exec:java
```

## Goal
Student types ID and then scans QRCode with their mobile app to check out.

To check in, scan the QRCode of the book again.

Teacher should have a simple time adding books & QR codes. Master list of library books will be downloaded from S3 as a .csv

---------------

Moved to Spring Boot because the Spark REST library had bad (read as next to no) documentation on enabling SSL and I didn't want to go looking for another solution. Sure I could have tried to figure something out with Tomcat or Jetty but I knew Spring Boot to some degree already. Replacing Guice was a minor cost for a well-supported and well-documented solution with a community.
