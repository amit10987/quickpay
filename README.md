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
2. POST /accounts                     <i>   # get all accounts </i>
3. GET /accounts/:accountNumber       <i>   # get all accounts </i>
4. POST /transaction/transfer         <i>   # get all accounts </i>
5. POST /transaction/deposit          <i>   # get all accounts </i>
6. POST /transaction/withdraw         <i>   # get all accounts </i></pre></h5>

<h4> Sample Request and Response </h4>
<pre> By defaults application runs on port 4567 and it creates two default accounts to play with </pre>
<pre><b>Get all the accounts  

url:http://localhost:4567/accounts </b>


</pre>

