package idv.shawnyang.repos;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import idv.shawnyang.entity.User;

@Repository
public interface UserRepos extends PagingAndSortingRepository<User, Long> {

}
