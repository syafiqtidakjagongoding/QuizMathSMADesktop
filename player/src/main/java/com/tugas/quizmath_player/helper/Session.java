package com.tugas.quizmath_player.helper;


public class Session {
    private static int siswaId;
    private static String siswaName;
    private static String adminUsername;

    // set session saat login berhasil
    public static void setSession(int id, String name) {
        siswaId = id;
        siswaName = name;
    }
    
    // set admin session
    public static void setAdminSession(String username) {
        adminUsername = username;
    }

    // ambil id siswa
    public static int getCurrentSiswaId() {
        return siswaId;
    }

    // ambil nama siswa
    public static String getCurrentSiswaName() {
        return siswaName;
    }
    
    // ambil username admin
    public static String getCurrentAdminUsername() {
        return adminUsername;
    }

    // clear session saat logout
    public static void clear() {
        siswaId = 0;
        siswaName = null;
        adminUsername = null;
    }
}
