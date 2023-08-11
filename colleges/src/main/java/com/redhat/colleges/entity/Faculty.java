package com.redhat.colleges.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Faculty implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
		
	/*
	 * The code of this Faculty.
	 *
	 * TODO for the "Validation" task:
	 * - ratio must be exactly 3 digit, ':', 2 digits (e.g. 123:45).
	 *   The regular expression for this is "\\d{3}:\\d{2}"
	 */
	@Column(nullable = false, unique = true)
	private String ratio;
	
	// The description of this Faculty.
	@Column(nullable = false, unique = true)
	private String description;

	
	/***************************
	 *
	 * GETTERS/SETTERS (hint: can be ignored)
	 *
	 ***************************/
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return ratio;
	}
}
