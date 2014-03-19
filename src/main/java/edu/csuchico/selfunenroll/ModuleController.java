package edu.csuchico.selfunenroll;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

// import com.blackboard.consulting.web.ModuleController;

import blackboard.data.ReceiptOptions;
import blackboard.data.course.Course;
import blackboard.persist.Id;
import blackboard.persist.course.CourseDbLoader;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManagerFactory; 
import blackboard.platform.servlet.InlineReceiptUtil;
import blackboard.portal.external.CustomData;
import blackboard.util.UrlUtil;


@Controller
@RequestMapping( value = "/modules" )
public class ModuleController {
	private static final Logger log = LoggerFactory.getLogger(ModuleController.class);

	/*
	* ACTION TO LOAD Module View Page
	*/
	@RequestMapping(value = { "view" }, method = RequestMethod.GET)
	public ModelAndView view(HttpServletRequest request,
		HttpServletResponse response, ModelMap model) {
		
		log.debug("Starting view Module target.....");
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
	      
	      //Load module personalization data
	      //CustomData cd = CustomData.getModulePersonalizationData( request );
	      //Read settings
	      // Boolean showCourseName = (Boolean) cd.getObjectValue("showCourseName");
	      
	      model.addAttribute( "courses", crsList );
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