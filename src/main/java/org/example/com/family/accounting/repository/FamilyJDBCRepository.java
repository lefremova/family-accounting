package org.example.com.family.accounting.repository;

import org.example.com.family.accounting.domain.object.Family;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * This class provides access to different actions with Family objects
 * using Spring JDBC / JDBC Template mechanism.
 */
@Repository
public class FamilyJDBCRepository implements FamilyRepository {
    private static final Logger LOG = LoggerFactory.getLogger(FamilyJDBCRepository.class);

    private static final String GET_FAMILY_BY_NAME_QUERY_TEMPLATE = "SELECT * FROM families WHERE family_name=?";
    private static final String INSERT_NEW_FAMILY_QUERY_TEMPLATE = "INSERT INTO families (family_name) VALUES (?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Family getFamilyByName(String familyName) {
        try {
            LOG.debug("Try to get family object by family name: " + familyName + " from database");
            return jdbcTemplate.queryForObject(GET_FAMILY_BY_NAME_QUERY_TEMPLATE,
                                                        new BeanPropertyRowMapper<>(Family.class), familyName);
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("There is no family with family name: " + familyName + " in database");
            return null;
        }
    }

    @Override
    public void addNewFamily(Family family) {
        LOG.debug("Try to add new family with family name: " + family.getFamilyName());
        jdbcTemplate.update(INSERT_NEW_FAMILY_QUERY_TEMPLATE, family.getFamilyName());
    }
}
