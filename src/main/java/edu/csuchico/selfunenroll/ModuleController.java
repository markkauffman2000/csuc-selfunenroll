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






import edu.csuchico.audit.AuditBean;
import edu.csuchico.audit.AuditDAO;
import blackboard.data.ReceiptOptions;
import blackboard.data.course.Course;
import blackboard.data.course.Organization;
import blackboard.persist.Id;
import blackboard.persist.course.CourseDbLoader;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManagerFactory; 
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
		
		log.debug("Starting view Module target.....");
		
		ApplicationContext appContext = new ClassPathXmlApplicationContext("Beans.xml");
        // Get the B2 handle from our config.properties file.
		Properties configProps = (Properties) appContext.getBean("config");		
		String b2handle = configProps.getProperty("config.b2handle");
		model.addAttribute("b2handle", b2handle);
		
		// Log what the user did in the Audit table.
		AuditDAO ado = (AuditDAO) appContext.getBean("auditDAO");
		AuditBean auditBean = new AuditBean(b2handle, "view");
		ado.save(auditBean);
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("modules/view-module"); 
	    ReceiptOptions ro = InlineReceiptUtil.getReceiptFromRequest(request);
	    if(null==ro){
	        ro = new ReceiptOptions();
	    }
	    
	    Context ctx = ContextManagerFactory.getInstance().getContext();

	    Id userId = ctx.getUserId();
	    
	    try
	    {
	      CourseDbLoader crsLoader = CourseDbLoader.Default.getInstance();
	      
	      List<Course> crsList = crsLoader.loadByUserId( userId );
	      List<Course> crsDisplayList = new ArrayList<Course>();
	      
	      for (Course c: crsList){
	    	  if (c.getEnrollmentType() == Course.Enrollment.SELF_ENROLLMENT)
	    	  {
	    		  crsDisplayList.add(c);
	    	  }
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
	}
	
	

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