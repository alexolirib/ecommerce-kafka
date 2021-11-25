package org.estudo.kafka.ecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //assim sempre vai fechar a conexão do kafka
        try(var orderDispatcher = new KafkaDispatcher<Order>()) {
            try (var emailDispatcher = new KafkaDispatcher<Email>()) {

                for (var i = 0; i < 10; i++) {
                    var userId = UUID.randomUUID().toString();
                    var orderId = UUID.randomUUID().toString();
                    var amount = new BigDecimal(Math.random() * 5000 + 1);
                    var order = new Order(userId, orderId, amount);
                    orderDispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);

                    var emailKey = "email";
                    var emailValue = "welcome to value email";
                    var email = new Email("subject teste", "body do email");
                    emailDispatcher.send("ECOMMERCE_SEND_EMAIL", emailKey, email);

                }
            }
        }

    }
}
