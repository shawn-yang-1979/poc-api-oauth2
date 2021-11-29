package idv.shawnyang.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import idv.shawnyang.event.UserSignedUp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DomainEventListener {

	@TransactionalEventListener
	public void onMyDomainEvent(UserSignedUp event) {
		log.info(event.getEmail());
	}
}
