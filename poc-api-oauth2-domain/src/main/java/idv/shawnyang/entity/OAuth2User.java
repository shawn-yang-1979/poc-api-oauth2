package idv.shawnyang.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OAuth2User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	private String oauth2ProviderId;
	@NotBlank
	private String oauth2ProviderUsername;
	@ManyToOne
	@NotNull
	private User user;

	public String getOauth2Username() {
		return this.oauth2ProviderId + ":" + this.oauth2ProviderUsername;
	}

}
