package ro.bg;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ro.bg.chat.Server;

@SpringBootApplication
@ComponentScan({"ro.bg.service", "ro.bg.controller"})
@EntityScan("ro.bg.model")
@EnableJpaRepositories("ro.bg.dao")
public class RunApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunApplication.class, args);

        Server server = new Server(8181);
        server.start();
    }

}