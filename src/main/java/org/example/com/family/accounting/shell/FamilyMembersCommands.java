package org.example.com.family.accounting.shell;

import org.example.com.family.accounting.domain.object.FamilyMember;
import org.example.com.family.accounting.exceptions.DuplicateObjectException;
import org.example.com.family.accounting.services.FamilyMembersService;
import org.example.com.family.accounting.utils.Gender;
import org.jline.reader.LineReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.BeanListTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static org.example.com.family.accounting.utils.MessageSourceHelper.getMessage;

/**
 * This class is responsible for shell commands
 * that works with family/family members.
 */
@ShellComponent
@DependsOn({"messageSourceHelper"})
public class FamilyMembersCommands {
    @Autowired
    @Lazy
    private LineReader lineReader;

    @Autowired
    private FamilyMembersService familyMembersService;

    private static final String FAMILY_MEMBERS_TABLE_NAME = getMessage("family.members.table.name");;
    private static final String FAMILY_MEMBERS_TABLE_SURNAME = getMessage("family.members.table.surname");;
    private static final String FAMILY_MEMBERS_TABLE_GENDER = getMessage("family.members.table.gender");;
    private static final String FAMILY_MEMBERS_TABLE_BIRTHDAY = getMessage("family.members.table.birthday");

    private static final String INPUT_NAME = getMessage("input.name");
    private static final String INPUT_SURNAME = getMessage("input.surname");
    private static final String INPUT_GENDER = getMessage("input.gender");
    private static final String INPUT_BIRTHDAY = getMessage("input.birthday");

    private static final String INPUT_GENDER_ERROR_FORMAT = getMessage("input.gender.error.format");
    private static final String INPUT_BIRTHDAY_ERROR_FORMAT = getMessage("input.birthday.error.format");

    @ShellMethod(key = "вывести-членов-семьи",
                 value = "Вывести всех членов семьи в виде таблицы." +
                         "В качестве обязательного параметра указывается название семьи.")
    public Table listFamilyMembers(@ShellOption(value = "название_семьи") String familyName) {
        List<FamilyMember> familyMembers = familyMembersService.getAllFamilyMembers(familyName);

        var model = new BeanListTableModel<>(familyMembers, crerateFamilyMembersTableHeaders());

        var tableBuilder = new TableBuilder(model);
        tableBuilder.addInnerBorder(BorderStyle.fancy_light);
        tableBuilder.addHeaderBorder(BorderStyle.fancy_double);

        return tableBuilder.build();
    }

    private LinkedHashMap<String, Object> crerateFamilyMembersTableHeaders() {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("name", FAMILY_MEMBERS_TABLE_NAME);
        headers.put("surname", FAMILY_MEMBERS_TABLE_SURNAME);
        headers.put("gender", FAMILY_MEMBERS_TABLE_GENDER);
        headers.put("birthday", FAMILY_MEMBERS_TABLE_BIRTHDAY);
        return headers;
    }

    @ShellMethod(key = "добавить-члена-семьи",
                 value = "Добавить члена семьи. В качестве обязательного параметра указывается название семьи")
    public String addFamilyMember(@ShellOption(value = "название_семьи") String familyName) {
        FamilyMember familyMember = new FamilyMember();
        familyMember.setName(lineReader.readLine(INPUT_NAME + " "));
        familyMember.setSurname(lineReader.readLine(INPUT_SURNAME + " "));

        String inputGender = lineReader.readLine(INPUT_GENDER + " ");
        try {
            familyMember.setGender(Gender.fromString(inputGender));
        } catch (IllegalArgumentException ex) {
            return INPUT_GENDER_ERROR_FORMAT;
        }

        String birthdayString = lineReader.readLine(INPUT_BIRTHDAY + " ");
        try {
            LocalDate birthdayParsedFromString = LocalDate.parse(birthdayString);
            Date birthday = Date.from(birthdayParsedFromString.atStartOfDay(ZoneId.systemDefault()).toInstant());
            familyMember.setBirthday(birthday);
        } catch (DateTimeParseException ex) {
            return INPUT_BIRTHDAY_ERROR_FORMAT;
        }

        try {
            familyMembersService.addFamilyMember(familyName, familyMember);
        } catch (DuplicateObjectException e) {
            return getMessage("add.family.member.error.duplicate", new Object[] {familyName});
        }
        return getMessage("add.family.member.success", new Object[] {familyName});
    }
}
