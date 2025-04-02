package org.example.com.family.accounting.repository;

import org.example.com.family.accounting.domain.object.FamilyMember;
import org.example.com.family.accounting.exceptions.DuplicateObjectException;

import java.util.List;

/**
 * Interface that provides access to different actions with Family Members.
 */
public interface FamilyMemberRepository {
    /**
     * Returns all family members of the defined family.
     *
     * @param familyID the family identifier
     * @return the list of all family members corresponding to the family with the provided id or an empty list
     */
    List<FamilyMember> getAllFamilyMembers(long familyID);

    /**
     * Adds the defined family member.
     *
     * @param familyMember family member that should be added
     * @throws DuplicateObjectException if family member
     * with the unique group of parameters already exists
     */
    void addFamilyMember(FamilyMember familyMember) throws DuplicateObjectException;
}
