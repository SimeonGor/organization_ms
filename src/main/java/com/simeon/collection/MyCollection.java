package com.simeon.collection;


import com.simeon.collection.element.Organization;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Vector;

@ToString
public class MyCollection {
    @Getter
    @Setter
    @PastOrPresent
    private LocalDate creationDate;
    @Getter
    @Setter
    @PastOrPresent
    private LocalDate modifiedDate;

    @Getter
    @Valid
    protected final Vector<Organization> collection = new Vector<>();

    public void add(Organization e) {
        collection.add(e);
    }

    @Override
    public String toString() {
        if (this.collection.isEmpty()) return "Empty collection";
        String template = "%4s| %-4s| %-10s| %3s| %-6s| %-10s| %-15s| %-10s| %-9s";
        String header = String.format("%4s| %-4s| %-10s| %-11s| %-10s| %-15s| %-10s| %-9s",
                "#",
                "id",
                "name",
                "coordinates",
                "date",
                "annual turnover",
                "type",
                "zip code");

        StringBuilder result = new StringBuilder();
        result.append(header).append("\n");
        int i = 1;
        for (var e : collection) {
            String zipcode = "";
            if (e.getPostalAddress() != null && e.getPostalAddress().getZipCode() != null) {
                zipcode = e.getPostalAddress().getZipCode();
            }
            result.append(String.format(template,
                            i++,
                            e.getId(),
                            e.getName(),
                            e.getCoordinates().getX(),
                            e.getCoordinates().getY(),
                            e.getCreationDate(),
                            e.getAnnualTurnover(),
                            e.getType(),
                            zipcode))
                    .append("\n");
        }
        return result.substring(0, result.length() - 1);
    }
}
