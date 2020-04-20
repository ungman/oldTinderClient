package io.github.ungman.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@JsonIgnoreProperties
public class User {
    private Long idUser;
    private String username;
    private String password;
}
