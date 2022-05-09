package com.example.securitydemoapp.entity;

import lombok.*;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;


@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;//buradan generation value yi kaldırdım hata olursa ekle
    private String rol;
}
