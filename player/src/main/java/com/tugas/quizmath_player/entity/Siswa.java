/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.entity;

import jakarta.persistence.*;

/**
 *
 * @author syafiq
 */
@Entity
@Table(name = "siswa")
public class Siswa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nama", nullable = false)
    private String nama;

    @Column(name = "nis")
    private String nis;

    @Column(name = "no_absen")
    private int no_absen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kelas_id")
    private Kelas kelas;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    public Siswa() {
    }

    public Siswa(int id, String nama, String username, String password, String nis, int no_absen, Kelas kelas) {
        this.id = id;
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.nis = nis;
        this.no_absen = no_absen;
        this.kelas = kelas;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public int getNoAbsen() {
        return no_absen;
    }

    public void setNoAbsen(int no_absen) {
        this.no_absen = no_absen;
    }

    public Kelas getKelas() {
        return kelas;
    }

    public void setKelas(Kelas kelas) {
        this.kelas = kelas;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Helper for table display and compatibility
    public String getNamaKelas() {
        return kelas != null ? kelas.getKelas() : "";
    }

    public String getJurusan() {
        return kelas != null ? kelas.getJurusan() : "";
    }
}
