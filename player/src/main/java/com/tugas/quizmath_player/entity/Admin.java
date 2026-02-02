/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.entity;

import jakarta.persistence.*;
import com.tugas.quizmath_player.constant.Errors;

/**
 *
 * @author syafiq
 */
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    public Admin() {
    }

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Errors validate() {
        if ("".equals(username.trim())) {
            return new Errors("Username tidak boleh kosong", "Username Error");
        }

        if ("".equals(password.trim())) {
            return new Errors("Password tidak boleh kosong", "Password Error");
        }
        return new Errors("Data valid", false);

    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
