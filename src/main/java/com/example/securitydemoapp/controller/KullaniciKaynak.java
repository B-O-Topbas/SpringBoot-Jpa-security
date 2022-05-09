package com.example.securitydemoapp.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.securitydemoapp.entity.Kullanici;
import com.example.securitydemoapp.entity.Role;
import com.example.securitydemoapp.service.KullaniciService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
public class KullaniciKaynak {
    private final KullaniciService kullaniciService;

    @Autowired
    public KullaniciKaynak(KullaniciService kullaniciService) {
        this.kullaniciService = kullaniciService;
    }


    @GetMapping("/kullanicilar")
    public ResponseEntity<List<Kullanici>> kullanicilar() {
        return ResponseEntity.ok().body(kullaniciService.getKullanicilar());
    }

    @PostMapping("/kullanici/ekle")
    public ResponseEntity<Kullanici> kullaniciEkle(@RequestBody Kullanici kullanici) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/kullanici/ekle").toUriString());

        return ResponseEntity.created(uri).body(kullaniciService.saveKullanici(kullanici));
    }

    @PostMapping("/role/ekle")
    public ResponseEntity<Role> roleEkle(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/ekle").toUriString());

        return ResponseEntity.created(uri).body(kullaniciService.saveRole(role));
    }

    @PostMapping("/role/kullaniciyarolver")
    public  ResponseEntity<?>addRoleToUser(@RequestBody RoletoUserFrom roletoUserFrom){
        kullaniciService.kullaniciRolVer(roletoUserFrom.getKullaniciAdi(),roletoUserFrom.getRol());
        return ResponseEntity.ok().build();
    }



    @GetMapping("/token/refresh")
    public void refresgtoken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodejwt = verifier.verify(refresh_token);

                String username = decodejwt.getSubject();

                Kullanici user=kullaniciService.getKullanici(username);


                String acces_token = JWT.create()
                        .withSubject(user.getKullaniciadi())
                        .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",user.getRoles().stream().map(Role::getRol).collect(Collectors.toList()))
                        .sign(algorithm);




                Map<String,String> tokens=new HashMap<>();
                tokens.put("access_token",acces_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);



            } catch (Exception e) {
                response.setHeader("error",e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String,String> error=new HashMap<>();
                error.put("error_message",e.getMessage());

                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);



            }


        }else {
          throw  new RuntimeException("Token not found");
        }




        }




    @Data
    class RoletoUserFrom{
        private String kullaniciAdi;
        private String rol;

    }

}
