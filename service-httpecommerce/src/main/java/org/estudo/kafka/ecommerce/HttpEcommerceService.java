package org.estudo.kafka.ecommerce;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HttpEcommerceService {

    public static void main(String[] args) throws Exception {
        var server = new Server(8080);

        //CRIAR O CONTEXT DO SERVIDOR
        var context = new ServletContextHandler();

        context.setContextPath("/");
//        exemplo: http://localhost:8080/new?email=alexolirib@gmail.com&amount=5000
        context.addServlet(new ServletHolder(new NewOrderServlet()), "/new");

        server.setHandler(context);
        //iniciar o servidor
        server.start();
        //esperar o servidor termar para finalizar a aplicação
        server.join();
    }
}