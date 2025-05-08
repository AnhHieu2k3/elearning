package utc.k62.cntt5.elearning.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import utc.k62.cntt5.elearning.domain.User;
import utc.k62.cntt5.elearning.exception.ResourceNotFoundException;
import utc.k62.cntt5.elearning.repository.UserRepository;

import javax.transaction.Transactional;

@Service
public class CustomerUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomerUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(userName)
                .filter(User::getActive)
                .stream().findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userName));
//todo return user not active?
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found user with id = " + id)
        );

        return UserPrincipal.create(user);
    }
}
