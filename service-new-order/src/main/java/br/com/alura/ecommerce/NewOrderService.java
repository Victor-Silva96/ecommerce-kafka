package br.com.alura.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderService {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try(KafkaDispatcher kafkaDispatcher = new KafkaDispatcher<>()) {
            for (int i = 0; i < 10; i++) {
                String userId = UUID.randomUUID().toString();
                String orderId = UUID.randomUUID().toString();
                BigDecimal amount = new BigDecimal(Math.random() * 5000 + 1);

                Order order = new Order(userId, orderId, amount);

                kafkaDispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);

                String email = "Thank you for you order! We are processing you order!";
                kafkaDispatcher.send("ECOMMERCE_EMAIL_ORDER", userId, email);
            }
        }

    }

    private static Properties properties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }


}
