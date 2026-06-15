/* ============================================================
   TABELA: TIPO_NAVIO
============================================================ */

CREATE TABLE Tipo_Navio(
    ID_Tipo_Navio int IDENTITY(1,1),
    Nome VARCHAR(255) NOT NULL,
    N_Maximo_Cargas INT NOT NULL,

    CONSTRAINT PK_Tipo_Navio
        PRIMARY KEY(ID_Tipo_Navio),

    CONSTRAINT CK_Tipo_Navio_Max_Cargas
        CHECK (N_Maximo_Cargas > 0)
);

/* ============================================================
   TABELA: PORTO
============================================================ */

CREATE TABLE Porto(
    ID_Porto int IDENTITY(1,1),
    Nome VARCHAR(255) NOT NULL,
    Cidade VARCHAR(255) NOT NULL,
    Pais VARCHAR(255) NOT NULL,
    Longitude DECIMAL(9,6) NOT NULL,
    Latitude DECIMAL(9,6) NOT NULL,

    CONSTRAINT PK_Porto
        PRIMARY KEY(ID_Porto),

    CONSTRAINT UQ_Porto
        UNIQUE(Nome, Cidade, Pais)
);

/* ============================================================
   TABELA: NAVIO
============================================================ */

CREATE TABLE Navio(
    ID_Navio INT IDENTITY(1,1),
    Nome VARCHAR(255) NOT NULL,
    IdentificadorIMO VARCHAR(255) NOT NULL,
    Tipo INT NOT NULL,
    N_Compartimentos INT NOT NULL,
    N_Maximo_Cargas INT NOT NULL,
    Bandeira VARCHAR(255) NOT NULL,
    Ano_Fabrico INT NOT NULL,
    Estado_Operacional VARCHAR(20) NOT NULL DEFAULT 'ativo',

    CONSTRAINT PK_Navio
        PRIMARY KEY(ID_Navio),

    CONSTRAINT UQ_Navio_IMO
        UNIQUE(IdentificadorIMO),

    CONSTRAINT FK_Navio_Tipo
        FOREIGN KEY(Tipo)
        REFERENCES Tipo_Navio(ID_Tipo_Navio),

    CONSTRAINT CK_Ano_Fabrico
        CHECK (
            Ano_Fabrico >= 1900
            AND Ano_Fabrico <= YEAR(GETDATE())
        ),  

    CONSTRAINT CK_Estado_Operacional
        CHECK (
            Estado_Operacional IN
            ('ativo','em manutenção','inativo')
        ),

    CONSTRAINT CK_Navio_N_Compartimentos
        CHECK (N_Compartimentos > 0),

    CONSTRAINT CK_Navio_Max_Cargas
        CHECK (N_Maximo_Cargas > 0)

    -- NOTA: A regra de negócio que impede N_Maximo_Cargas de exceder
    -- o máximo do Tipo_Navio é implementada via TRIGGER (não por constraint simples,
    -- pois envolve JOIN com outra tabela).
);

/* ============================================================
   TABELA: TIPO_CARGA
============================================================ */

CREATE TABLE Tipo_Carga(
    ID_Tipo_Carga int IDENTITY(1,1),
    Nome VARCHAR(255) NOT NULL,
    Designacao VARCHAR(255),

    CONSTRAINT PK_Tipo_Carga
        PRIMARY KEY(ID_Tipo_Carga)
);

/* ============================================================
   TABELA: CARGA
============================================================ */

CREATE TABLE Carga(
    ID_Carga int IDENTITY(1,1),
    Designacao VARCHAR(255) NOT NULL,
    Tipo INT NOT NULL,
    Volume FLOAT NOT NULL,
    Peso FLOAT NOT NULL,
    Porto_Carga INT NOT NULL,
    Porto_Descarga INT NOT NULL,

    CONSTRAINT PK_Carga
        PRIMARY KEY(ID_Carga),

    CONSTRAINT FK_Carga_Tipo
        FOREIGN KEY(Tipo)
        REFERENCES Tipo_Carga(ID_Tipo_Carga),

    CONSTRAINT FK_Carga_Porto_Carga
        FOREIGN KEY(Porto_Carga)
        REFERENCES Porto(ID_Porto),

    CONSTRAINT FK_Carga_Porto_Descarga
        FOREIGN KEY(Porto_Descarga)
        REFERENCES Porto(ID_Porto),

    CONSTRAINT CK_Carga_Volume
        CHECK (Volume > 0),

    CONSTRAINT CK_Carga_Peso
        CHECK (Peso > 0),

    CONSTRAINT CK_Carga_Portos_Distintos
        CHECK (Porto_Carga <> Porto_Descarga)
);

/* ============================================================
   TABELA: RISCO
============================================================ */

CREATE TABLE Risco(
    ID_Risco int IDENTITY(1,1),
    Designacao VARCHAR(30) NOT NULL,

    CONSTRAINT PK_Risco
        PRIMARY KEY(ID_Risco),

    CONSTRAINT CK_Risco
        CHECK (
            Designacao IN
            ('inflamavel','corrosivo','toxico')
        )
);

/* ============================================================
   TABELA: RISCO_TIPO_CARGA
============================================================ */

CREATE TABLE Risco_Tipo_Carga(
    ID_Risco INT NOT NULL,
    ID_Tipo_Carga INT NOT NULL,

    CONSTRAINT PK_Risco_Tipo_Carga
        PRIMARY KEY(ID_Tipo_Carga, ID_Risco),

    CONSTRAINT FK_RTC_Risco
        FOREIGN KEY(ID_Risco)
        REFERENCES Risco(ID_Risco),

    CONSTRAINT FK_RTC_Tipo_Carga
        FOREIGN KEY(ID_Tipo_Carga)
        REFERENCES Tipo_Carga(ID_Tipo_Carga)
);

/* ============================================================
   TABELA: TIPO_CARGA_NAVIO
============================================================ */

CREATE TABLE Tipo_Carga_Navio(
    ID_Tipo_Carga INT NOT NULL,
    ID_Tipo_Navio INT NOT NULL,

    CONSTRAINT PK_Tipo_Carga_Navio
        PRIMARY KEY(ID_Tipo_Carga, ID_Tipo_Navio),

    CONSTRAINT FK_TCN_Tipo_Carga
        FOREIGN KEY(ID_Tipo_Carga)
        REFERENCES Tipo_Carga(ID_Tipo_Carga),

    CONSTRAINT FK_TCN_Tipo_Navio
        FOREIGN KEY(ID_Tipo_Navio)
        REFERENCES Tipo_Navio(ID_Tipo_Navio)
);

/* ============================================================
   TABELA: VIAGEM
============================================================ */

CREATE TABLE Viagem(
    ID_Viagem INT identity(1,1),
    ID_Navio INT NOT NULL,
    Data_Partida DATE NOT NULL,
    Data_Chegada_Prevista DATE NOT NULL,
    Porto_Origem INT NOT NULL,
    Porto_Destino INT NOT NULL,
    Estado_Viagem VARCHAR(50) NOT NULL DEFAULT 'planeada',

    CONSTRAINT PK_Viagem
        PRIMARY KEY(ID_Viagem),

    CONSTRAINT FK_Viagem_Navio
        FOREIGN KEY(ID_Navio)
        REFERENCES Navio(ID_Navio),

    CONSTRAINT FK_Viagem_Porto_Origem
        FOREIGN KEY(Porto_Origem)
        REFERENCES Porto(ID_Porto),

    CONSTRAINT FK_Viagem_Porto_Destino
        FOREIGN KEY(Porto_Destino)
        REFERENCES Porto(ID_Porto),

    CONSTRAINT CK_Estado_Viagem
        CHECK (
            Estado_Viagem IN
            ('planeada','em curso','concluída','cancelada')
        ),

    CONSTRAINT CK_Datas_Viagem
        CHECK (
            Data_Chegada_Prevista >= Data_Partida
        ),

    CONSTRAINT CK_Viagem_Portos_Distintos
        CHECK (Porto_Origem <> Porto_Destino)

    -- NOTA: A regra de negócio de uma única viagem ativa por navio
    -- é implementada via TRIGGER (envolve consulta a outras linhas da tabela).
);

/* ============================================================
   TABELA: CARGA_VIAGEM
============================================================ */

CREATE TABLE Carga_Viagem(
    ID_Viagem INT NOT NULL,
    ID_Carga INT NOT NULL,

    CONSTRAINT PK_Carga_Viagem
        PRIMARY KEY(ID_Viagem, ID_Carga),

    CONSTRAINT FK_Carga_Viagem_Viagem
        FOREIGN KEY(ID_Viagem)
        REFERENCES Viagem(ID_Viagem),

    CONSTRAINT FK_Carga_Viagem_Carga
        FOREIGN KEY(ID_Carga)
        REFERENCES Carga(ID_Carga)

    -- NOTA: A exclusividade de carga em viagens ativas simultâneas
    -- é implementada via TRIGGER.
);

/* ============================================================
   TABELA: FUNCAO
============================================================ */

CREATE TABLE Funcao(
    ID_Funcao INT,
    Nome VARCHAR(255) NOT NULL,

    CONSTRAINT PK_Funcao
        PRIMARY KEY(ID_Funcao)
);

/* ============================================================
   TABELA: TRIPULANTE
============================================================ */

CREATE TABLE Tripulante(
    ID_Tripulante INT,
    Nome VARCHAR(255) NOT NULL,
    Sobrenome VARCHAR(255) NOT NULL,
    Estado_Disponibilidade VARCHAR(255) NOT NULL DEFAULT 'disponivel',
    Nacionalidade VARCHAR(255) NOT NULL,
    Data_Nascimento DATE NOT NULL,

    CONSTRAINT PK_Tripulante
        PRIMARY KEY(ID_Tripulante),

    CONSTRAINT CK_Estado_Disponibilidade
        CHECK (
            Estado_Disponibilidade IN
            ('disponível','em viagem','inativo')
        ),

    CONSTRAINT CK_Tripulante_Idade_Minima
        CHECK (
            Data_Nascimento <= DATEADD(YEAR, -18, GETDATE())
        ),
    CONSTRAINT FK_Funcao
         FOREIGN KEY (ID_Tripulante) 
         REFERENCES Funcao(ID_Funcao),
);

/* ============================================================
   TABELA: TRIPULACAO
   (FUNÇÃO POR VIAGEM)
============================================================ */

CREATE TABLE Tripulacao(
    ID_Viagem INT NOT NULL,
    ID_Tripulante INT NOT NULL,
    ID_Funcao INT NOT NULL,

    CONSTRAINT PK_Tripulacao
        PRIMARY KEY(ID_Viagem, ID_Tripulante),

    CONSTRAINT FK_Tripulacao_Viagem
        FOREIGN KEY(ID_Viagem)
        REFERENCES Viagem(ID_Viagem),

    CONSTRAINT FK_Tripulacao_Tripulante
        FOREIGN KEY(ID_Tripulante)
        REFERENCES Tripulante(ID_Tripulante),

    CONSTRAINT FK_Tripulacao_Funcao
        FOREIGN KEY(ID_Funcao)
        REFERENCES Funcao(ID_Funcao)
);