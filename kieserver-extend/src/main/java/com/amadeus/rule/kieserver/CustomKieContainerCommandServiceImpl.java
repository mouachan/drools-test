package com.amadeus.rule.kieserver;

import java.util.List;

import org.drools.core.command.runtime.BatchExecutionCommandImpl;
import org.drools.core.command.runtime.rule.FireAllRulesCommand;
import org.drools.core.command.runtime.rule.InsertObjectCommand;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.ExecutionResults;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.services.api.KieServerRegistry;
import org.kie.server.services.drools.RulesExecutionService;
import org.kie.server.services.impl.KieContainerCommandServiceImpl;
import org.kie.server.services.impl.KieContainerInstanceImpl;
import org.kie.server.services.impl.KieServerImpl;
import org.kie.server.services.impl.locator.ContainerLocatorProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amadeus.rule.model.Fact;

public class CustomKieContainerCommandServiceImpl extends KieContainerCommandServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(CustomKieContainerCommandServiceImpl.class);

	private RulesExecutionService rulesExecutionService;

	public CustomKieContainerCommandServiceImpl(KieServerImpl kieServer, KieServerRegistry context,
			RulesExecutionService rulesExecutionService) {
		super(kieServer, context);
		this.rulesExecutionService = rulesExecutionService;
	}

	protected void addFactToCommand(String containerId, Fact fact, BatchExecutionCommandImpl cmd) throws InstantiationException, IllegalAccessException {
		
		FactType objType = context.getContainer(containerId).getKieContainer().getKieBase()
				.getFactType(fact.getNamespace(), fact.getName());

		Object obj = objType.newInstance();
		for (String attrname : fact.getAttributes().keySet()) {
			Object value = fact.getAttributes().get(attrname);
			if (value instanceof String)
				objType.set(obj, attrname, value);
		}
		cmd.addCommand(new InsertObjectCommand(obj, fact.getIdentifier()));
	}

	public ServiceResponse<ExecutionResults> callContainer(String containerId, List<Fact> facts) {

		try {
			KieContainerInstanceImpl kci = (KieContainerInstanceImpl) context.getContainer(containerId);
			BatchExecutionCommandImpl cmds = new BatchExecutionCommandImpl();
			for (Fact fact : facts) {
				addFactToCommand(containerId, fact, cmds);
			}
			cmds.addCommand(new FireAllRulesCommand(containerId));

			if (kci != null && kci.getKieContainer() != null) {
				ExecutionResults results = rulesExecutionService.call(kci, cmds);
				return new ServiceResponse<ExecutionResults>(ServiceResponse.ResponseType.SUCCESS,
						"Container " + containerId + " successfully called.", results);
			} else {
				return new ServiceResponse<ExecutionResults>(ServiceResponse.ResponseType.FAILURE,
						"Container " + containerId + " is not instantiated.");
			}

		} catch (Exception e) {
			logger.error("Error calling container '" + containerId + "'", e);
			return new ServiceResponse<ExecutionResults>(ServiceResponse.ResponseType.FAILURE,
					"Error calling container " + containerId + ": " + e.getMessage());
		}
	}
	
	public ServiceResponse<ExecutionResults> callContainerByAlias(String alias, List<Fact> facts) {
		String containerId = null;
		try {
			
			KieContainerInstanceImpl kci = (KieContainerInstanceImpl) context.getContainer(alias, ContainerLocatorProvider.get().getLocator());
			containerId = kci.getKieContainer().getContainerId();
			BatchExecutionCommandImpl cmds = new BatchExecutionCommandImpl();
			for (Fact fact : facts) {
				addFactToCommand(containerId, fact, cmds);
			}
			cmds.addCommand(new FireAllRulesCommand(alias));
			if (kci != null && kci.getKieContainer() != null) {
				ExecutionResults results = rulesExecutionService.call(kci, cmds);
				return new ServiceResponse<ExecutionResults>(ServiceResponse.ResponseType.SUCCESS,
						"Container " + containerId + " successfully called.", results);
			} else {
				return new ServiceResponse<ExecutionResults>(ServiceResponse.ResponseType.FAILURE,
						"Container " + containerId + " is not instantiated.");
			}

		} catch (Exception e) {
			logger.error("Error calling container '" + containerId + "'", e);
			return new ServiceResponse<ExecutionResults>(ServiceResponse.ResponseType.FAILURE,
					"Error calling container " + containerId + ": " + e.getMessage());
		}
	}
}
