package ru.netology.utils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {


    public static String getCreditId() {
        var creditIdSQL = "SELECT credit_id FROM order_entity order by created desc limit 1;";
        return getValue(creditIdSQL);
    }

    public static String getCreditStatus() {
        var creditStatusSQL = "SELECT status FROM credit_request_entity order by created desc limit 1;";
        return getValue(creditStatusSQL);
    }

    public static String getPaymentStatus() {
        var paymentStatusSQL = "SELECT status FROM payment_entity order by created desc limit 1;";
        return getValue(paymentStatusSQL);
    }

    public static String getPaymentAmount() {
        var paymentAmountSQL = "SELECT amount FROM payment_entity order by created desc limit 1;";
        return getValue(paymentAmountSQL);
    }

    public static String getValue(String request) {
        var runner = new QueryRunner();
        var value = new String();
        try (var conn = DriverManager.getConnection(System.getProperty("dbUrl"),
                System.getProperty("dbUsername"), System.getProperty("dbPassword"));) {
            var result = runner.query(conn, request, new ScalarHandler<>());
            value = String.valueOf(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }
}

