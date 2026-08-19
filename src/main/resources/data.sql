insert into categoria(id, descricao) values('05','Cidade');
insert into categoria(id, descricao) values('10','Vila');
insert into categoria(id, descricao) values('15','Vila');
insert into categoria(id, descricao) values('20','Vila');

insert into uf(sigla,descricao) values('AC', 'Acre');
insert into uf(sigla,descricao) values('AL', 'Alagoas');
insert into uf(sigla,descricao) values('AM', 'Amazonas');
insert into uf(sigla,descricao) values('AP', 'Amapá');
insert into uf(sigla,descricao) values('BA', 'Bahia');
insert into uf(sigla,descricao) values('CE', 'Ceará');
insert into uf(sigla,descricao) values('DF', 'Distrito Federal');
insert into uf(sigla,descricao) values('ES', 'Espírito Santo');
insert into uf(sigla,descricao) values('GO', 'Goiás');
insert into uf(sigla,descricao) values('MA', 'Maranhão');
insert into uf(sigla,descricao) values('MG', 'Minas Gerais');
insert into uf(sigla,descricao) values('MT', 'Mato Grosso');
insert into uf(sigla,descricao) values('MS', 'Mato Grosso do Sul');
insert into uf(sigla,descricao) values('PA', 'Pará');
insert into uf(sigla,descricao) values('PB', 'Paraíba');
insert into uf(sigla,descricao) values('PE', 'Pernambuco');
insert into uf(sigla,descricao) values('PI', 'Piauí');
insert into uf(sigla,descricao) values('PR', 'Paraná');
insert into uf(sigla,descricao) values('RN', 'Rio Grande do Norte');
insert into uf(sigla,descricao) values('RJ', 'Rio de Janeiro');
insert into uf(sigla,descricao) values('RO', 'Rondônia');
insert into uf(sigla,descricao) values('RR', 'Roraima');
insert into uf(sigla,descricao) values('RS', 'Rio Grande do Sul');
insert into uf(sigla,descricao) values('SC', 'Santa Catarina');
insert into uf(sigla,descricao) values('SE', 'Sergipe');
insert into uf(sigla,descricao) values('SP', 'São Paulo');
insert into uf(sigla,descricao) values('TO', 'Tocantins');

insert into mesoregiao(descricao, uf_id) values('MESO TESTE', 1);

insert into microregiao(descricao, mesoregiao_id) values('MICRO TESTE', 1);

insert into municipio(codigo, descricao, uf_sigla, microregiao_id) values(1, 'MUNICIPIO TESTE', 'AC', 1); 

insert into distrito(descricao, municipio_id) values('DISTRITO TESTE',1);

insert into localidade(descricao, tipo, bairro, subdistrito, nivel, categoria_id, distrito_id, latitude, longitude, altitude) values('PONTE NOVA', 'URBANA', 'Centro Histórico', '1 SUBDISTRITO', 1, '05', 1, -20.41664342, -42.90968427, 422.561737);
insert into localidade(descricao, tipo, bairro, subdistrito, nivel, categoria_id, distrito_id, latitude, longitude, altitude) values('ALTA FLORESTA D''OESTE', 'URBANA', 'Redondo', null, 1, '05', 1, -11.9355403, -61.9998239, 337.735719);
insert into localidade(descricao, tipo, bairro, subdistrito, nivel, categoria_id, distrito_id, latitude, longitude, altitude) values('BRASÌLIA', 'URBANA', null, 'BRASÌLIA', 1, '05', 1, -15.79408736, -47.88790548, 1115.248357);
insert into localidade(descricao, tipo, bairro, subdistrito, nivel, categoria_id, distrito_id, latitude, longitude, altitude) values('SÃO PAULO', 'URBANA', null, null, 1, '05', 1, -23.5673865, -46.57038318, 783.615127);


insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('22222222222', 'Mae do Cidadao A', '1998-09-07', 'Cidadao A', 'Pai do Cidadao A', '1523551', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('33333333333', 'Mae do Cidadao B', '1998-09-07', 'Cidadao B', 'Pai do Cidadao B', '1523552', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('44444444444', 'Mae do Cidadao C', '1998-09-07', 'Cidadao C', 'Pai do Cidadao C', '1523553', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('55555555555', 'Mae do Cidadao D', '1998-09-07', 'Cidadao D', 'Pai do Cidadao D', '1523554', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('66666666666', 'Mae do Cidadao E', '1998-09-07', 'Cidadao E', 'Pai do Cidadao E', '1523555', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('77777777777', 'Mae do Cidadao F', '1998-09-07', 'Cidadao F', 'Pai do Cidadao F', '1523556', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('88888888888', 'Mae do Cidadao G', '1998-09-07', 'Cidadao G', 'Pai do Cidadao G', '1523557', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('11111111111', 'Mae do Cidadao H', '1998-09-07', 'Cidadao H', 'Pai do Cidadao H', '1523558', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('99999999999', 'Mae do Cidadao I', '1998-09-07', 'Cidadao I', 'Pai do Cidadao I', '1523559', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('72434701493', 'Mae do Cidadao J', '1998-09-07', 'Cidadao J', 'Pai do Cidadao J', '15235510', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('30239915291', 'Mae do Cidadao K', '1998-09-07', 'Cidadao K', 'Pai do Cidadao K', '15235511', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('47225231855', 'Mae do Cidadao L', '1998-09-07', 'Cidadao L', 'Pai do Cidadao L', '15235512', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('63537729634', 'Mae do Cidadao M', '1998-09-07', 'Cidadao M', 'Pai do Cidadao m', '15235513', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('22337853128', 'Mae do Cidadao N', '1998-09-07', 'Cidadao N', 'Pai do Cidadao N', '15235514', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('82181658609', 'Mae do Cidadao O', '1998-09-07', 'Cidadao O', 'Pai do Cidadao O', '15235515', 'M', 1, '2023-02-14 09:00:00');
insert into cidadao(cpf, mae, nascimento_data, nome, pai, rg, sexo, municipio_nascimento_codigo, audit_data)
    values('14576843575', 'Mae do Cidadao P', '1998-09-07', 'Cidadao P', 'Pai do Cidadao P', '15235515', 'M', 1, '2023-02-14 09:00:00');
