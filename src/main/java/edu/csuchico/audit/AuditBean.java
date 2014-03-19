package edu.csuchico.audit;

import java.util.Calendar;

import blackboard.data.AbstractIdentifiable;
import blackboard.persist.DataType;
import blackboard.persist.Id;
import blackboard.persist.impl.mapping.annotation.Column;
import blackboard.persist.impl.mapping.annotation.PrimaryKey;
import blackboard.persist.impl.mapping.annotation.RefersTo;
import blackboard.persist.impl.mapping.annotation.Table;
import blackboard.data.user.*;

@Table("csuc_b2_audit")
public class AuditBean extends AbstractIdentifiable {
	public static final DataType DATA_TYPE = new DataType(AuditBean.class);		
	
	private AuditBean() {}; // You must use a constructor with parameters.
	
	public AuditBean(String b2name, String description) 
	{ 
		this.b2name = b2name;
		this.description = description;
		
	}
	
	public AuditBean(String b2name, String description, String course_id, String course_name)
	{
		this.b2name = b2name;
		this.description = description;
		this.course_id = course_id;
		this. course_name = course_name;
	}

	public DataType getDataType() {return DATA_TYPE;}
	
	@PrimaryKey
	private int pk1;
	
	public int getPk1() { return pk1; }
	
	@Column(value = "b2name")
	  private String b2name;
	
	public String getB2name()
	{
		return this.b2name;
	}

	public void setB2name(String b2name) 
	{
		this.b2name = b2name;
	}// setB2name
	
	@Column(value = "datestamp")
		private Calendar datestamp;
	
	public Calendar getDatestamp() {
		return this.datestamp;
	}
	
	public void setDatestamp(Calendar ds) {
		this.datestamp = ds;
	}

	@Column(value = "user_id")
		private String user_id;

	public String getUserId() {
		return this.user_id;
	}

	public void setUserId(String userId) {
		this.user_id = userId;
	}	
	
	@Column(value = "course_id")
		private String course_id;
	
	public String getCourseId() {
		return this.course_id;
	}
	
	public void setCourseId(String courseId) {
		this.course_id = courseId;
	}

	@Column(value = "course_name")
		private String course_name;
	
	public String getCourseName() {
		return this.course_name;
	}
	
	public void setCourseName(String courseName) {
		this.course_name = courseName;
	}	
	

	@Column(value = "description")
		private String description;
	
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String d) {
		this.description = d;
	}	
		
		
}// public class AuditBean
