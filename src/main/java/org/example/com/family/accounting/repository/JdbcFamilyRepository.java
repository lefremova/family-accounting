package org.example.com.family.accounting.repository;

import org.example.com.family.accounting.domain.object.Family;
import org.example.com.family.accounting.exceptions.DuplicateObjectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This class provides access to different actions with Family objects
 * using Spring JDBC / JDBC Template mechanism.
 */
@Repository
public class JdbcFamilyRepository implements FamilyRepository {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcFamilyRepository.class);

    @Autowired
    private NamedParameterJdbcOperations namedJdbcTemplate;

    @Override
    public Optional<Family> getFamilyByName(String familyName) {
        try {
            LOG.debug("Try to get family object by family name: " + familyName + " from database");

            String sqlStatement = "SELECT id, family_name FROM families WHERE family_name = :family_name";
            SqlParameterSource namedParameters = new MapSqlParameterSource("family_name", familyName);
            return Optional.ofNullable(namedJdbcTemplate.queryForObject(sqlStatement, namedParameters,
                                                                        new BeanPropertyRowMapper<>(Family.class)));
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("There is no family with family name: " + familyName + " in database");
            return Optional.empty();
        }
    }

    @Override
    public void addNewFamily(Family family) throws DuplicateObjectException {
        String familyName = family.getFamilyName();

        LOG.debug("Try to add new family with family name: " + familyName);

        String sqlStatement = "INSERT INTO families (family_name) VALUES (:family_name)";
        SqlParameterSource namedParameters = new MapSqlParameterSource("family_name", familyName);
        try {
            namedJdbcTemplate.update(sqlStatement, namedParameters);
        } catch (DuplicateKeyException ex) {
            throw new DuplicateObjectException(ex.getMessage());
        }
    }
}
