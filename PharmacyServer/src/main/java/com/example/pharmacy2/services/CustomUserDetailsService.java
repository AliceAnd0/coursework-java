package com.example.pharmacy2.services;
import com.example.pharmacy2.models.AppUser;
import org.springframework.security.core.userdetails.User;
import com.example.pharmacy2.repositories.EmployeeRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Сотрудник не найден: " + username));

        // Администратор всегда имеет роль ADMIN
        if (user.getUsername().equals("admin")) {
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles("ADMIN")
                    .build();
        }

        // Все остальные сотрудники имеют роль USER
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER")  // Роль для сотрудника
                .build();
    }
}
