# Refactoring Legacy - A Google Guice Story

## Legacy
One word that sends shivers down most developers spines is ‘Legacy’. There are many different kinds of legacy-systems and obstacles that they impose. Legacy exists on a scale; on one end there are systems that you don’t want to touch because no matter what you do, they will most likely implode. On the other end there are systems that only have been mistreated code-wise or architecturally but actually have some kind of potential and can be refactored iteratively and improved upon. Some legacy-systems could be at their end of life while other will continue to live for a long time.

Not so long ago, i was a part of a team that got a legacy application assigned to us. This legacy-system was probably somewhere down the middle on the ‘legacy-scale’ and there were no plans to replace it. The perfect victim for some refactoring and improvements! Not only for the sake of the system but for the sake of the developers that are going maintain the application and introduce new features.

The application was written in Java and there were three ways that this application was being used:
* JSP pages
* REST API (introduced a couple of months before we took over)
* Standalone Java Job - Runs scheduled jobs (not Quartz, just plain Java with Threads)

## A simple problem
A very simple code example from that project is the following:

```
public class OrderDoer {
  public static OrdersSum getOrdersSum(Transaction transaction, long customerId) {
    List<Order> orders = OrderFetcher.getOrders(transaction, customerId);
    OrdersSum ordersSum = // ... do something with orders to get the 'sum'
    return ordersSum;
  }
}
```


```
public class OrderFetcher {
  public static List<Order> getOrders(Transaction transaction, long customerId) {
    Result result = transaction.executeSql("select * from order where customer_id = " + customerId);
    List<Order> orders = // ... convert result to a list of orders
    return orders;
  }
}
```

100% of the code was written in this way and it imposes a few problems:
* The transaction object is never used in 'OrderDoer'. It is just completely unnecessary and takes up precious space in our method and class imports. The worst case of unnecessary ‘object-forwarding’ that i came across in the application was 5 levels deep...
* Static method calls are not testable (I know that PowerMock exists but that’s only treating the symptoms and not the cause of the problem).

## Dependency Injection
So what we wanted to do with this application was to introduce Dependency Injection (DI) to solve these issues (I'm not going to explain what DI is, Google does a fine job at that). After some research i found that these three were the best options available:
* JEE/CDI/Weld
* Spring
* Google Guice

Remember that a part of the application was Standalone Java. To my understanding all of these works in standalone environments (not JEE, but Weld) but Google Guice seemed to be the most lightweight of them all and and Google tend to make libraries that work very well so i decided to go with Google Guice. Another reason was that i had never heard of Guice but everyone basically knows what JEE/CDI/Weld and Spring are.

## A simple problem solved with Google Guice and Dependency Injection
Here is an example of the same code as above but with DI using Google Guice:

```
public class OrderDoer {

  @Inject
  private OrderFetcher orderfetcher;

  public OrdersSum getOrdersSum(long customerId) {
    List<Order> orders = orderFetcher.getOrders(customerId);
    OrdersSum ordersSum = // ... do something with orders to get the 'sum'
    return ordersSum;
  }
}
```

```
public class OrderFetcher {

  @Inject
  private Transaction transaction;

  public List<Order> getOrders(long customerId) {
    Result result = transaction.executeSql("select * from order where customer_id = " + customerId);
    List<Order> orders = // ... convert result to a list of orders
    return orders;
  }
}
```

The issues have been resolved! Both OrderDoer and OrderFetcher can easily be tested by injecting whatever you like in your JUnit tests and OrderDoer never have to bother with Transaction.

## Dependency Injection and object creation
On to the next issue. In our application, Transaction is a special wrapper of java.sql.Connection and it cannot be created with a default constructor and it is also created differently depending on if you are using the application via JSP or REST API/Standalone Java Job.

In the REST API and Standalone Java Job the Transaction is created once per initiation.
So if a API method is called a single Transaction is created and commited by the end of that call. The same goes for the Standalone Java Job, when a scheduled job runs, a single Transaction is created and commited by the end of the job.

In JSP, each page creates a single Transaction and it is committed by the end of that page. However, that means that if you use <jsp:import> to import another page, two Transactions will be created. If any JSP page calls any business logic with a Transaction, it will always call the method with the latest created Transaction. (Say what you want, this is how the application works and we had no plans to change it. One step at a time :)).

Simple examples of how Transaction is created in REST API, Standalone Java Job and JSP before Google Guice and DI:

```
// REST
@GET
public Response doRestStuff() {
  Transaction transaction = new Transaction("Simple parameters");
  …
  transaction.commit();
}
```

```
// Standalone
public void doStandaloneStuff() {
  Transaction transaction = new Transaction("Simple parameters");
  ....
  transaction.commit();
}
```

```
public class TransactionPage extends HttpServlet implements HttpJspPage {

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Transaction transaction = new Transaction("Special JSP parameters");
    ((List<Transaction>) request.getAttribute("transactionList").add(transaction);
    _jspService(request, response);
    ((List<Transaction>) request.getAttribute("transactionList").remove(transaction);
  }

  protected Transaction getTransaction(HttpServletRequest request) {
    return request.getAttribute("transactionList").get(request.getAttribute("transactionListForThisRequest").size() - 1);
  }

  ...
}
```

```
<%@ page extends="TransactionPage" %>
<%
Transaction transaction = getTransaction(request);
…
transaction.commit();
%>
```

## Google Guice Providers
To inject objects that cannot be created with a default constructor in Google Guice one must use something called Provider. A simple Provider for Transaction that can be used for our API or Standalone Java Job could look like this:

```
public class SimpleTransactionProvider implements Provider<Transaction> {

  @Override
  public Transaction get() {
    return new Transaction("Simple parameters");
  }
}
```

To configure Guice to use this Provider when creating a Transaction instead of the defualt no-args constructor we need to extend something called AbstractModule:

```
public class SimpleGuiceModule extends AbstractModule {

  @Override
  protected void configure() {
bind(Transaction.class).toProvider(SimpleTransactionProvider.class).in(ServletScopes.REQUEST);
  }
}
```

Basically this tells Guice that when Transaction should be injected, Guice needs to ask the SimpleTransactionProvider for an instance of Transaction via the get method. One can also specify the scope (basically how long a certain instance should live). In our case we specify REQUST scope which means that a Transaction created with the Provider should live for an entire HTTP Request. Four standard scopes exists Default, Request, Session and Singleton. The latter three are quite self-explanatory. The first one (Default) have no name and will inject a new instance whenever anything is requested.

As you may already have figured out, ServletScopes.REQUEST (and ServletScopes.SESSION) does not exist in our Standalone Java Job. Only if it is run in a web-server. The good thing is that it is quite easy to write custom Scopes and there are a few examples out there and a popular one is THREAD scope. I will not go into detail about THREAD scope, i’ve included an implementation in the github repo. 

For our Standalone Java App we just create the following Guice Module:

```
public class StandaloneGuiceModule extends AbstractModule {

  @Override
  protected void configure() {
   bind(Transaction.class).toProvider(SimpleTransactionProvider.class).in(CustomScopes.THREAD);
  }
}
```

Since our JSP pages have multiple Transactions we need to store them in a Request scoped object (not Provider), lets call it TransactionManager.

```
public class TransactionManager {
  private Deque<Transaction> transactions = new ArrayDeque<>();

  public void push(Transaction transaction) {
    transactions.push(transaction);
  }

  public Transaction pop() {
    return transactions.pop();
  }

  public Transaction peek() {
    return this.transactions.peek();
  }
}
```

We also need to push our Transactions created by the TransactionPage to the TransactionManager:

```
public class TransactionPage extends HttpServlet implements HttpJspPage {

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Transaction transaction = new Transaction("Special JSP parameters");
    getGuiceInstance(TransactionManager.class).push(transaction);
    _jspService(request, response);
    getGuiceInstance(TransactionManager.class).pop();
  }

  // Used to get Guice objects in JSP pages
  protected <T> T getGuiceInstance(Class<T> clazz) {
    return ((Injector) getServletContext().getAttribute(Injector.class.getName())).getInstance(clazz);
  }

  ...
}
```

Then we create a Transaction Provider that also has a reference to the Request scoped TransactionManager and just fetches the latest created Transaction from there.

```
public class WebTransactionProvider implements Provider<Transaction> {

  @Inject
  private TransactionManager transactionManager;

  @Override
  public Transaction get() {
    return transactionManager.peek();
  }
}
```

And finally we configure it all in a Guice Module. First we bind the TransactionManager in Request Scope and then we bind the Transaction to the WebTransactionProvider in default scope.

```
public class WebGuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(TransactionManager.class).in(ServletScopes.REQUEST);
    bind(Transaction.class).toProvider(WebTransactionProvider.class);
  }
}
```

With the combination of Providers and Guice Modules we have enabled the injection of Transactions in two different scopes and three different areas of the application.

## Summary
I like Google Guice! It is a powerful but lightweight library that can be configured in almost any way you like. It is easy to set up and there are no magic xml files (except for web.xml but we are not escaping that one), and everything is configured in plain Java.

I am not going to compare Weld, Spring and Guice because i think a search on Google can provide a far better explanation than me.

Alongside with the introduction of DI we migrated from Ant to Maven, introduced JUnit (!) and the generation of a WADL description file for the API. We also improved the application by continuously removing code duplication and refactored the different modules to have a more clear goal and to encourage code reuse. Not only did we improve the code, we also got a better understanding of how the application actually worked which is of great help when developing new features.

I believe that working with legacy actually can be quite fun and rewarding (to a certain degree of course) as long as you have the authority and freedom to take the time to introduce these kind of changes.

## Running the example
In order to see the full examples in action just clone the github repo and execute the follow these steps:

**Install and run WildFly with Docker:**
docker run -it -p 8080:8080 -v /local/folder/for/your/deployments/:/opt/jboss/wildfly/standalone/deployments/:rw jboss/wildfly

**Install and run WildFly locally:**
Download and extract the wildfly-10.0.0.Final.tar.gz from http://wildfly.org/downloads/
Start it with /your/installation/folder/wildfly-10.0.0.Final/bin/standalone.sh

**Clone the repository:**

[https://github.com/stromvap/squeed-guice-example](https://github.com/stromvap/squeed-guice-example)

**Build the project:**
mvn clean install

**Copy war to WildFly:**
Docker: cp guice-example-web/target/guice-example-web-1.0.0-SNAPSHOT.war /local/folder/for/your/deployments/
Locally: cp guice-example-web/target/guice-example-web-1.0.0-SNAPSHOT.war /your/installation/folder/wildfly-10.0.0.Final/standalone/deployments/

**Run the JSP page:**
http://localhost:8080/index.jsp

**Run the API GET:**
http://localhost:8080/api

**Run the Standalone Java ‘Job’:**
java -jar guice-example-standalone/target/guice-example-standalone-1.0.0-SNAPSHOT-jar-with-dependencies.jar

*Note that i use WildFly 10 and Java 8 but the example can be run in any JBoss or WildFly version and with Java 6. You might have to modify some dependency versions as they have been compiled for Java 8.*