package mpaugreen.keycloak.requiredaction;

import org.keycloak.email.EmailException;
import org.keycloak.email.EmailTemplateProvider;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.*;


/**
 * @author Mriganka Paul
 */
public class CreateUserEventListenerProvider implements EventListenerProvider {

	private KeycloakSession session;
	public CreateUserEventListenerProvider(KeycloakSession session){
		this.session = session;
	}
	@Override
	public void onEvent(Event event) {
	}

	@Override
	public void onEvent(AdminEvent adminEvent, boolean b) {

		if (!(ResourceType.USER.equals(adminEvent.getResourceType())
				&& OperationType.CREATE.equals(adminEvent.getOperationType()))) {
			return;
		}

		RealmModel realmModel = session.realms().getRealm(adminEvent.getRealmId());
		UserModel user = session.users().getUserById(realmModel,adminEvent.getResourcePath().substring(6));
		EmailTemplateProvider emailTemplateProvider = session.getProvider(EmailTemplateProvider.class);
		Map<String, Object> bodyAttr = new HashMap<>();
		bodyAttr.put("username",user.getFirstAttribute("x-username"));
		bodyAttr.put("password",user.getFirstAttribute("x-password"));
		bodyAttr.put("loginlink",user.getFirstAttribute("x-loginlink"));
		try {
			emailTemplateProvider
					.setRealm(realmModel)
					.setUser(user)
					.setAttribute("username",user.getFirstAttribute("x-username"))
					.setAttribute("password",user.getFirstAttribute("x-password"))
					.setAttribute("loginlink",user.getFirstAttribute("x-loginlink"))
					.send("Welcome to FedRamp keycloak", "password-reset.ftl", bodyAttr);
		} catch (EmailException e) {
			System.out.println("Error occurred while sending email ");
			e.printStackTrace(System.err);
		}
	}

	@Override
	public void close() {

	}

	private String toString(Event event) {

		StringBuilder sb = new StringBuilder();


		sb.append("type=");

		sb.append(event.getType());

		sb.append(", realmId=");

		sb.append(event.getRealmId());

		sb.append(", clientId=");

		sb.append(event.getClientId());

		sb.append(", userId=");

		sb.append(event.getUserId());

		sb.append(", ipAddress=");

		sb.append(event.getIpAddress());


		if (event.getError() != null) {

			sb.append(", error=");

			sb.append(event.getError());

		}


		if (event.getDetails() != null) {

			for (Map.Entry<String, String> e : event.getDetails().entrySet()) {

				sb.append(", ");

				sb.append(e.getKey());

				if (e.getValue() == null || e.getValue().indexOf(' ') == -1) {

					sb.append("=");

					sb.append(e.getValue());

				} else {

					sb.append("='");

					sb.append(e.getValue());

					sb.append("'");

				}

			}

		}


		return sb.toString();

	}

	private String toString(AdminEvent adminEvent) {

		StringBuilder sb = new StringBuilder();


		sb.append("operationType=");

		sb.append(adminEvent.getOperationType());

		sb.append(", realmId=");

		sb.append(adminEvent.getAuthDetails().getRealmId());

		sb.append(", clientId=");

		sb.append(adminEvent.getAuthDetails().getClientId());

		sb.append(", userId=");

		sb.append(adminEvent.getAuthDetails().getUserId());

		sb.append(", ipAddress=");

		sb.append(adminEvent.getAuthDetails().getIpAddress());

		sb.append(", resourcePath=");

		sb.append(adminEvent.getResourcePath());


		if (adminEvent.getError() != null) {

			sb.append(", error=");

			sb.append(adminEvent.getError());

		}


		return sb.toString();

	}
}

