/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import com.tugas.quizmath_player.constant.Errors;

/**
 *
 * @author syafiq
 */
public class DataSiswa {
    public String username;
    public String password;
    public String nama;
    public String nis;
    public String kelas;
    public String absen;
    
    
   // Constructor dengan parameter
    public DataSiswa(String username,String password) {
        this.username = username;
        this.password = password;
    }
    
      public DataSiswa(String username, String absen, String nama, String nis, String kelas) {
        this.username = username;
        this.absen = absen;
        this.nama = nama;
        this.nis = nis;
        this.kelas = kelas;
    }
    
    public Errors validate() {
       if (username == null || username.trim().isEmpty()) {
        return new Errors("❌ Username tidak boleh kosong", true);
    }
     
      if (password == null || password.trim().isEmpty()) {
        return new Errors("❌ Password tidak boleh kosong", true);
    }
   
    return new Errors("✅ Data valid", false);
    }
}
