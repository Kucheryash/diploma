package project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Market {
    private int id;

    private String name;

    private String revenue22;

    private String revenue21;

    private String growthRev;

    private String employees22;

    private String employees21;

    private String growthEmpl;

    private String activity;
}
