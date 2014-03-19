<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@taglib uri="/bbNG" prefix="bbNG"   %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<bbNG:includedPage>
<%--
Couldn't get the following spring tag to work, so we're sticking with the jstl/fmt.
<spring:message code="introduction" />
--%>

<fmt:setBundle basename="messages" var="lang"/>
<fmt:message key="introduction" bundle="${lang}"/>

<bbNG:miniList items="${courses}" rowHeaderId="courseId" var="crs" className="blackboard.data.course.Course" >
	
    	<bbNG:miniListElement id="courseId" title="Course Id">
    		<font color="${color}">${crs.courseId}</font>
    	</bbNG:miniListElement>
    
</bbNG:miniList> 
</bbNG:includedPage>