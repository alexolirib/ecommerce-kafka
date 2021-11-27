package org.estudo.kafka.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class FraudDetectorService {

    public static void main(String[] args) {
        var fraudService = new FraudDetectorService();
        //assim sempre vai fechar a conexão do kafka
        try(var service = new KafkaService<>(
                    FraudDetectorService.class.getSimpleName(),
                    "ECOMMERCE_NEW_ORDER",
                    fraudService::parse,
                //classe que vou DESERIALIZER
                    Order.class,
                    new HashMap()
                )) {
            service.run();
        }
    }

    //para enviar mensagem tbm , seria o retorno da mensagem
    private final KafkaDispatcher<Order> orderKafkaDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, checking for fraud");
        System.out.println("------------------------------------------");
        System.out.println("key - "+ record.key());
        System.out.println("value - " + record.value());
        System.out.println("partition - " + record.partition());
        System.out.println("offset - " + record.offset());
        System.out.println("------------------------------------------");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            //ignoring
            e.printStackTrace();
        }
        //order processing
        var order = record.value();
        if (idFraud(order)){
            // verificando se é maior que 4500 a solicitação
            System.out.println("Order is a fraud!!!!!!" );
            orderKafkaDispatcher.send("ECOMMERCE_ORDER_REJECTED", order.getUserId(), order);
        }else {
            System.out.println("Approved: "+ order);
            orderKafkaDispatcher.send("ECOMMERCE_ORDER_APPROVED", order.getUserId(), order);
        }
    }

    private boolean idFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("4500"))>=0;
    }


}
