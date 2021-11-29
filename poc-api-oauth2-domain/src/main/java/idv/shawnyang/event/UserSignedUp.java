package idv.shawnyang.event;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;

@Getter
public class UserSignedUp {

	private Instant occurred = Instant.now();
	private UUID id = UUID.randomUUID();

	private Instant entered;
	private String enteredBy;

	private String email;
	private String oauth2ProviderId;
	private String oauth2ProviderUsername;

	public UserSignedUp(String email, String oauth2ProviderId, String oauth2ProviderUsername) {
		super();
		this.entered = Instant.now();
		this.enteredBy = email;

		this.email = email;
		this.oauth2ProviderId = oauth2ProviderId;
		this.oauth2ProviderUsername = oauth2ProviderUsername;
	}

}
