<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@taglib uri="/bbNG" prefix="bbNG"   %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<bbNG:includedPage>
<bbNG:jsBlock> 
    <script type="text/javascript">
    	function confirmRemove()
    	{
    		alert("Are you certain?");
    	}
    </script>
</bbNG:jsBlock>

<%--
Couldn't get the following spring tag to work, so we're sticking with the jstl/fmt.
<spring:message code="introduction" />
--%>

<fmt:setBundle basename="messages" var="lang"/>
<fmt:message key="introduction" bundle="${lang}"/>

<bbNG:miniList items="${courses}" rowHeaderId="courseTitle" var="crs" className="blackboard.data.course.Course" >
       	
    	<bbNG:miniListElement id="courseTitle" title="Course Title">
    	    <bbNG:button url="javascript:confirmRemove()" label="Remove" />
    		<font color="${color}">${crs.title} </font>

    	</bbNG:miniListElement>
    
</bbNG:miniList> 
</bbNG:includedPage>