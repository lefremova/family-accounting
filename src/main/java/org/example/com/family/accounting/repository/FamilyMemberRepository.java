package org.example.com.family.accounting.repository;

import org.example.com.family.accounting.domain.object.FamilyMember;

import java.util.List;

/**
 * Interface that provides access to different actions with Family Members.
 */
public interface FamilyMemberRepository {
    /**
     * Returns all family members of the defined family.
     *
     * @param familyName the unique family name
     * @return the list of all family members
     */
    List<FamilyMember> getAllFamilyMembers(String familyName);

    /**
     * Adds family member to the defined family.
     * If there is no such family, adds new family before.
     *
     * @param familyName the unique family name
     * @param familyMember family member that should be added to the defined family
     */
    void addFamilyMember(String familyName, FamilyMember familyMember);
}
