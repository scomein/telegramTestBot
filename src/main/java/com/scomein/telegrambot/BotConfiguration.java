package com.scomein.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ComponentScan("com.scomein.telegrambot.entities")
public class BotConfiguration {

    public static final String BOT_TOKEN = "token";

    @Bean
    TelegramBot telegramBot() {
        return TelegramBotAdapter.build(System.getProperty(BOT_TOKEN, "343530764:AAGCuisCe4Dk7M0hQ1v7PSUnb-rmNX67ZP4"));
    }

    @Bean
    SessionFactory sessionFactory() {
        return new org.hibernate.cfg.Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }


    @Bean
    PlatformTransactionManager transactionManager() {
        HibernateTransactionManager manager = new HibernateTransactionManager();
        manager.setSessionFactory(sessionFactory());
        manager.setDataSource(new DriverManagerDataSource("jdbc:postgresql://localhost:5432/template1", "postgres", "password"));
        return manager;
    }
}
