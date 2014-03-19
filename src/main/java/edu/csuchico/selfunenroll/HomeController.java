package edu.csuchico.selfunenroll;

import edu.csuchico.audit.*;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
// The following will only be used if we decide to use annotation configuration.
// import org.springframework.context.annotation.AnnotationConfigApplicationContext;
// First we try the ClassPathXmlApplicationContext and using xml configuration
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = {"/", "showtime"}, method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		/***  We'll need the following if we decided to use annotations over xml Bean configuration 
		ApplicationContext ctx = new AnnotationConfigApplicationContext(AuditConfig.class);
		AuditDAO ado = ctx.getBean(AuditDAO.class);
		***/
		
		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		AuditDAO ado = (AuditDAO) context.getBean("auditDAO");
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		AuditBean auditBean = new AuditBean("selfunenroll", "something happened");
		
		
		ado.save(auditBean);
	
		return "home";
	}// home
	
}// class HomeController
