package ru.netology.utils;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbutils.handlers.ScalarHandler;


public class DBUtils {

    private static Connection getConnection() {
        String url = System.getProperty("url");
        String username = System.getProperty("username");
        String password = System.getProperty("password");
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void cleanTables() {
        String deleteCreditRequest = "DELETE FROM credit_request_entity; ";
        String deleteOrderEntity = "DELETE FROM order_entity; ";
        String deletePaymentEntity = "DELETE FROM payment_entity; ";
        try (val conn = DBUtils.getConnection();
             val deleteCreditRequestStmt = conn.createStatement();
             val deleteOrderEntityStmt = conn.createStatement();
             val deletePaymentEntityStmt = conn.createStatement()
        ) {
            deleteCreditRequestStmt.executeUpdate(deleteCreditRequest);
            deleteOrderEntityStmt.executeUpdate(deleteOrderEntity);
            deletePaymentEntityStmt.executeUpdate(deletePaymentEntity);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static String getValue(String request) {
        var runner = new QueryRunner();
        var value = new String();
        try (var conn = DriverManager.getConnection(System.getProperty("dbUrl"),
                System.getProperty("dbUser"), System.getProperty("dbPassword"));) {
            var result = runner.query(conn, request, new ScalarHandler<>());
            value = String.valueOf(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getCreditId() {
        var creditIdSQL = "SELECT credit_id FROM order_entity WHERE created = (SELECT max(created) FROM order_entity);";
        return getValue(creditIdSQL);
    }

    public static String getCreditStatus() {
        var creditStatusSQL = "SELECT status FROM credit_request_entity WHERE created = (SELECT max(created) FROM credit_request_entity);";
        return getValue(creditStatusSQL);
    }

    public static String getPaymentStatus() {
        var paymentStatusSQL = "SELECT status FROM payment_entity WHERE created = (SELECT max(created) FROM payment_entity);";
        return getValue(paymentStatusSQL);
    }


}


