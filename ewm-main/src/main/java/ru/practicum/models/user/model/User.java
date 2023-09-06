package ru.practicum.models.user.model;

import lombok.Data;
import ru.practicum.models.user.dto.NestedUserDTO;
import ru.practicum.models.user.dto.RequestUserDTO;
import ru.practicum.models.user.dto.ResponseUserDTO;

import javax.persistence.*;

@Data
@Entity
@Table(name="ewm_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    public static User fromDTO(RequestUserDTO dto) {
        User user = new User();
        user.name = dto.getName();
        user.email = dto.getEmail();
        return user;
    }

    public ResponseUserDTO toDTO() {
        return new ResponseUserDTO(
            this.id,
            this.name,
            this.email
        );
    }

    public NestedUserDTO toNestedDTO() {
        return new NestedUserDTO(
                this.id,
                this.name
        );
    }
}
