package org.estudo.kafka.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.regex.Pattern;

public class LogService {

    public static void main(String[] args) {
        var logService = new LogService();
        try (var service = new KafkaService(LogService.class.getSimpleName(),
                Pattern.compile("ECOMMERCE.*"),logService::parse)) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) {
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
    }
}
