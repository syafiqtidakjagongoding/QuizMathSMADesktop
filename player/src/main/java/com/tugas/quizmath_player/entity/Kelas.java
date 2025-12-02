/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.entity;

/**
 *
 * @author syafiq
 */

    
public class Kelas {
    public int id;
    public String kelas;
    public String jurusan;
    
    public Kelas(int id,String kelas,String jurusan) {
        this.id = id;
        this.kelas = kelas;
        this.jurusan = jurusan;
    }
    
    @Override
    public String toString() {
        return this.kelas; // misalnya hanya nama kelas
    }

    public String getKelas() {
        return kelas;
    }
    
    public int getId() {
        return id;
    }

}
