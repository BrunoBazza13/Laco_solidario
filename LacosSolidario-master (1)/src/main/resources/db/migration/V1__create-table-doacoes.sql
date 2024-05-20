create table doacoes(

    id bigint not null auto_increment,
    tipoDeDoacao varchar(100) not null,
    descricao varchar(100),
    data varchar(10) not null,
    status bit(20) not null,


    primary key(id)

);