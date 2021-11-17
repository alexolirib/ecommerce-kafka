package org.estudo.kafka.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailServiceService {

    public static void main(String[] args) {
        var emailService = new EmailServiceService();
        try(var service = new KafkaService(
                EmailServiceService.class.getSimpleName(),
                "ECOMMERCE_SEND_EMAIL",
                emailService::parse)) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("------------------------------------------");
        System.out.println("Processing new Email, checking for email");
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
