# Introduction to Servlets

Servlets are Java programs that run on a web server, handling client requests and generating dynamic responses.
Servlet used to process or store data submitted by an HTML form, provide dynamic content like returning results from a database query, manage state information, etc.

## Servlet Lifecycle

The lifecycle of a servlet is controlled by the container (e.g., Tomcat), and it goes through the following stages:

- Loading and Instantiation: The servlet class is loaded and an instance is created.
- Initialization (init method): The init method is called once to initialize the servlet.
- Request Handling (service method): The service method is called for each request to process it.
- Destruction (destroy method): The destroy method is called once before the servlet is removed from service.
