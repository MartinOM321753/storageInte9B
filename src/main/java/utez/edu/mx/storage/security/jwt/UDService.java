package utez.edu.mx.storage.security.jwt;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import utez.edu.mx.storage.modules.user.BeanUser;
import utez.edu.mx.storage.modules.user.UserRpository;

import java.util.Collections;

@Service
public class UDService implements UserDetailsService {

    @Autowired
    private UserRpository userRpository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BeanUser found = userRpository.findByUsername(username).orElse(null);
        if (found == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + found.getRol().getName());

        return new User(
                found.getUsername(),
                found.getPassword(),
                Collections.singleton(authority)

        );
    }
}
