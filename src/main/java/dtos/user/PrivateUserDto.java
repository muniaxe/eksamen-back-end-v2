package dtos.user;

import entities.Role;
import entities.User;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * PrivateUserDTO is for transferring sensitive data about a user.
 * This data should only ever be requested by the user itself or in some cases, an admin.
 *
 * */

@Data
public class PrivateUserDto {
    private String username;
    private List<String> roles;

    public PrivateUserDto(User user) {
        this.username = user.getUsername();
        this.roles = user.getRoles().stream().map(Object::toString).collect(Collectors.toList());
    }

    public List<String> getRolesAsStrings() {
        return roles.isEmpty() ? null : roles.stream().map(Object::toString).collect(Collectors.toList());
    }
}