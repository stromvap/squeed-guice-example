Guice example

* Describe the issues with Legacy applications:
 - Static methods (Not testable)
 - Passing of unnessecary parameters
 - Not always running in a state-of-the-art Web Server

* What to show
 - Guice in JSP pages with Request scope and TransactionManager
 - Guice in API with Request scope and SimpleTransactionProvider
 - Guice in Standalone with Thread scope and SimpleTransactionProvider

* Blog post
 - Talk about Legacy and Refactoring.
 - Show simple method with static Business call and Transaction parameter forwarding
 - Show improved method with injected Business and removed Transaction
 - But where did the Transaction go?
 - Show bigger examples of our 3 scenarios: JSP, API, Standalone
 - Show how we inject the Transaction with Providers and configure it in Guice Modules

* Conclusion
 - Refactoring = Better understanding of the code and improved code
 - Guice works everywhere and is easy to set up. Small comparison to JEE and Spring
 - No magic xml files (except for web.xml). All configurations are in the Guice Modules

=Game of Guice - A Song of Legacy and Refactoring=
=Google Guice - Legacy and Refactoring=

One word that sends shivers down most developers spines is 'Legacy'.
And to quote the most recent blog-post on blog.squeed.com: Why? Why does Legacy 