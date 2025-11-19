/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author syafiq
 */
public class Leaderboard {
    public int id;
    public String siswa;
    public String nis;
    public String kelas;
    public int correct_anwer;
    public int wrong_anwer;
    public int total_question;
    public int final_score;

     // Constructor lengkap
    public Leaderboard(int id, String siswa, String nis, String kelas, int correct_anwer, int wrong_anwer, int total_question, int final_score) {
        this.id = id;
        this.siswa = siswa;
        this.nis = nis;
        this.kelas = kelas;
        this.correct_anwer = correct_anwer;
        this.wrong_anwer = wrong_anwer;
        this.total_question = total_question;
        this.final_score = final_score;
    }
}
