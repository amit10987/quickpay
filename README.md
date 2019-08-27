<b> quickpay </b>
<pre>simple money transfer application 
</pre>
</hr>

<h4> prerequisite </h4>
<pre>
Java version 8
Maven 3
</pre>
</hr>
<h4> Libraries used </h4>
<pre>
Spark-java : to expose rest services.
HikariCP : for connection pooling
Sql2o : for sql query and transaction management
Junit4 and Mockito : for Unit and Functional test cases
Spark-test : for integration test cases
</pre>
</hr>
<h4> How to run the application</h4>
<pre>
mvn exec:java
</pre>
</hr>
<h4> How to run test cases </h4>
<pre>
mvn clean test
</pre>

<h4> Rest service end points </h4>

<h5><pre>1. GET /accounts                      <i>   # get all accounts </i>
2. POST /accounts                     <i>   # create an account </i>
3. GET /accounts/:accountNumber       <i>   # get account by account number </i>
4. POST /transaction/transfer         <i>   # transfer money from one account to another </i>
5. POST /transaction/deposit          <i>   # deposit money </i>
6. POST /transaction/withdraw         <i>   # withdraw money </i></pre></h5>

<h4> Sample Request and Response </h4>
<pre> By defaults application runs on port 4567 and it creates two default accounts to play with </pre>

<pre><b>Get all the accounts: </b>

curl  http://localhost:4567/accounts

<b>Response:</b> [{"balance":500.00,"userName":"Amit","accountNumber":6642159765},{"balance":500.00,"userName":"Anil","accountNumber":6642159766}]
</pre>
<pre>
<b>Create an account:</b>

curl -X POST http://localhost:4567/accounts -d '{"userName" : "John", "openingBalance" : "200"}'

<b>Response:</b> {"accountNumber":6893508747}
</pre>
<pre>
<b>Get account by account number: </b>

curl  http://localhost:4567/accounts/6893508747 

<b>Response:</b> {"balance":200.00,"userName":"John","accountNumber":6893508747}
</pre>
<pre>
<b>Money Transfer: </b>

<b><i>check account details before transfer the money</i></b>
curl http://localhost:4567/accounts
Response: [{<b>"balance":500.00</b>,"userName":"Amit","accountNumber":6642159765},{<b>"balance":500.00</b>,"userName":"Anil","accountNumber":6642159766}]

curl -X POST http://localhost:4567/transaction/transfer -d '{"fromAccountNumber":6642159765, "toAccountNumber":6642159766, "transferAmount": 250}'

<b>Response:</b> {"message":"Money successfully transferred"}

<b><i>check account details after transfer the money</i></b>
curl http://localhost:4567/accounts
Response: [{<b>"balance":250.00</b>,"userName":"Amit","accountNumber":6642159765},{<b>"balance":750.00</b>,"userName":"Anil","accountNumber":6642159766}]
</pre>
<pre>
<b>Money Deposit: </b>

<b><i>check account detail before deposit the money</i></b>
curl http://localhost:4567/accounts/6642159765

