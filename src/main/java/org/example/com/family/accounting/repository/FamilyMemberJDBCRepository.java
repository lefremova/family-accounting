package org.example.com.family.accounting.repository;

import org.example.com.family.accounting.domain.object.Family;
import org.example.com.family.accounting.domain.object.FamilyMember;
import org.example.com.family.accounting.utils.Gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

/**
 * This class provides access to different actions with Family Member objects
 * using Spring JDBC / JDBC Template mechanism.
 */
@Repository
public class FamilyMemberJDBCRepository implements FamilyMemberRepository {
    private static final Logger LOG = LoggerFactory.getLogger(FamilyMemberJDBCRepository.class);

    private static final String FAMILY_MEMBER_ID_COLUMN = "family_member_id";
    private static final String FAMILY_ID_COLUMN = "family_id";
    private static final String NAME_COLUMN = "name";
    private static final String SURNAME_COLUMN = "surname";
    private static final String GENDER_COLUMN = "gender";
    private static final String BIRTHDAY_COLUMN = "birthday";

    private static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd";
    private static final String GET_FAMILY_MEMBERS_BY_FAMILY_NAME_QUERY_TEMPLATE =
                                                            "SELECT * FROM families_members WHERE family_id = ?";
    private static final String INSERT_NEW_FAMILY_MEMBER_QUERY_TEMPLATE =
              "INSERT INTO families_members (family_id, name, surname, gender, birthday) VALUES (?, ?, ?, ?, ?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    static class FamilyMemberRowMapper implements RowMapper<FamilyMember> {
        @Override
        public FamilyMember mapRow(ResultSet rs, int rowNum) throws SQLException {
            FamilyMember familyMember = new FamilyMember();
            familyMember.setId(rs.getLong(FAMILY_MEMBER_ID_COLUMN));
            familyMember.setFamilyId(rs.getLong(FAMILY_ID_COLUMN));
            familyMember.setName(rs.getString(NAME_COLUMN));
            familyMember.setSurname(rs.getString(SURNAME_COLUMN));
            familyMember.setGender(Gender.fromString(rs.getString(GENDER_COLUMN).toUpperCase()));
            familyMember.setBirthday(rs.getDate(BIRTHDAY_COLUMN));
            return familyMember;
        }

    }

    @Autowired
    private FamilyRepository familyRepository;

    @Override
    public List<FamilyMember> getAllFamilyMembers(String familyName) {
        LOG.debug("Try to get all family members for family with name: " + familyName + " from database");

        Optional<Family> familyOptional = Optional.ofNullable(familyRepository.getFamilyByName(familyName));
        if (familyOptional.isPresent()) {
            LOG.debug("Family with name: " + familyName + " is presented in database");

            var family = familyOptional.get();
            var familyID = family.getId();
            return jdbcTemplate.query(GET_FAMILY_MEMBERS_BY_FAMILY_NAME_QUERY_TEMPLATE,
                                                new FamilyMemberRowMapper(), familyID);
        } else {
            LOG.debug("Family with name: " + familyName + " is not presented in database");
            return List.of();
        }
    }

    @Override
    @Transactional
    public void addFamilyMember(String familyName, FamilyMember familyMember) {
        LOG.debug("Try to add family member with name: " + familyMember.getName() + " " +
                familyMember.getSurname() + " for family with name: " + familyName + " to database");
        Family family = createFamilyIfNotExists(familyName);

        String birthdayString = new SimpleDateFormat(DATABASE_DATE_FORMAT).format(familyMember.getBirthday());

        jdbcTemplate.update(INSERT_NEW_FAMILY_MEMBER_QUERY_TEMPLATE,
                family.getId(), familyMember.getName(), familyMember.getSurname(),
                familyMember.getGender().toString(), birthdayString);
    }

    private Family createFamilyIfNotExists(String familyName) {
        Optional<Family> familyOptional = Optional.ofNullable(familyRepository.getFamilyByName(familyName));

        if (familyOptional.isPresent()) {
            LOG.debug("Family with name: " + familyName + " already exists in database");
            return familyOptional.get();
        } else {
            LOG.debug("Family with name: " + familyName + " does not exist in database");
            var familyToAdd = new Family();
            familyToAdd.setFamilyName(familyName);
            familyRepository.addNewFamily(familyToAdd);

            return familyRepository.getFamilyByName(familyName);
        }
    }

}
