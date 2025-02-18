// Use DBML to define your database structure
// Docs: https://dbml.dbdiagram.io/docs

Table families {
    id bigint [primary key]
    family_name varchar(255) [not null, unique]
}

Table families_members {
    person_id bigint [primary key]
    family_id bigint [ref: > families.id]
    name varchar(255) [not null]
    surname varchar(255) [not null]
    gender varchar(1) [not null]
    birthday date [not null]
}

Table incomes {
    income_id bigint [primary key]
    sum numeric(14,2) [not null]
    person_id bigint [ref: > families_members.person_id]
    comment text
    timestamp timestamp [not null]
}

Table expenses {
    expense_id bigint [primary key]
    sum decimal(14,2) [not null]
    person_id bigint [ref: > families_members.person_id]
    category_id integer [ref: - category.category_id]
    timestamp timestamp [not null]
}

Table category {
    category_id integer [primary key]
    category_name varchar(255) [not null, unique]
}
