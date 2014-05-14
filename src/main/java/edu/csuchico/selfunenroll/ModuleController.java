package edu.csuchico.selfunenroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

// import com.blackboard.consulting.web.ModuleController;


import blackboard.admin.data.IAdminObject.RecStatus;
import blackboard.admin.data.IAdminObject.RowStatus;
import blackboard.admin.data.course.Enrollment;
import blackboard.admin.persist.course.EnrollmentLoader;
import blackboard.admin.persist.course.EnrollmentPersister;
import edu.csuchico.audit.AuditBean;
import edu.csuchico.audit.AuditDAO;
import blackboard.data.ReceiptOptions;
import blackboard.data.course.Course;
import blackboard.data.course.CourseMembership;
import blackboard.data.course.CourseMembership.Role;
import blackboard.data.course.Organization;
import blackboard.data.user.User;
import blackboard.persist.Id;
import blackboard.persist.course.CourseDbLoader;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.persist.course.CourseMembershipDbPersister;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManagerFactory; 
import blackboard.platform.plugin.PlugInUtil;
import blackboard.platform.servlet.InlineReceiptUtil;
import blackboard.portal.external.CustomData;
import blackboard.util.UrlUtil;


@Controller
@RequestMapping( value = "/modules" )
public class ModuleController implements ApplicationContextAware{
	private static final Logger log = LoggerFactory.getLogger(ModuleController.class);
	
	ApplicationContext applicationContext = null;
	
	// The following is to implement ApplicationContextAware
    // It's possible we don't need this because we can use @Value to get the 
    // properties we want...
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        System.out.println("setting context");
        this.applicationContext = applicationContext;
    }
	
	/*
	* ACTION TO LOAD Module View Page
	*/
	@RequestMapping(value = { "view" }, method = RequestMethod.GET)
	public ModelAndView view(HttpServletRequest request,
		HttpServletResponse response, ModelMap model) {
		
		EnrollmentLoader enLoader;
		CourseMembershipDbLoader memLoader;
		CourseDbLoader crsLoader;
		
		log.debug("Starting view Module target.....");
		String action = request.getParameter("action");
		if (action == null || action.equals("")){
			action = "NA";
		}
		model.addAttribute("action", action);

		
		String courseId = request.getParameter("courseId");
		if (courseId == null || courseId.equals("")){
			courseId = "NA";
		}
		model.addAttribute("courseId",courseId);
		
		ApplicationContext appContext = new ClassPathXmlApplicationContext("Beans.xml");
        // Get the B2 handle from our config.properties file.
		Properties configProps = (Properties) appContext.getBean("config");		
		String b2handle = configProps.getProperty("config.b2handle");
		model.addAttribute("b2handle", b2handle);

		String jqueryURIstring = PlugInUtil.getUri("csuc", b2handle, "js/jquery-1.11.0.js");
		model.addAttribute("jqueryURIstring", jqueryURIstring);
		
		String removeURIstring = PlugInUtil.getUri("csuc",b2handle, "modules/view");
		removeURIstring = removeURIstring + "?action=remove";
		model.addAttribute("removeURIstring",removeURIstring);
		
		

		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("modules/view-module"); 
	    ReceiptOptions ro = InlineReceiptUtil.getReceiptFromRequest(request);
	    if(null==ro){
	        ro = new ReceiptOptions();
	    }
	    
	    Context ctx = ContextManagerFactory.getInstance().getContext();

	    Id userId = ctx.getUserId();
	    // User is an object.  The User has a userName which Chico calls the portal ID.
	    User user = ctx.getUser();
	    String userBatchUid = user.getBatchUid();

	    String userName = user.getUserName();
	    model.addAttribute("userName",userName);
	    
    	try
    	{
    		memLoader = CourseMembershipDbLoader.Default.getInstance();
    		enLoader = EnrollmentLoader.Default.getInstance();
    		crsLoader = CourseDbLoader.Default.getInstance();

    	    if (action.equals("remove") && !courseId.equals("NA"))
    	    {

    	    	// Remove the user's enrollment in this courseId before getting their list.
    	    	// We may want to move this to a remove action that then pulls the same view
    	    	// after the removal.
    	    	 Course theCourse = crsLoader.loadByCourseId(courseId);

    	    	 Enrollment en = enLoader.load(theCourse.getBatchUid(), userBatchUid);
    	    	 CourseMembership mem = memLoader.loadByCourseAndUserId(theCourse.getId(), userId);
    	    	
    	    	 // I'm leaving the enrollment code commented out should we ever decide to DISABLE
    	    	 // the enrollment instead of deleteing the membership.
    	    	 CourseMembershipDbPersister.Default.getInstance().deleteById(mem.getId());
    	    	 // en.setRowStatus(RowStatus.DISABLED);
    	    	 // en.setRowStatus(RowStatus.SOFT_DELETE);
    	    	 // en.setRecStatus(RecStatus.DELETE);
    	    	 // EnrollmentPersister enPer = EnrollmentPersister.Default.getInstance();
    	    	 // enPer.update(en);  //was enPer.save(en) when we DISABLED...
    	 		 // Log what the user did in the Audit table.
    	 		 AuditDAO ado = (AuditDAO) appContext.getBean("auditDAO");
    	 		 AuditBean auditBean = new AuditBean(b2handle, "removed enrollment", theCourse.getBatchUid(), theCourse.getTitle());
    	 		 ado.save(auditBean);
    	    }//  if (action.equals("remove") && !courseId.equals("NA"))
    	}
    	catch ( Exception e )
    	{
    	        ro.addErrorMessage("Exception raised getting CourseMembershipDbLoader. [Error = "+e.getLocalizedMessage()+"]", e);
    	        e.printStackTrace();
    	}

	    
	    try
	    {
	      crsLoader = CourseDbLoader.Default.getInstance();
	      enLoader = EnrollmentLoader.Default.getInstance();
	      List<Course> crsList = crsLoader.loadByUserId( userId );

	      List<Course> crsDisplayList = new ArrayList<Course>();
	      
	      for (Course c: crsList){
	    	  if (c.getEnrollmentType() == Course.Enrollment.SELF_ENROLLMENT)
	    	  {
	    		  Enrollment en = enLoader.load(c.getBatchUid(), userBatchUid);
	    		  Role role = en.getRole();
	    		  if (role.equals(CourseMembership.Role.STUDENT))
	    			  if (en.getRowStatus() == RowStatus.ENABLED)
	    			  	crsDisplayList.add(c);
	    	  }// END if (c.getEnrollmentType()
	      }
	      
	      model.addAttribute( "courses", crsDisplayList );
	      
	      //model.addAttribute( "showCourseName", (showCourseName==null?false:showCourseName));
	      
	    }
	    catch ( Exception e )
	    {
	        ro.addErrorMessage("Exception raised loading course list. [Error = "+e.getLocalizedMessage()+"]", e);
	        e.printStackTrace();
	    }
	    
	    InlineReceiptUtil.addReceiptToRequest(request, ro); 
		return mav;
	}  // end ACTION to view the Module
	

	/*
	* ACTION TO LOAD Module personalization Page
	* Commented out the functionality.  Don't use this unless we need it...
	*/
	@RequestMapping(value = { "edit" }, method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest request,
		HttpServletResponse response, ModelMap model) {
		
		log.debug("Starting edit Module target.....");
		 
	    ReceiptOptions ro = InlineReceiptUtil.getReceiptFromRequest(request);
	    if(null==ro){
	        ro = new ReceiptOptions();
	    } 
	    ModelAndView mav = new ModelAndView();
		
		mav.setViewName("modules/edit-module"); 
	    try
	    {
	    	//Load module personalization data
	    	//CustomData cd = CustomData.getModulePersonalizationData( request );
	    	//Read settings
	    	//Boolean showCourseName = (Boolean) cd.getObjectValue("showCourseName");
	    	//add to model for the view
	    	//model.addAttribute("showCourseName", showCourseName);
	    	
	      
	    }
	    catch ( Exception e )
	    {
	        ro.addErrorMessage("Exception raised loading My Course Module Personalized Settings. [Error = "+e.getLocalizedMessage()+"]", e);
	        e.printStackTrace();
	    }
	    InlineReceiptUtil.addReceiptToRequest(request, ro); 
		return mav;
	  
	}
	
	/*
	* ACTION TO SAVE Module personalization Page
	* Commented out the functionality.  Don't use this unless we need it...
	*/
	@RequestMapping(value = { "savePersonalSettings" }, method = RequestMethod.POST)
	public ModelAndView savePersonalSettings(HttpServletRequest request,
		HttpServletResponse response, @RequestParam(value="showCourseName", required=false) String showCourseNameSelected, ModelMap model) {
		
		log.debug("Starting save personal settings Module target.....");
		 
	    ReceiptOptions ro = InlineReceiptUtil.getReceiptFromRequest(request);
	    if(null==ro){
	        ro = new ReceiptOptions();
	    }
	     
	    
	    try
	    {
	    	
	    	if(showCourseNameSelected ==null || showCourseNameSelected.isEmpty()){
	    		//not selected
	    		showCourseNameSelected="false";
	    	}
	    	//CustomData cd = CustomData.getModulePersonalizationData( request );
    		//cd.setObjectValue("showCourseName", Boolean.valueOf(showCourseNameSelected));
	    	//cd.save();
	    	ro.addSuccessMessage("My Course Module Personal Settings saved successfully.");
	     
	    }
	    catch ( Exception e )
	    {
	        ro.addErrorMessage("Exception raised loading My Course Module Personalized Settings. [Error = "+e.getLocalizedMessage()+"]", e);
	        e.printStackTrace();
	    }
	    
	    String returnUrl = UrlUtil.calculateFullUrl(request.getLocalName(), UrlUtil.isForcedSystemSSL(), "webapps/portal/frameset.jsp");
	    ModelAndView mav =  new ModelAndView( ( new RedirectView( InlineReceiptUtil.addReceiptToUrl( returnUrl, ro ) ) ) ); 
		return mav;
	 
	}
}// class ModuleController