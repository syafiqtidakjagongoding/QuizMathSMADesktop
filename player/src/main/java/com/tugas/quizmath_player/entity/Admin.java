/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.entity;
import com.tugas.quizmath_player.constant.Errors;
/**
 *
 * @author syafiq
 */
public class Admin {
    public String username;
    public String password;
    
    public Admin(String username,String password) {
        this.username = username;
        this.password = password;
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

    public String getPassword() {
        return this.password;
    }
}
