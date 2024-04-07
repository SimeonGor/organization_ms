package com.simeon.collection;


import com.simeon.element.Organization;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Vector;

@Getter
public class MyCollection {
    @Setter
    @NotNull
    @PastOrPresent
    private LocalDate creationDate;
    @Setter
    @NotNull
    @PastOrPresent
    private LocalDate modifiedDate;

    @Valid
    protected final Vector<Organization> collection = new Vector<>();

    public void add(Organization e) {
        collection.add(e);
    }
}
