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
@Table(name = "siswa_answer")
public class SiswaAnswer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "siswa_id", nullable = false)
	private Siswa siswa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_answer_id", nullable = false)
	private OptionAnswer optionAnswer;

	public SiswaAnswer() {
	}

	public SiswaAnswer(Siswa siswa, OptionAnswer optionAnswer) {
		this.siswa = siswa;
		this.optionAnswer = optionAnswer;
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

	public OptionAnswer getOptionAnswer() {
		return optionAnswer;
	}

	public void setOptionAnswer(OptionAnswer optionAnswer) {
		this.optionAnswer = optionAnswer;
	}
}
