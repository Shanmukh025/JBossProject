package com.redhat.colleges;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Test;

import com.redhat.colleges.entity.Course;
import com.redhat.colleges.entity.Faculty;
import com.redhat.colleges.entity.Student;
import com.redhat.training.College;

public class ValidationTest {

	College c = new College();
	
	
	@Test
    public void getdatawithRequest() throws Exception
    {
		Course course = new Course();
		course.setRegisteredOn(null);
				
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Course>> violations = validator.validate(course);
		boolean result = false;
		if(violations.size()>0)
			result=true;
		assertTrue(result);	
    }
		
	@Test
    public void getdetailswithRequest()
    {
        Course course = new Course();
        course.setObjective(c.objective);	
				
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Course>> violations = validator.validate(course);
		boolean result = false;
		if(violations.size()>0)
			result=true;
		assertTrue(result);	
    }
	
	@Test
    public void senddataonRequest() throws ParseException
    {
		Student stu = new Student();
		stu.setAcquired(c.myDate);
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Student>> violations = validator.validate(stu);
		boolean result = false;
		for (ConstraintViolation<Student> violation : violations) {
			if(violation.getMessage().equals(c.msg))
				result = true;
		}
		assertTrue(result);	
    }
	
	@Test
    public void senddetailsonRequest() throws ParseException
    {
		Student stu = new Student();
		stu.setFeeAmount(c.B);
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Student>> violations = validator.validate(stu);
		boolean result = false;
		if(violations.size()>0)
			result=true;
		assertTrue(result);	
    }
	
	@Test
    public void passdatatopipeline()
    {
		Faculty F = new Faculty();
		F.setRatio(c.ratio);
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Faculty>> violations = validator.validate(F);
		boolean result = false;
		if(violations.size()>0)
			result=true;
		assertTrue(result);	
    }
}
