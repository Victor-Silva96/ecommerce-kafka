package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

public class FraudService {


    public static void main(String[] args) {
        FraudService fraudService = new FraudService();
        try(KafkaService kafkaService = new KafkaService("ECOMMERCE_NEW_ORDER",
                fraudService::parse, FraudService.class.getSimpleName(), Order.class,
                Map.of())){
            kafkaService.run();
        }


    }

    private void parse(ConsumerRecord<String, Order> record){
        System.out.println("----------------------------------------");
        System.out.println("Processing new order, checking for fraud");
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
        System.out.println("Order processed");
    }
}
