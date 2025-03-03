package org.example.com.family.accounting.repository;

import jakarta.annotation.Nullable;
import org.example.com.family.accounting.domain.object.Family;

/**
 * Interface that provides access to different actions with Family objects.
 */
public interface FamilyRepository {
    /**
     * Returns family object by the unique family name
     * or null value if there is no family with a such name.
     *
     * @param name the unique family name
     * @return family object
     */
    @Nullable
    Family getFamilyByName(String name);

    /**
     * Adds new family.
     *
     * @param family the new family object that should be added
     */
    void addNewFamily(Family family);
}
