package com.example.securitydemoapp.service;


import com.example.securitydemoapp.entity.Kullanici;
import com.example.securitydemoapp.entity.Role;

import java.util.List;

public interface KullaniciService {
    Kullanici saveKullanici(Kullanici kullanici);
    Role saveRole(Role role);
    void kullaniciRolVer(String  ad, String  role);
    Kullanici getKullanici(String kullaniciAdi);
    List<Kullanici> getKullanicilar();
}
