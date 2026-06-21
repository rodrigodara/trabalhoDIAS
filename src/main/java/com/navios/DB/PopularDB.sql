/* ============================================================
   SCRIPT DE POPULAÇÃO — NavPetro / Navios
   SQL Server
   
   PROBLEMAS DE DESIGN IDENTIFICADOS NO SCHEMA:
   
   1. Risco_Tipo_Carga.ID_Risco é IDENTITY mas também FK para
      Risco(ID_Risco). Usamos SET IDENTITY_INSERT para controlar
      os valores inseridos.
   
   2. Tipo_Carga_Navio.ID_Tipo_Carga é IDENTITY mas também FK
      para Tipo_Carga(ID_Tipo_Carga). Mesmo workaround.
   
   3. Carga_Viagem.ID_Viagem é IDENTITY mas também FK para
      Viagem(ID_Viagem). Mesmo workaround.
   
   4. Tripulacao.ID_Viagem é IDENTITY mas também FK para
      Viagem(ID_Viagem). Mesmo workaround.
   
   5. Tripulante tem CONSTRAINT FK_Funcao FOREIGN KEY (ID_Tripulante)
      REFERENCES Funcao(ID_Funcao) — ou seja, o ID do tripulante
      tem de existir como ID de Funcao. Isso é um erro de design
      (falta uma coluna ID_Funcao na tabela Tripulante). Por
      agora, inserimos tantos tripulantes quantas as funções (5)
      para satisfazer a constraint.
   
   6. Tripulante.Estado_Disponibilidade tem DEFAULT 'disponivel'
      (sem acento) mas o CHECK exige 'disponível' (com acento).
      Usamos 'disponível' nos inserts.
   ============================================================ */


/* ============================================================
   1. TIPO_NAVIO
   Sem dependências.
============================================================ */
INSERT INTO Tipo_Navio (Nome, N_Maximo_Cargas) VALUES
('Petroleiro de crude', 200),
('Produtos refinados', 180),
('Químico',            120),
('Químico/produtos',   150);


/* ============================================================
   2. PORTO
   Sem dependências.
============================================================ */
INSERT INTO Porto (Nome, Cidade, Pais, Longitude, Latitude) VALUES
('Porto de Lisboa',    'Lisboa',    'Portugal',      -9.142700, 38.716900),
('Porto de Barcelona', 'Barcelona', 'Espanha',        2.173400, 41.385100),
('Porto de Roterdão',  'Roterdão',  'Países Baixos',  4.477700, 51.922500),
('Porto de Hamburgo',  'Hamburgo',  'Alemanha',        9.993700, 53.548800),
('Porto de Génova',    'Génova',    'Itália',          8.923200, 44.405600),
('Porto de Valência',  'Valência',  'Espanha',        -0.328300, 39.453900);


/* ============================================================
   3. RISCO
   Sem dependências. Apenas 3 valores permitidos pelo CHECK.
============================================================ */
INSERT INTO Risco (Designacao) VALUES
('inflamavel'),
('corrosivo'),
('toxico');


/* ============================================================
   4. TIPO_CARGA
   Sem dependências.
============================================================ */
INSERT INTO Tipo_Carga (Nome, Designacao) VALUES
('Combustível',  'Produtos petrolíferos e derivados'),
('Contentores',  'Carga geral em contentor ISO'),
('Cereais',      'Grãos e cereais a granel'),
('Químicos',     'Produtos químicos industriais'),
('Maquinaria',   'Equipamento industrial pesado');


/* ============================================================
   5. FUNCAO
   Sem dependências.
============================================================ */
INSERT INTO Funcao (Nome) VALUES
('Capitão'),
('Imediato'),
('Maquinista-Chefe'),
('Marinheiro'),
('Engenheiro de Bordo');


/* ============================================================
   6. NAVIO
   Depende de Tipo_Navio.
   IDs de Tipo: 1=Porta-Contentores, 2=Graneleiro,
                3=Petroleiro, 4=Ro-Ro
   N_Maximo_Cargas deve ser <= ao N_Maximo_Cargas do Tipo.
============================================================ */
INSERT INTO Navio (Nome, IdentificadorIMO, Tipo, N_Compartimentos, N_Maximo_Cargas, Bandeira, Ano_Fabrico, Estado_Operacional) VALUES
('NM Atlântico',    'IMO1234567', 1, 10, 200, 'Portugal', 2005, 'ativo'),
('NM Mediterrâneo', 'IMO2345678', 2,  8, 150, 'Espanha',  2010, 'ativo'),
('NM Oceânico',     'IMO3456789', 3,  6, 100, 'Portugal', 2015, 'ativo'),
('NM Europa',       'IMO4567890', 4,  5,  80, 'Alemanha', 2018, 'em manutenção');


/* ============================================================
   7. RISCO_TIPO_CARGA
   Depende de Risco e Tipo_Carga.
   ID_Risco é IDENTITY + FK — usamos SET IDENTITY_INSERT.
   
   Associações:
     Combustível (1) → inflamavel (1), corrosivo (2)
     Químicos    (4) → toxico (3), inflamavel (1)
============================================================ */
SET IDENTITY_INSERT Risco_Tipo_Carga ON;

INSERT INTO Risco_Tipo_Carga (ID_Risco, ID_Tipo_Carga) VALUES
(1, 1),  -- Combustível → inflamavel
(2, 1),  -- Combustível → corrosivo
(3, 4),  -- Químicos    → toxico
(1, 4);  -- Químicos    → inflamavel

SET IDENTITY_INSERT Risco_Tipo_Carga OFF;


/* ============================================================
   8. TIPO_CARGA_NAVIO
   Depende de Tipo_Carga e Tipo_Navio.
   ID_Tipo_Carga é IDENTITY + FK — usamos SET IDENTITY_INSERT.
   
   Associações (quais tipos de carga cada tipo de navio aceita):
     Porta-Contentores (1) → Contentores (2), Maquinaria (5)
     Graneleiro        (2) → Cereais (3)
     Petroleiro        (3) → Combustível (1), Químicos (4)
     Ro-Ro             (4) → Maquinaria (5)
============================================================ */
SET IDENTITY_INSERT Tipo_Carga_Navio ON;

INSERT INTO Tipo_Carga_Navio (ID_Tipo_Carga, ID_Tipo_Navio) VALUES
(1, 1),
(1, 2),
(2, 2),
(4, 3),
(1, 4),
(4, 4);

SET IDENTITY_INSERT Tipo_Carga_Navio OFF;


/* ============================================================
   9. CARGA
   Depende de Tipo_Carga e Porto.
   Porto_Carga <> Porto_Descarga (constraint no schema).
============================================================ */
INSERT INTO Carga (Designacao, Tipo, Volume, Peso, Porto_Carga, Porto_Descarga) VALUES
('Petróleo Bruto Lote A',       1, 5000.0, 4500.0, 1, 3),  -- Lisboa → Roterdão
('Contentores Eletrónica',      2,  800.0,  650.0, 3, 2),  -- Roterdão → Barcelona
('Trigo a Granel',              3, 1200.0, 1100.0, 4, 1),  -- Hamburgo → Lisboa
('Ácido Sulfúrico Industrial',  4,  300.0,  540.0, 5, 4),  -- Génova → Hamburgo
('Turbinas Industriais',        5,  450.0,  900.0, 2, 6),  -- Barcelona → Valência
('Gasóleo Refinado Lote B',     1, 3000.0, 2700.0, 6, 5);  -- Valência → Génova


/* ============================================================
   10. TRIPULANTE
   Depende de Funcao (por erro de design no schema).
   Inserimos exatamente 5 tripulantes (= nº de Funcoes)
   para que os IDs gerados (1–5) existam em Funcao.
   
   NOTA: O correto seria ter uma coluna ID_Funcao em Tripulante
   e a FK apontar para ela — não para ID_Tripulante.
============================================================ */
INSERT INTO Tripulante (Nome, Sobrenome, Estado_Disponibilidade, Nacionalidade, Data_Nascimento) VALUES
('João',    'Silva',    'disponível', 'Portuguesa', '1980-03-15'),
('Carlos',  'Ferreira', 'disponível', 'Portuguesa', '1985-07-22'),
('Miguel',  'Santos',   'disponível', 'Espanhola',  '1990-11-10'),
('António', 'Costa',    'disponível', 'Portuguesa', '1978-05-30'),
('Rui',     'Oliveira', 'disponível', 'Alemã',      '1992-01-18');


/* ============================================================
   11. VIAGEM
   Depende de Navio e Porto.
   Porto_Origem <> Porto_Destino e Data_Chegada >= Data_Partida.
============================================================ */
INSERT INTO Viagem (ID_Navio, Data_Partida, Data_Chegada_Prevista, Porto_Origem, Porto_Destino, Estado_Viagem) VALUES
(1, '2025-01-10', '2025-01-15', 1, 3, 'concluída'),   -- NM Atlântico: Lisboa → Roterdão
(2, '2025-02-01', '2025-02-08', 3, 2, 'concluída'),   -- NM Mediterrâneo: Roterdão → Barcelona
(3, '2025-03-05', '2025-03-12', 5, 4, 'em curso'),    -- NM Oceânico: Génova → Hamburgo
(1, '2025-04-01', '2025-04-07', 3, 6, 'planeada');    -- NM Atlântico: Roterdão → Valência


/* ============================================================
   12. CARGA_VIAGEM
   Depende de Viagem e Carga.
   ID_Viagem é IDENTITY + FK — usamos SET IDENTITY_INSERT.
============================================================ */
INSERT INTO Carga_Viagem (ID_Viagem, ID_Carga) VALUES
(1, 1),  -- Viagem 1 (Lisboa→Roterdão): Petróleo Bruto
(2, 2),  -- Viagem 2 (Roterdão→Barcelona): Contentores Eletrónica
(3, 4),  -- Viagem 3 (Génova→Hamburgo): Ácido Sulfúrico
(4, 5);  -- Viagem 4 (Roterdão→Valência): Turbinas Industriais



/* ============================================================
   13. TRIPULACAO
   Depende de Viagem, Tripulante e Funcao.
   ID_Viagem é IDENTITY + FK — usamos SET IDENTITY_INSERT.
============================================================ */
INSERT INTO Tripulacao (ID_Viagem, ID_Tripulante, ID_Funcao) VALUES
(1, 1, 1),  -- Viagem 1: João Silva       → Capitão
(1, 2, 2),  -- Viagem 1: Carlos Ferreira  → Imediato
(1, 3, 4),  -- Viagem 1: Miguel Santos    → Marinheiro
(2, 4, 1),  -- Viagem 2: António Costa    → Capitão
(2, 5, 3),  -- Viagem 2: Rui Oliveira     → Maquinista-Chefe
(3, 1, 1),  -- Viagem 3: João Silva       → Capitão
(3, 2, 5),  -- Viagem 3: Carlos Ferreira  → Engenheiro de Bordo
(4, 3, 2),  -- Viagem 4: Miguel Santos    → Imediato
(4, 4, 4);  -- Viagem 4: António Costa    → Marinheiro


