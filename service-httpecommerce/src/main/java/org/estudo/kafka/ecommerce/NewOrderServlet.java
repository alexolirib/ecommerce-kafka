package org.estudo.kafka.ecommerce;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
    private final KafkaDispatcher<Email> emailDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
        emailDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            //?email=&amount=
            var email = req.getParameter("email");
            var amount = new BigDecimal(req.getParameter("amount"));

            var orderId = UUID.randomUUID().toString();

            var order = new Order(orderId, amount, email);
            orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, order);

            var emailKey = "email";
            var emailValue = "welcome to value email";
            var emailCode = new Email("subject teste", "body do email");
            emailDispatcher.send("ECOMMERCE_SEND_EMAIL", emailKey, emailCode);

            System.out.println("Nova Ordem sendo enviado");

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Nova Ordem sendo enviado");
        } catch (ExecutionException e){
            throw new ServletException(e);
        } catch (InterruptedException e){
            throw new ServletException(e);
        }

    }
}
