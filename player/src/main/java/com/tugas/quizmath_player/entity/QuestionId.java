/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionId {
    private List<Integer> ids;
    private Random rand;

    // Constructor: terima daftar id dari DB
    public QuestionId(List<Integer> ids) {
        this.ids = new ArrayList<>(ids); // copy ke list baru biar aman
        this.rand = new Random();
    }

    // Ambil 1 id random dan hapus dari list
    public Integer getRandomId() {
        if (ids.isEmpty()) {
            return null; // kalau sudah habis
        }
        int index = rand.nextInt(ids.size());
        return ids.remove(index); // remove sekaligus return id
    }

    // Ambil semua id tersisa
    public List<Integer> getIds() {
        return new ArrayList<>(ids); // return copy biar aman
    }

    // Cek sisa id
    public boolean hasMore() {
        return !ids.isEmpty();
    }
}
