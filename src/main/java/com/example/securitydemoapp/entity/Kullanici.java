package com.example.securitydemoapp.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.Collection;


@Entity
@Data
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Kullanici {
    @Id
    private Long id;
    private String ad;
    private String kullaniciadi;
    private String soyad;
    private String sifre;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles=new ArrayList<>();

}
