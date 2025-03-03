package org.example.com.family.accounting.domain.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.com.family.accounting.utils.Gender;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamilyMember {
    private long id;
    private long familyId;
    private String name;
    private String surname;
    private Gender gender;
    private Date birthday;
}
