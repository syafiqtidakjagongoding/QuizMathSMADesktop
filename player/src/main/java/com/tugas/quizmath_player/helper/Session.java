/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.helper;


public class Session {
    private static int siswaId;
    private static String siswaName;

    // set session saat login berhasil
    public static void setSession(int id, String name) {
        siswaId = id;
        siswaName = name;
    }

    // ambil id siswa
    public static int getCurrentSiswaId() {
        return siswaId;
    }

    // ambil nama siswa
    public static String getCurrentSiswaName() {
        return siswaName;
    }

    // clear session saat logout
    public static void clear() {
        siswaId = 0;
        siswaName = null;
    }
}
