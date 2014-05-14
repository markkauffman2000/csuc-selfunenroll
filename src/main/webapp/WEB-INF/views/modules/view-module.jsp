<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@taglib uri="/bbNG" prefix="bbNG"   %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<bbNG:includedPage>

<bbNG:jsBlock> 
    <script type="text/javascript" src="${jqueryURIstring}"></script>
    <script type="text/javascript">
        var $j = jQuery.noConflict();
    </script> 

    <script type="text/javascript">

    	function confirmRemove(userName, courseId, courseTitle, message, askForInput, contentRemoved)
    	{   	    
		    $j(function () {
	    	    
	    		var result = prompt(message + ":\n" + courseTitle + "?\n" + askForInput + "\n" + contentRemoved );
	    		if (result == "remove"){
					
	    			$j.get( "${removeURIstring}"+ "&courseId="+courseId, function( data ) {	    	
	    				top.location.reload();		
	    			}); //$j.get
	    			
					
	    		}// if (result == true)
	    		
		 	}); //$j(function())
    		
    	}
    </script>
</bbNG:jsBlock>

<%--
Couldn't get the following spring tag to work, so we're sticking with the jstl/fmt.
<spring:message code="introduction" />
--%>

<fmt:setBundle basename="messages" var="lang"/>
<fmt:message key="areyousure" var="confirmMessage" bundle="${lang}"/>
<fmt:message key="askforinput" var="askForInput" bundle="${lang}"/>
<fmt:message key="contentremoved" var="contentRemoved" bundle="${lang}"/>
    


<c:choose>
<c:when test="${empty courses}">
	<fmt:message key="nocourses" bundle="${lang}"/>
</c:when> 
<c:otherwise>
    <fmt:message key="introduction" bundle="${lang}"/>
	<bbNG:miniList items="${courses}" rowHeaderId="courseTitle" var="crs" className="blackboard.data.course.Course" >
	   	<bbNG:miniListElement id="courseTitle" title="Course Title">
	    	    <bbNG:button url="javascript:confirmRemove('${userName}', '${crs.courseId}', '${crs.title}', '${confirmMessage}', '${askForInput}', '${contentRemoved}' )" label="Remove" />
	    		<font color="${color}">${crs.title} </font>
	   	</bbNG:miniListElement>
	</bbNG:miniList>
</c:otherwise>  
</c:choose>

</bbNG:includedPage>