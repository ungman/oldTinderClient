package io.github.ungman.pojo;

import lombok.Data;

@Data
public class Profile {
    private Long idUser;
    private String name;
    private String gender;
    private String description;
    private Long idShown;

}
