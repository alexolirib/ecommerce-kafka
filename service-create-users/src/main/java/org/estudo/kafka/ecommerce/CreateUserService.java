package org.estudo.kafka.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class CreateUserService {

    private final Connection connection;

    CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        this.connection= DriverManager.getConnection(url);
        try {

            connection.createStatement().execute("create table Users (" +
                    "uuid varchar(200) primary key, " +
                    "email varchar(200))"
            );
        } catch (SQLException e){
            //ignorando se der erro, pois irá acontecer somente quando o banco já existir
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        var userService = new CreateUserService();
        //assim sempre vai fechar a conexão do kafka
        try(var service = new KafkaService<>(
                KafkaService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                userService::parse,
                //classe que vou DESERIALIZER
                Order.class,
                new HashMap()
        )) {
            service.run();
        }
    }

    //para enviar mensagem tbm , seria o retorno da mensagem
    private final KafkaDispatcher<Order> orderKafkaDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException, SQLException {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, checking for user");
        System.out.println("value - " + record.value());
        var order = record.value();
        if(this.isNewUser(order.getEmail())){
            insertNewUser(order.getEmail(), order.getUserId());
        }
    }

    private void insertNewUser(String email, String uuid) throws SQLException {
        var insert = connection.prepareStatement("insert into Users (uuid, email)"+
                " values (?,?)");
        insert.setString(1, uuid);
        insert.setString(2, email);
        insert.execute();
        System.out.println("Usuário "+ email +" inserido com sucesso!");
    }

    private boolean isNewUser(String email) throws SQLException {
        var exists = connection.prepareStatement("select uuid from Users" +
                " where email = ? limit 1");
        exists.setString(1, email);
        var results = exists.executeQuery();
        //se for para a próxima linha significa que não é um usuário novo
        return !results.next();
    }


}
