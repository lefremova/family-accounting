package org.example.com.family.accounting.repository;

import org.example.com.family.accounting.domain.object.Family;
import org.example.com.family.accounting.exceptions.DuplicateObjectException;

import java.util.Optional;

/**
 * Interface that provides access to different actions with Family objects.
 */
public interface FamilyRepository {
    /**
     * Returns family object by the unique family name.
     *
     * @param name the unique family name
     * @return family object
     */
    Optional<Family> getFamilyByName(String name);

    /**
     * Adds new family.
     *
     * @param family the new family object that should be added
     * @throws DuplicateObjectException if the family object with the unique family name parameter already exists
     */
    void addNewFamily(Family family) throws DuplicateObjectException;
}
