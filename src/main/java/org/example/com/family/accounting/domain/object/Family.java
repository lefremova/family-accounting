package org.example.com.family.accounting.domain.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Family {
    private long id;
    private String familyName;
}
