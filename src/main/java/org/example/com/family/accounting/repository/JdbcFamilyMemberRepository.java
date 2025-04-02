package org.example.com.family.accounting.repository;

import org.example.com.family.accounting.domain.object.FamilyMember;
import org.example.com.family.accounting.exceptions.DuplicateObjectException;
import org.example.com.family.accounting.utils.Gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * This class provides access to different actions with Family Member objects
 * using Spring JDBC / JDBC Template mechanism.
 */
@Repository
public class JdbcFamilyMemberRepository implements FamilyMemberRepository {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcFamilyMemberRepository.class);

    private static final String FAMILY_MEMBER_ID_COLUMN = "family_member_id";
    private static final String NAME_COLUMN = "name";
    private static final String SURNAME_COLUMN = "surname";
    private static final String GENDER_COLUMN = "gender";
    private static final String BIRTHDAY_COLUMN = "birthday";

    private static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd";

    @Autowired
    private NamedParameterJdbcOperations namedJdbcTemplate;

    @Override
    public List<FamilyMember> getAllFamilyMembers(long familyID) {
        LOG.debug("Try to get all family members for family with id: " + familyID + " from database");

        String sqlStatement = "SELECT family_member_id, name, surname, gender, birthday " +
                                                            "FROM families_members WHERE family_id = :family_id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("family_id", familyID);
        return namedJdbcTemplate.query(sqlStatement, namedParameters, new FamilyMemberRowMapper());
    }

    @Override
    public void addFamilyMember(FamilyMember familyMember) throws DuplicateObjectException {
        String birthdayString = new SimpleDateFormat(DATABASE_DATE_FORMAT).format(familyMember.getBirthday());

        String sqlStatement = "INSERT INTO families_members (family_id, name, surname, gender, birthday) " +
                                                            "VALUES (:family_id, :name, :surname, :gender, :birthday)";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                                                    .addValue("family_id", familyMember.getFamily().getId())
                                                    .addValue("name", familyMember.getName())
                                                    .addValue("surname", familyMember.getSurname())
                                                    .addValue("gender", familyMember.getGender().toString())
                                                    .addValue("birthday", birthdayString);
        try {
            namedJdbcTemplate.update(sqlStatement, namedParameters);
        } catch (DuplicateKeyException ex) {
            throw new DuplicateObjectException(ex.getMessage());
        }
    }

    private static class FamilyMemberRowMapper implements RowMapper<FamilyMember> {
        @Override
        public FamilyMember mapRow(ResultSet rs, int rowNum) throws SQLException {
            FamilyMember familyMember = new FamilyMember();
            familyMember.setId(rs.getLong(FAMILY_MEMBER_ID_COLUMN));
            familyMember.setName(rs.getString(NAME_COLUMN));
            familyMember.setSurname(rs.getString(SURNAME_COLUMN));
            familyMember.setGender(Gender.fromString(rs.getString(GENDER_COLUMN).toUpperCase()));
            familyMember.setBirthday(rs.getDate(BIRTHDAY_COLUMN));
            return familyMember;
        }
    }
}
