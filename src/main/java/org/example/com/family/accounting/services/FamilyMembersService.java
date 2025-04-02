package org.example.com.family.accounting.services;

import org.example.com.family.accounting.domain.object.Family;
import org.example.com.family.accounting.domain.object.FamilyMember;
import org.example.com.family.accounting.exceptions.DuplicateObjectException;
import org.example.com.family.accounting.repository.FamilyMemberRepository;
import org.example.com.family.accounting.repository.FamilyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FamilyMembersService {
    private final FamilyRepository familyRepository;
    private final FamilyMemberRepository familyMemberRepository;

    public FamilyMembersService(FamilyRepository familyRepository, FamilyMemberRepository familyMemberRepository) {
        this.familyRepository = familyRepository;
        this.familyMemberRepository = familyMemberRepository;
    }

    @Transactional(readOnly = true)
    public List<FamilyMember> getAllFamilyMembers(String familyName) {
        Optional<Family> familyOptional = familyRepository.getFamilyByName(familyName);
        if (familyOptional.isPresent()) {
            List<FamilyMember> familyMembers = familyMemberRepository.getAllFamilyMembers(familyOptional.get().getId());
            familyMembers.forEach(member -> member.setFamily(familyOptional.get()));
            return familyMembers;
        } else {
            return List.of();
        }
    }

    @Transactional
    public void addFamilyMember(String familyName, FamilyMember familyMember) throws DuplicateObjectException {
        Family createdOrRetrievedFamily = createFamilyIfNotExists(familyName);
        familyMember.setFamily(createdOrRetrievedFamily);
        familyMemberRepository.addFamilyMember(familyMember);
    }

    private Family createFamilyIfNotExists(String familyName) throws DuplicateObjectException {
        Optional<Family> familyOptional = familyRepository.getFamilyByName(familyName);

        if (familyOptional.isPresent()) {
            return familyOptional.get();
        } else {
            var familyToAdd = new Family();
            familyToAdd.setFamilyName(familyName);
            familyRepository.addNewFamily(familyToAdd);

            return familyRepository.getFamilyByName(familyName).get();
        }
    }
}
