// Use DBML to define your database structure
// Docs: https://dbml.dbdiagram.io/docs

Table families {
    family_id integer [primary key]
    surname varchar(255) [not null]
}

Table people {
    person_id integer [primary key]
    family_id integer [ref: > families.family_id]
    name varchar(255) [not null]
    gender varchar(1) [not null]
    age integer [not null]
}

Table incomes {
    income_id integer [primary key]
    sum numeric(14,2) [not null]
    person_id integer [ref: > people.person_id]
    comment text
    date date [not null]
}

Table expenses {
    expense_id integer [primary key]
    sum decimal(14,2) [not null]
    person_id integer [ref: > people.person_id]
    category category [default: 'others']
    date date [not null]
}

enum category {
    supermarkets
    clothes
    transfers
    beauty
    pharmacy
    education
    others
}
