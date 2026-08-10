insert into uf(descricao, sigla) values('AC', 'Acre');
insert into uf(descricao, sigla) values('AL', 'Alagoas');
insert into uf(descricao, sigla) values('AM', 'Amazonas');
insert into uf(descricao, sigla) values('AP', 'Amapá');
insert into uf(descricao, sigla) values('BA', 'Bahia');
insert into uf(descricao, sigla) values('CE', 'Ceará');
insert into uf(descricao, sigla) values('DF', 'Distrito Federal');
insert into uf(descricao, sigla) values('ES', 'Espírito Santo');
insert into uf(descricao, sigla) values('GO', 'Goiás');
insert into uf(descricao, sigla) values('MA', 'Maranhão');
insert into uf(descricao, sigla) values('MG', 'Minas Gerais');
insert into uf(descricao, sigla) values('MT', 'Mato Grosso');
insert into uf(descricao, sigla) values('MS', 'Mato Grosso do Sul');
insert into uf(descricao, sigla) values('PA', 'Pará');
insert into uf(descricao, sigla) values('PB', 'Paraíba');
insert into uf(descricao, sigla) values('PE', 'Pernambuco');
insert into uf(descricao, sigla) values('PI', 'Piauí');
insert into uf(descricao, sigla) values('PR', 'Paraná');
insert into uf(descricao, sigla) values('RN', 'Rio Grande do Norte');
insert into uf(descricao, sigla) values('RJ', 'Rio de Janeiro');
insert into uf(descricao, sigla) values('RO', 'ROndônia');
insert into uf(descricao, sigla) values('RR', 'Roraima');
insert into uf(descricao, sigla) values('RS', 'Rio Grande do Sul');
insert into uf(descricao, sigla) values('SC', 'Santa Catarina');
insert into uf(descricao, sigla) values('SE', 'Sergipe');
insert into uf(descricao, sigla) values('SP', 'São Paulo');
insert into uf(descricao, sigla) values('TO', 'Tocantins');

insert into mesoregiao(descricao, uf_id) values('X', 1);

insert into microregiao(descricao, mesoregiao_id) values('X',1);

insert into municipio(codigo, descricao, uf_sigla, microregiao_id) values(1, 'Sao Paulo', 'SP', 1); 
insert into municipio(codigo, descricao, uf_sigla, microregiao_id) values(2, 'Rio de Janeiro', 'RJ', 1);
insert into municipio(codigo, descricao, uf_sigla, microregiao_id) values(3, 'Aracaju', 'SE', 1);

insert into distrito(descricao, municipio_id) values('X',1);

insert into categoria(id, descricao) values('05','Cidade');

insert into localidade(descricao, tipo, nivel, categoria_id, distrito_id) values('São Paulo', 'URBANA', 1, '05', 1);

insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('22222222222', 'Mae do Cidadao A', '1998-09-07', 'Cidadao A', 'Pai do Cidadao A', '1523551', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('33333333333', 'Mae do Cidadao B', '1998-09-07', 'Cidadao B', 'Pai do Cidadao B', '1523552', 'M', 2, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('44444444444', 'Mae do Cidadao C', '1998-09-07', 'Cidadao C', 'Pai do Cidadao C', '1523553', 'M', 3, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('55555555555', 'Mae do Cidadao D', '1998-09-07', 'Cidadao D', 'Pai do Cidadao D', '1523554', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('66666666666', 'Mae do Cidadao E', '1998-09-07', 'Cidadao E', 'Pai do Cidadao E', '1523555', 'M', 2, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('77777777777', 'Mae do Cidadao F', '1998-09-07', 'Cidadao F', 'Pai do Cidadao F', '1523556', 'M', 3, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('88888888888', 'Mae do Cidadao G', '1998-09-07', 'Cidadao G', 'Pai do Cidadao G', '1523557', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('11111111111', 'Mae do Cidadao H', '1998-09-07', 'Cidadao H', 'Pai do Cidadao H', '1523558', 'M', 2, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('99999999999', 'Mae do Cidadao I', '1998-09-07', 'Cidadao I', 'Pai do Cidadao I', '1523559', 'M', 3, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('72434701493', 'Mae do Cidadao J', '1998-09-07', 'Cidadao J', 'Pai do Cidadao J', '15235510', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('30239915291', 'Mae do Cidadao K', '1998-09-07', 'Cidadao K', 'Pai do Cidadao K', '15235511', 'M', 2, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('47225231855', 'Mae do Cidadao L', '1998-09-07', 'Cidadao L', 'Pai do Cidadao L', '15235512', 'M', 3, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('63537729634', 'Mae do Cidadao M', '1998-09-07', 'Cidadao M', 'Pai do Cidadao m', '15235513', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('22337853128', 'Mae do Cidadao N', '1998-09-07', 'Cidadao N', 'Pai do Cidadao N', '15235514', 'M', 2, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('82181658609', 'Mae do Cidadao O', '1998-09-07', 'Cidadao O', 'Pai do Cidadao O', '15235515', 'M', 3, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('14576843575', 'Mae do Cidadao P', '1998-09-07', 'Cidadao P', 'Pai do Cidadao P', '15235515', 'M', 1, '2023-02-14 09:00:00');
