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
@Table(name = "final_score")
public class FinalScore {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "siswa_id", nullable = false, unique = true)
	private Siswa siswa;

	@Column(name = "correct_answer")
	private int correctAnswer;

	@Column(name = "wrong_answer")
	private int wrongAnswer;

	@Column(name = "total_question")
	private int totalQuestion;

	@Column(name = "final_score")
	private double finalScore;

	public FinalScore() {
	}

	public FinalScore(Siswa siswa, int correctAnswer, int wrongAnswer, int totalQuestion, double finalScore) {
		this.siswa = siswa;
		this.correctAnswer = correctAnswer;
		this.wrongAnswer = wrongAnswer;
		this.totalQuestion = totalQuestion;
		this.finalScore = finalScore;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Siswa getSiswa() {
		return siswa;
	}

	public void setSiswa(Siswa siswa) {
		this.siswa = siswa;
	}

	public int getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public int getWrongAnswer() {
		return wrongAnswer;
	}

	public void setWrongAnswer(int wrongAnswer) {
		this.wrongAnswer = wrongAnswer;
	}

	public int getTotalQuestion() {
		return totalQuestion;
	}

	public void setTotalQuestion(int totalQuestion) {
		this.totalQuestion = totalQuestion;
	}

	public double getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(double finalScore) {
		this.finalScore = finalScore;
	}
}
