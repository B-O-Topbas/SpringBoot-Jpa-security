package com.example.securitydemoapp.service;

import com.example.securitydemoapp.entity.Kullanici;
import com.example.securitydemoapp.entity.Role;
import com.example.securitydemoapp.repo.KullaniciRepo;
import com.example.securitydemoapp.repo.RoleRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



@Service
 @Transactional @Slf4j
public class KullaniciServiceImpl implements KullaniciService , UserDetailsService {
    private final KullaniciRepo kullaniciRepository;
    private final RoleRepo roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public KullaniciServiceImpl(KullaniciRepo kullaniciRepository, RoleRepo roleRepository, PasswordEncoder passwordEncoder) {
        this.kullaniciRepository = kullaniciRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Kullanici saveKullanici(Kullanici kullanici) {
        log.info("yeni kullanici kaydediliyor: " + kullanici.getAd());

        kullanici.setSifre(passwordEncoder.encode(kullanici.getSifre()));
        return kullaniciRepository.save(kullanici);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("yeni rol kaydediliyor: " + role.getRol());
        return roleRepository.save(role);
    }

    @Override
    public void kullaniciRolVer(String ad, String role) {
        log.info("kullaniciya yeni  rol veriliyor: " + ad + " " + role);
        Kullanici kullanici = kullaniciRepository.findKullaniciByAd(ad);
        Role rol = roleRepository.findByrol(role);
        kullanici.getRoles().add(rol);
    }

    @Override
    public Kullanici getKullanici(String kullaniciAdi) {
        log.info("kullanici getiriliyor: " + kullaniciAdi);
        return kullaniciRepository.findKullaniciByAd(kullaniciAdi);
    }

    @Override
    public List<Kullanici> getKullanicilar() {
        log.info("kullanicilar getiriliyor");
        return kullaniciRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Kullanici kullanici = kullaniciRepository.findKullaniciByAd(username);
        if (kullanici == null) {
            log.error("kullanici bulunamadi: " + username);
            throw new UsernameNotFoundException("Kullanici veritabanında bulunamadi: " + username);
        }else{

            log.info("kullanici bulundu: " + username);
        }
        Collection < SimpleGrantedAuthority>authorities=new ArrayList<>();
        kullanici.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRol()));
        });
        return new org.springframework.security.core.userdetails.User(kullanici.getKullaniciadi(),kullanici.getSifre(),authorities);
    }
}


