delete from Avlevering_Pasientjournal;
delete from Avlevering;
delete from Avtale;
delete from Pasientjournal_Lagringsenhet;
delete from Pasientjournal_Diagnose;
delete from Pasientjournal;

SELECT  * from Pasientjournal_Diagnose;
select * from Diagnose;
show tables;
SELECT  * from Diagnosekode;
select * from Avtale;

select * from Pasientjournal;
select * from Avlevering_Pasientjournal where Avlevering_avleveringsidentifikator='avlevering';
select * from Pasientjournal where uuid in (select pasientjournal_uuid from Avlevering_Pasientjournal where Avlevering_avleveringsidentifikator='avlevering');

select * from Avlevering where avleveringsidentifikator='avlevering';
select * from Avlevering;

select * from Pasientjournal where uuid='92b018e8-806a-4078-9587-22f5c2c4f313';
select * from Pasientjournal_Diagnose where Pasientjournal_uuid = '92b018e8-806a-4078-9587-22f5c2c4f313';
select count(*) from Diagnose;
select * from Diagnose;
select count(*) from Pasientjournal_Diagnose;
select * from Pasientjournal_Diagnose;

select * from Pasientjournal where uuid ='a0791f90-16bd-49e6-9510-b52b2e922d08';

select count(*) from Avlevering_Pasientjournal pasientjou0_ inner join Pasientjournal pasientjou1_ on pasientjou0_.pasientjournal_uuid=pasientjou1_.uuid left outer join Kjonn kjønn2_ on pasientjou1_.kjonn=kjønn2_.code where pasientjou0_.Avlevering_avleveringsidentifikator='avlevering';


desc Pasientjournal;

-- opprette brukertabeller og roller

CREATE TABLE BrukerRolle(
  id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
  brukernavn VARCHAR(255) NOT NULL,
  rolle VARCHAR(255) NOT NULL,
  CONSTRAINT fk_rolle_navn FOREIGN KEY (rolle) REFERENCES Rolle(navn),
  CONSTRAINT fk_bruker_brukernavn FOREIGN KEY (brukernavn) REFERENCES Bruker(brukernavn)
);

DROP TABLE BrukerRolle;
DROP TABLE Bruker;
DROP TABLE Rolle;

CREATE TABLE Rolle(
  navn VARCHAR(255) NOT NULL PRIMARY KEY
);

select rolle, 'Roles' from BrukerRolle where brukernavn='nhabruker1';

CREATE TABLE Bruker(
  brukernavn VARCHAR(255) NOT NULL PRIMARY KEY ,
  passord VARCHAR(255) NOT NULL,
  rollenavn VARCHAR(255) NOT NULL,
  authtoken VARCHAR(255),
  CONSTRAINT fk_rolle_navn FOREIGN KEY (rollenavn) REFERENCES Rolle(navn)
);

INSERT INTO nhareg.Rolle(navn) values('bruker');
INSERT INTO nhareg.Bruker(brukernavn, passord, rollenavn) VALUES ('nhabruker2', 'BgWGA+ADDzl11fikSWj2FYtZl4xNNfm5cln0oUWC49w=', 'bruker');

INSERT INTO nhareg.BrukerRolle(brukernavn, rolle) values('nhabruker1', 'admin');
select * from BrukerRolle;

select passord as 'Password' from Bruker where brukernavn='nhabruker1';

select passwd from Users where username;
select rolle, 'Roles' from BrukerRolle where brukernavn='nhabruker1';
select rolle as 'role', 'Roles' from BrukerRolle where brukernavn='nhabruker1';

select * from Rolle;
select * from avtale;

-- opprette konfigtabell

CREATE TABLE Konfigparam (
  navn        VARCHAR(255) NOT NULL PRIMARY KEY,
  verdi       VARCHAR(255) NOT NULL,
  beskrivelse VARCHAR(255)
);

INSERT INTO nhareg.Konfigparam(navn, verdi, beskrivelse) VALUES ('LowLim', '01.01.1800', 'Tidligste tillatte dato i systemet');
INSERT INTO nhareg.Konfigparam(navn, verdi, beskrivelse) VALUES ('MaxAge', '110', 'Høyeste tillatte alder for en pasient som skal legges inn i systemet');
INSERT INTO nhareg.Konfigparam(navn, verdi, beskrivelse) VALUES ('WaitLim', '10', 'Antall år en pasient må være død før journalen kan legges inn i systemet');

-- NHA-021 legge inn gyldighetsperioder for diagnosekodeverk
CREATE TABLE Diagnosekodeverk(
  kodeverkversjon VARCHAR(255) PRIMARY KEY,
  gyldig_fra_dato DATE,
  gyldig_til_dato DATE
);

INSERT INTO Diagnosekodeverk VALUES
  ('ICD6', '1800-01-01', '1899-12-31'),
  ('ICD7', '1900-01-01', '1968-12-31'),
  ('ICD8', '1969-01-01', '1987-12-31'),
  ('ICD9', '1988-01-01', '1998-12-31'),
  ('ICD10', '1999-01-01', '2099-12-31');

ALTER TABLE Diagnosekode
  ADD CONSTRAINT FK_kodeverkversjon
FOREIGN KEY (codeSystemVersion) REFERENCES Diagnosekodeverk (kodeverkversjon);
-- NHA-021 end..

-- NHA-038
ALTER TABLE Avlevering
    ADD COLUMN lagringsenhetformat VARCHAR(255);
ALTER TABLE Bruker
    ADD COLUMN lagringsenhet VARCHAR(255);

-- NHA-038 end...

-- NHA-027
ALTER TABLE Pasientjournal
    ADD COLUMN merknad VARCHAR(255);

-- NHA-027 end...

