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
