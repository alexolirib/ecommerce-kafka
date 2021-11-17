package org.estudo.kafka.ecommerce;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //assim sempre vai fechar a conexão do kafka
        try(var dispatcher = new KafkaDispatcher()) {

            for (var i = 0; i < 10; i++) {
                var key = UUID.randomUUID().toString();
                var value = key + " 10992310298,3232,44";
                dispatcher.send("ECOMMERCE_NEW_ORDER", key, value);

                var emailKey = "email";
                var emailValue = "welcome to value email";
                dispatcher.send("ECOMMERCE_SEND_EMAIL", emailKey, emailValue);

            }
        }

    }
}
