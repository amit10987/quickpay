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
mvn clean install exec:java
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
<pre>By defaults application runs on port 4567 and it creates two default accounts to play with </pre>

<pre><b>1. Get all the accounts: </b>

<b><i>curl  http://localhost:4567/accounts</i></b>

<b>Response:</b> [{"balance":500.00,"userName":"Amit","accountNumber":6642159765},{"balance":500.00,"userName":"Anil","accountNumber":6642159766}]
</pre>
<pre>
<b>2. Create an account:</b>

<b><i>curl -X POST http://localhost:4567/accounts -d '{"userName" : "John", "openingBalance" : 200}'</i></b>

<b>Response:</b> {"accountNumber":6893508747}
</pre>
<pre>
<b>3. Get account by account number: </b>

<b><i>curl  http://localhost:4567/accounts/6642159765 </i></b>

<b>Response:</b> {"balance":500.00,"userName":"Amit","accountNumber":6642159765}
</pre>
<pre>
<b>4. Money Transfer: </b>

<b>check account details before transfer the money</b>
curl http://localhost:4567/accounts
Response: [{<b>"balance":500.00</b>,"userName":"Amit","accountNumber":6642159765},{<b>"balance":500.00</b>,"userName":"Anil","accountNumber":6642159766}]

<b><i>curl -X POST http://localhost:4567/transaction/transfer -d '{"fromAccountNumber":6642159765, "toAccountNumber":6642159766, "transferAmount": 250}'</i></b>

<b>Response:</b> {"message":"Money successfully transferred"}

<b>check account details after transfer the money</b>
curl http://localhost:4567/accounts
Response: [{<b>"balance":250.00</b>,"userName":"Amit","accountNumber":6642159765},{<b>"balance":750.00</b>,"userName":"Anil","accountNumber":6642159766}]
</pre>
<pre>
<b>5. Money Deposit: </b>

<b>check account detail before deposit the money</b>
curl http://localhost:4567/accounts/6642159765
Response: {<b>"balance":250.00</b>,"userName":"Amit","accountNumber":6642159765}

<b><i>curl -X POST http://localhost:4567/transaction/deposit -d '{"accountNumber":6642159765, "amount": 250}'</i></b>
 
<b>Response:</b> {"message":"Money successfully deposited"}
 
<b>check account detail after deposit the money</b>
curl http://localhost:4567/accounts/6642159765
Response: {<b>"balance":500.00 </b>,"userName":"Amit","accountNumber":6642159765}
</pre>

<pre>
<b>6. Money Withdraw: </b>

<b>check account detail before withdraw the money</b>
curl http://localhost:4567/accounts/6642159765
Response: {<b>"balance":500.00</b>,"userName":"Amit","accountNumber":6642159765}

<b><i>curl -X POST http://localhost:4567/transaction/withdraw -d '{"accountNumber":6642159765, "amount": 300}'</i></b>
 
<b>Response:</b> {"message":"Money successfully withdraw"}
 
<b>check account detail after withdraw the money</b>
curl http://localhost:4567/accounts/6642159765
Response: {<b>"balance":200.00</b>,"userName":"Amit","accountNumber":6642159765}
</pre>

<h4>Basic validations </h4>
<pre>opening balance can not be negative 

<b><i>curl -X POST http://localhost:4567/accounts -d '{"userName" : "John", "openingBalance" : -200}'</i></b>

<b>Response:</b> {"message":"Opening balance is mandatory and must not be negative"}
</pre>
<pre>if transfer amount is greater than account balance 

<b><i>curl -X POST http://localhost:4567/transaction/transfer -d '{"fromAccountNumber":6642159765, "toAccountNumber":6642159766, "transferAmount": 1000}'</i></b>

<b>Response:</b> {"message":"Insufficient Fund"}
</pre>
<pre>deposit amount is negative 

<b><i>curl -X POST http://localhost:4567/transaction/deposit -d '{"accountNumber":6642159765, "amount": -250}'</i></b>

<b>Response:</b> {"message":"Not a valid amount to credit"}
</pre>
<pre>withdraw amount is more than account balance 

<b><i>curl -X POST http://localhost:4567/transaction/withdraw -d '{"accountNumber":6642159765, "amount": 1000}'</i></b>

<b>Response:</b> {"message":"Insufficient Fund"}
</pre>
