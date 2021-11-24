package idv.shawnyang.repos;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import idv.shawnyang.entity.User;

@Repository
public interface UserRepos extends PagingAndSortingRepository<User, Long> {

	Optional<User> findByUsername(String username);

}
