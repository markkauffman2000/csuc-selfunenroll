package edu.csuchico.audit;

import org.springframework.context.annotation.Import;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Calendar;
import java.util.List;

import blackboard.data.user.*;
import blackboard.platform.BbServiceManager;
import blackboard.platform.authentication.*;
import blackboard.platform.session.*;
import blackboard.persist.Id;
import blackboard.persist.dao.impl.SimpleDAO;

public class AuditDAO extends SimpleDAO<AuditBean>{
	private static final Logger logger = LoggerFactory.getLogger(AuditDAO.class);
	
	public AuditDAO() {
		super(AuditBean.class);
	}
	
	public AuditDAO(Class<AuditBean> cls) {
		super(cls);
	}
	
	public List<AuditBean> loadAll(){
		return getDAOSupport().loadAll();  // This works because we extended SimpleDAO
	}// loadAll()
	
	public void save(AuditBean ab) {
		BbSession mySession = blackboard.platform.authentication.impl.SessionManagerImpl.getInstance().getContextSession();
		String myUserId = mySession.getUserName();  // getUserName() returns the USER.USER_ID field. 
		
		logger.info("AuditDAO.saving a record for myUserId:"+myUserId+":");
		
		ab.setDatestamp(Calendar.getInstance());
		ab.setUserId(myUserId);
		System.out.println(ab);
		getDAOSupport().persist(ab);
	}// save

} // class AuditDAO
