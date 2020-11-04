package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

public class EmailService {

    public static void main(String[] args) {
        EmailService emailService = new EmailService();
        try(KafkaService kafkaService = new KafkaService("ECOMMERCE_EMAIL_ORDER",
              emailService::parse, EmailService.class.getSimpleName(),
                String.class,
                Map.of())){
            kafkaService.run();
        }


    }

    private void parse(ConsumerRecord<String, String> record){
        System.out.println("----------------------------------------");
        System.out.println("Thanks for order, sending email");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.offset());
        System.out.println(record.partition());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignoring
            e.printStackTrace();
        }
        System.out.println("Sending email");
    }
}
