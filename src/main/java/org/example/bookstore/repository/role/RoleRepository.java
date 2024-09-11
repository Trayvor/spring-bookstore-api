package org.example.bookstore.repository.role;

import java.util.Optional;
import org.example.bookstore.model.Role;
import org.example.bookstore.model.constant.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}
