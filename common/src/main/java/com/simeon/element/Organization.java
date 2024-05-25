package com.simeon.element;

import com.simeon.UserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;


/**
 * Organization
 */
@Getter
@Builder
@ToString
public class Organization implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;

    @Setter
    @NotNull
    @Min(value=0)
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @NotNull
    @NotBlank(message="The name should not be empty")
    private String name; //Поле не может быть null, Строка не может быть пустой
    @NotNull
    @Valid
    private Coordinates coordinates; //Поле не может быть null

    @NotNull
    @Past
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @NotNull
    @AnnualTurnoverConstraint
    private Double annualTurnover; //Поле не может быть null, Значение поля должно быть больше 0
    @NotNull(message="Invalid organization type")
    private OrganizationType type; //Поле не может быть null
    @Valid
    private Address postalAddress; //Поле может быть null

    @Setter
    @NotNull
    private UserInfo userInfo;
}
