package com.redhat.colleges.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import com.redhat.colleges.entity.Course;

@Stateless
@DeclareRoles({Role.deanAcademics_role ,Role.hod_role, Role.courseCoordinator_role})
public class CourseManagement {
		
	@PersistenceContext(unitName = "college")
	private transient EntityManager entityManager;
	
	/**
	 * Updates given course status to APPROVED
	 * if current status is REQUESTED
	 *
	 * @param id
	 *            id of course
	 * @throws CourseNotFoundException
	 *             if id doesn't exist
	 */
	@RolesAllowed(Role.hod_role)
	public void approveCourse(@NotNull Long id) throws CourseNotFoundException {
		System.out.printf("#approveCourse %s started\n", id);

		setStatusIfCurrentStatusMatch(CourseStatus.APPROVED, id, CourseStatus.REQUESTED);
	}
	
	/**
	 * Updates given course status to REJECTED
	 * if current status is REQUESTED
	 *
	 * @param id
	 *            id of course
	 * @throws CourseNotFoundException
	 *             if id doesn't exist
	 */
	@RolesAllowed(Role.hod_role)
	public void rejectCourse(@NotNull Long id) throws CourseNotFoundException {
		System.out.printf("#rejectCourse %s started\n", id);

		setStatusIfCurrentStatusMatch(CourseStatus.REJECTED, id, CourseStatus.REQUESTED);

	}
	
	/**
	 * Updates given course status to SENT_BACK
	 * if current status is REQUESTED
	 *
	 * @param id
	 *            id of course
	 * @throws CourseNotFoundException
	 *             if id doesn't exist
	 */
	@RolesAllowed(Role.hod_role)
	public void sendBackCourse(@NotNull Long id) throws CourseNotFoundException {
		System.out.printf("#sendBackCourse %s started\n", id);

		setStatusIfCurrentStatusMatch(CourseStatus.SENT_BACK, id, CourseStatus.REQUESTED);
	}
	
	/**
	 * Updates given course status to REQUESTED
	 * if current status is SENT_BACK or CREATED
	 *
	 * @param id
	 *            id of course
	 * @throws CourseNotFoundException
	 *             if id doesn't exist
	 */
	@RolesAllowed(Role.courseCoordinator_role)
	public void requestCourseApproval(@NotNull Long id) throws CourseNotFoundException {
		System.out.printf("#requestCourseApproval %s started\n", id);

		setStatusIfCurrentStatusMatch(CourseStatus.REQUESTED, id, CourseStatus.SENT_BACK, CourseStatus.CREATED);

	}
	
	/**
	 * Updates given course status to CANCELLED
	 * if current status is SENT_BACK, CREATED or REQUESTED
	 *
	 * @param id
	 *            id of course
	 * @throws CourseNotFoundException
	 *             if id doesn't exist
	 */
	@RolesAllowed(Role.courseCoordinator_role)
	public void cancelClaimRequest(@NotNull Long id) throws CourseNotFoundException {
		System.out.printf("#cancelCourseRequest %s started\n", id);

		setStatusIfCurrentStatusMatch(CourseStatus.CANCELLED, id, CourseStatus.SENT_BACK, CourseStatus.CREATED,
				CourseStatus.REQUESTED);

	}
	
	/**
	 * Updates given claim status to TECHINCAL_ERROR
	 * so claim can not be handled by any other workflow
	 * until deanAcademics review
	 *
	 * @param id
	 *            id of course
	 * @throws CourseNotFoundException
	 *             if id doesn't exist
	 */
	@RolesAllowed(Role.deanAcademics_role)
	public void lockClaimForMaintenance(@NotNull Long id) throws CourseNotFoundException {
		System.out.printf("#lockCourseForMaintenance %s started\n", id);

		setStatusIfCurrentStatusMatch(CourseStatus.TECHINCAL_ERROR, id);
	}
	
	/***********************
	 *
	 * UTILITY METHODS (hint: can be ignored)
	 *
	 **********************/
	
	public enum CourseStatus {
		APPROVED, REJECTED, SENT_BACK, REQUESTED, CANCELLED, CREATED, TECHINCAL_ERROR;
	}
	
	/**
	 * returns Course by given id
	 *
	 * @param id
	 *            id of Course
	 * @return Course
	 * @throws CourseNotFoundException
	 *             if id doesn't exist in DB
	 */
	private Course getById(long id) throws CourseNotFoundException {

		Course course = null;

		try {
			course = this.entityManager.find(Course.class, id);
		} catch (Exception e) {
			System.out.printf("#setStatusAndUpdateClaim an error occured during process: %s\n", e);
		}

		if (course == null) {
			throw new CourseNotFoundException();
		}
		return course;
	}
	
	/**
	 *
	 *
	 * @param courseStatusToSet
	 *            new Status
	 * @param id
	 *            id of Course
	 * @param courseStatuses
	 *            current status should be in this list (can be null)
	 * @throws IllegalStateException
	 *             if courseStatuses not empty and current status is not in courseStatuses
	 * @throws CourseNotFoundException
	 *             if id doesn't exist in database
	 */
	private void setStatusIfCurrentStatusMatch(CourseStatus claimStatusToSet, @NotNull Long id,
			CourseStatus... courseStatuses) throws IllegalStateException, CourseNotFoundException {

		final Course course = getById(id);

		if (courseStatuses != null && courseStatuses.length > 0) {
			List<CourseStatus> statuses = Arrays.asList(courseStatuses);

			if (!statuses.contains(CourseStatus.valueOf(course.getStatus()))) {

				throw new IllegalStateException(String.format("New status %s is not allowed to current Claim status %s",
						claimStatusToSet.name(), course.getStatus()));
			}
		}

		setStatusAndUpdateClaim(course, claimStatusToSet);

	}
	
	/**
	 * Updates Status of claim with given value
	 *
	 * @param claim to update
	 * @param claimStatus new Status
	 */
	private void setStatusAndUpdateClaim(@NotNull Course course,
			@NotNull CourseStatus courseStatus) {
		course.setStatus(courseStatus.name());
		try {
			this.entityManager.persist(course);
		} catch (Exception e) {
			System.out.printf("#setStatusAndUpdateClaim an error occured during process: %s", e);
			throw e;
		}
	}
	
	/**
	 * Checked exception in case of id not found in database
	 *
	 */
	public class CourseNotFoundException extends Exception {

		/**
		 * default serial
		 */
		private static final long serialVersionUID = 1L;

	}
}
