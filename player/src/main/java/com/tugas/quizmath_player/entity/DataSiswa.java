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
public class DataSiswa {

    public String username;
    public String password;
    public String absen;
    public String name;
    public String nis;
    public String jurusan;
    public String kelas;

    public DataSiswa(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public DataSiswa(String username, String absen, String name, String nis, String kelas, String jurusan) {
        this.username = username;
        this.absen = absen;
        this.name = name;
        this.nis = nis;
        this.kelas = kelas;
        this.jurusan = jurusan;
    }

    // Getter
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Validasi
    public Errors validate() {

        if (username == null || username.trim().isEmpty()) {
            return new Errors("Username tidak boleh kosong!", true);
        }

        if (password == null || password.trim().isEmpty()) {
            return new Errors("Password tidak boleh kosong!", true);
        }

        // Tambahan validasi opsional:
        if (username.length() < 3) {
            return new Errors("Username minimal 3 karakter!", true);
        }

        if (password.length() < 3) {
            return new Errors("Password minimal 3 karakter!", true);
        }

        return new Errors("OK", false);
    }
}
