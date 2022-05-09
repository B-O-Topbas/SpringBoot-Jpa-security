package com.example.securitydemoapp.repo;

import com.example.securitydemoapp.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KullaniciRepo extends JpaRepository<Kullanici, Long> {
    Kullanici findKullaniciByAd(String ad);


}

