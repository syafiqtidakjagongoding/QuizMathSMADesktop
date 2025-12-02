package com.tugas.quizmath_player.constant;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author syafiq
 */
public class Errors {
    public String message = ""; 
    public boolean isError;
    public String title;
    
    // Default: isError = true
    public Errors(String message, String title) {
        this.message = message;
        this.title = title;
        this.isError = true;
    }

    // Versi custom: bisa tentukan sendiri isError
    public Errors(String message, boolean isError) {
        this.message = message;
        this.isError = isError;
    }
}