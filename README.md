### Instructions
This server uses database (MySQL as a native decision). The connection string and database account data are not safe to store in 
code so it's moved out into `properties.xml`.

Template for `properties.xml`:

```
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
    <comment>application.properties</comment>
    <entry key="db.url">jdbc:mysql://localhost:port/database_name</entry>
    <entry key="db.username">db_username</entry>
    <entry key="db.password">db_password</entry>
    <entry key="db.driver">com.mysql.jdbc.Driver</entry>
</properties>
```

This file is stored in the root of the app. If you use Apache Tomcat (as I do), then move it to
`tomcat_installation_folder/bin/`.
