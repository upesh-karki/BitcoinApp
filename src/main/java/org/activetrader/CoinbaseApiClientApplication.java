package org.activetrader;




import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CoinbaseApiClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoinbaseApiClientApplication.class, args);
    }
}

