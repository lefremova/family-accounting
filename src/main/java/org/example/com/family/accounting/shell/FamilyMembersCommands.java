package org.example.com.family.accounting.shell;

import org.example.com.family.accounting.domain.object.FamilyMember;
import org.example.com.family.accounting.repository.FamilyMemberRepository;
import org.example.com.family.accounting.utils.Gender;
import org.jline.reader.LineReader;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * This class is responsible for shell commands
 * that works with family/family members.
 */
@ShellComponent
public class FamilyMembersCommands {
    @Autowired
    @Lazy
    private LineReader lineReader;

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @ShellMethod(key = "вывести-членов-семьи",
                 value = "Вывести всех членов семьи в виде таблицы." +
                         "В качестве обязательного параметра указывается название семьи.")
    public Table listFamilyMembers(@ShellOption(value = "название_семьи") String familyName) {
        List<FamilyMember> familyMembers = familyMemberRepository.getAllFamilyMembers(familyName);

        var model = new BeanListTableModel<>(familyMembers, crerateFamilyMembersTableHeaders());

        var tableBuilder = new TableBuilder(model);
        tableBuilder.addInnerBorder(BorderStyle.fancy_light);
        tableBuilder.addHeaderBorder(BorderStyle.fancy_double);

        return tableBuilder.build();
    }

    private LinkedHashMap<String, Object> crerateFamilyMembersTableHeaders() {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("name", "имя");
        headers.put("surname", "фамилия");
        headers.put("gender", "пол");
        headers.put("birthday", "дата рождения");
        return headers;
    }

    @ShellMethod(key = "добавить-члена-семьи",
                 value = "Добавить члена семьи. В качестве обязательного параметра указывается название семьи")
    public String addFamilyMember(@ShellOption(value = "название_семьи") String familyName) {
        FamilyMember familyMember = new FamilyMember();
        familyMember.setName(lineReader.readLine("Введите имя: "));
        familyMember.setSurname(lineReader.readLine("Введите фамилию: "));

        String inputGender = lineReader.readLine("Введите пол (M/Ж): ");
        try {
            familyMember.setGender(Gender.fromString(inputGender));
        } catch (IllegalArgumentException ex) {
            return "Введено некорректное значение для пола.";
        }

        String birthdayString = lineReader.readLine("Введите дату рождения (ГГГГ-MM-ДД): ");
        try {
            LocalDate birthdayParsedFromString = LocalDate.parse(birthdayString);
            Date birthday = Date.from(birthdayParsedFromString.atStartOfDay(ZoneId.systemDefault()).toInstant());
            familyMember.setBirthday(birthday);
        } catch (DateTimeParseException ex) {
            return "Введен некорректный формат даты рождения.";
        }

        familyMemberRepository.addFamilyMember(familyName, familyMember);
        return "Член семьи успешно добавлен в семью " + familyName;
    }
}
