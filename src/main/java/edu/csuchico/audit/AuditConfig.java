package edu.csuchico.audit;
// Not using this for the moment until we decide whether annotations or xml config are better.
// We're going to use xml bean config first.
import org.springframework.context.annotation.*;

@Configuration
public class AuditConfig {
	@Bean
	public AuditDAO auditDAO() {
		return new AuditDAO();
	}// auditDAO()
}
