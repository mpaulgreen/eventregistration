package mpaugreen.keycloak.requiredaction;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

/**
 * @author Mriganka Paul
 */
public class CreateUserEventListenerProviderFactory implements EventListenerProviderFactory {
	public EventListenerProvider create(KeycloakSession session) {
		return new CreateUserEventListenerProvider(session);
	}

	@Override
	public void init(Config.Scope config) {
	}

	@Override
	public void postInit(KeycloakSessionFactory factory) {

	}

	@Override
	public void close() {

	}

	@Override
	public String getId() {
		return "create-user";
	}

	@Override
	public int order() {
		return EventListenerProviderFactory.super.order();
	}
}
