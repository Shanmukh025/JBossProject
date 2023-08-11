package com.redhat.colleges.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class Student implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	// The owning Course
	@ManyToOne(optional = false)
	@XmlTransient
	private Course course;
	
	// It's sequence number
	private int studentNumber;
	
	/*
	 * The date when the Student registration was acquired.
	 *
	 * TODO for the "Validation" task:
	 * - acquired must be in the past
	 * - if given a future date, the validation error message must be "only past data is allowed"
	 */
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date acquired;
	
	// The type of this Item.
	@ManyToOne
	private Faculty type;
	
	// The details for this Student (may be null).
	private String details;
	
	/*
	 * TODO for the "Validation" task:
	 * - amount must be between -999.99 and +999.99
	 */
	@Column(nullable = false)
	private BigDecimal feeAmount;

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

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public int getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(int studentNumber) {
		this.studentNumber = studentNumber;
	}

	public Faculty getType() {
		return type;
	}

	public void setType(Faculty type) {
		this.type = type;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Date getAcquired() {
		return acquired;
	}

	public void setAcquired(Date acquired) {
		this.acquired = acquired;
	}
	
	@Override
	public String toString() {
		return details;
	}

	public BigDecimal getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}
}
