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
@Table(name = "kelas")
public class Kelas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "kelas", unique = true, nullable = false)
    private String kelas;

    @Column(name = "jurusan", nullable = false)
    private String jurusan;

    public Kelas() {
    }

    public Kelas(int id, String kelas, String jurusan) {
        this.id = id;
        this.kelas = kelas;
        this.jurusan = jurusan;
    }

    // Constructor without ID for creating new entities
    public Kelas(String kelas, String jurusan) {
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

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }
}
