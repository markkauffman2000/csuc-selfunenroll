package edu.csuchico.audit;
// Not using this for the moment until we decide whether annotations or xml config are better.
// Currently using xml config for this bean for our code.  
import org.springframework.context.annotation.*;

@Configuration
public class AuditConfig {
	@Bean
	public AuditDAO auditDAO() {
		return new AuditDAO();
	}// auditDAO()
}
