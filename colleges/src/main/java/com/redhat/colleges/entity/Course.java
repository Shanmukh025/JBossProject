package com.redhat.colleges.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;


@Entity
@NamedQuery(name="findCourseByClaimant",query="select c from Course c left outer join fetch c.students where c.claimant=:param")
public class Course implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	/*
	 * The owning Course.
	 *
	 * TODO for the "Persistence Mapping" task:
	 *  - Make the students persistent.  Best practice and default lazy loading must apply.
	 *  - The other side of the association is already mapped, see Student.course.
	 *    As a consequence, Course.students will be the "inverse" side of the association
	 */
	//@Transient
	@OneToMany(mappedBy = "course")
	private List<Student> students = new ArrayList<Student>();
	
	/*
	 *  The date it was registeredOn.
	 *
	 * TODO for the "Validation" task:
	 * - registeredOn cannot be null
	 */
	@Column(nullable = false)
	private Date registeredOn = new Date();
	
	/*
	 *  The objective of this course.
	 *
	 * TODO for the "Validation" task:
	 * - objective must be no more 120 characters long
	 */
	@Column(nullable = false)
	private String objective;
	
	// The claimant of this Claim.
	@Column(nullable = false)
	private String claimant;

	// The claimant of this Claim.
	private String status;
	
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

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public Date getRegisteredOn() {
		return registeredOn;
	}

	public void setRegisteredOn(Date registeredOn) {
		this.registeredOn = registeredOn;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public String getClaimant() {
		return claimant;
	}

	public void setClaimant(String claimant) {
		this.claimant = claimant;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}	
	
	@Override
	public String toString() {
		return claimant + " - " + objective;
	}
}
