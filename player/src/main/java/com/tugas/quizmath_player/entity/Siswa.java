/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.entity;

/**
 *
 * @author syafiq
 */
public class Siswa {
    public int id;
    public String nama;
    public String nis;
    public int no_absen;
    public String kelas;
    public String jurusan;
     public String username;
    public String password;
    
     // Constructor
    public Siswa(int id, String nama,String username,String password, String nis, int no_absen, String kelas, String jurusan) {
        this.id = id;
        this.nama = nama;
        this.nis = nis;
        this.username = username;
        this.password = password;
        this.no_absen = no_absen;
        this.kelas = kelas;
        this.jurusan = jurusan;
    }
    
  

}
