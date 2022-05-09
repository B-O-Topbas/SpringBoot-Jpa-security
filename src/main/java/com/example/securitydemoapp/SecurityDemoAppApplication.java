package com.example.securitydemoapp;

import com.example.securitydemoapp.entity.Kullanici;
import com.example.securitydemoapp.entity.Role;
import com.example.securitydemoapp.service.KullaniciService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SecurityDemoAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityDemoAppApplication.class, args);
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(KullaniciService kullaniciService) {
        return args -> {
            kullaniciService.saveRole(new Role(null,"ROLE_USER"));
            kullaniciService.saveRole(new Role(null,"ROLE_MANAGER"));
            kullaniciService.saveRole(new Role(null,"ROLE_ADMIN"));
            kullaniciService.saveRole(new Role(null,"ROLE_SUPER_ADMIN"));


            kullaniciService.saveKullanici(new Kullanici(1L,"beyto","bot58", "Topbas", "1234", new ArrayList<>()));
            kullaniciService.saveKullanici(new Kullanici(2L,"onur","onur58", "Topbas", "1234", new ArrayList<>()));
            kullaniciService.saveKullanici(new Kullanici(3L,"dilek","dilo58", "esir", "1234", new ArrayList<>()));
            kullaniciService.saveKullanici(new Kullanici(4L,"mevlut","mev37", "esir", "1234", new ArrayList<>()));

            kullaniciService.kullaniciRolVer("beyto","ROLE_SUPER_ADMIN");
            kullaniciService.kullaniciRolVer("onur","ROLE_ADMIN");
            kullaniciService.kullaniciRolVer("dilek","ROLE_MANAGER");
            kullaniciService.kullaniciRolVer("mevlut","ROLE_USER");
        };
    }
}
