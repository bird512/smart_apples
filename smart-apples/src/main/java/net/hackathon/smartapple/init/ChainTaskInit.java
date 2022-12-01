package net.hackathon.smartapple.init;

import net.hackathon.smartapple.service.ChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ChainTaskInit implements CommandLineRunner {

    @Autowired
    ChainService chainService;

    @Override
    public void run(String... args) {
        chainService.scanRegister();
        chainService.scanSubLoan();
        chainService.scanPaymentReceive();
        System.out.println("chain task inited");
    }
}
