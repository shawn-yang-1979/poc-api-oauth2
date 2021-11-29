package idv.shawnyang.entity;

import java.util.Collection;
import java.util.LinkedList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.util.Assert;

import idv.shawnyang.event.UserSignedUp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User extends AbstractAggregateRoot<User> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	private String email;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
	private Collection<OAuth2User> oauth2Users = new LinkedList<>();

	public void addOAuth2User(OAuth2User oauth2User) {
		Assert.notNull(oauth2User, OAuth2User.class.getCanonicalName() + " must not be null");
		Assert.isNull(oauth2User.getId(), OAuth2User.class.getCanonicalName() + " must be a new object.");
		Assert.state(
				this.oauth2Users.stream().noneMatch(e -> e.getOauth2Username().equals(oauth2User.getOauth2Username())),
				OAuth2User.class.getCanonicalName() + " had been signed up.");
		this.oauth2Users.add(oauth2User);
		oauth2User.setUser(this);
		super.registerEvent(
				new UserSignedUp(email, oauth2User.getOauth2ProviderId(), oauth2User.getOauth2ProviderUsername()));
	}

}
