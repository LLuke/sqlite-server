# SQLite Server
A [SQLite](https://www.sqlite.org/index.html) server based on the client/server architecture and org.xerial [sqlite-jdbc](https://github.com/xerial/sqlite-jdbc) project
1) Implement a subset(PgServer) of [postgreSQL c/s protocol](https://www.postgresql.org/docs/8.2/protocol.html) for supporting [pgjdbc](https://github.com/pgjdbc/pgjdbc) , psql, or ODBC
2) Support md5(default) and password authentication method in PgServer
3) High performance(insert 30,000 ~ 50,000+ rows per second in [wal & normal](https://www.sqlite.org/pragma.html#pragma_journal_mode) mode)

# Examples
1) Standalone SQLite server
Console 1
```shell
$java -Xmx128m org.sqlite.server.SQLiteServer -p 123456
2019-09-03 20:30:16.703 [SQLite server 0.3.27] INFO  SQLiteServer - Ready for connections on localhost:3272
```
Console 2
```shell
$psql -U root -p 3272 test.db
The user root's password:
psql (11.0, Server 8.2.23)
Input "help" for help information.

test.db=> \timing on
Timing on
test.db=> select count(*) from accounts;
 count(*)
----------
 32011001
(Rows 1)


Time: 338.081 ms
test.db=>
```

2) Embedded SQLite server
```java
SQLiteServer server = new SQLiteServer();
server.bootAsync("-p", "123456");
```
