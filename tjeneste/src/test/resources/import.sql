--
-- VIRKSOMHET
--
INSERT INTO VIRKSOMHET (ORGANISASJONSNUMMER, NAVN) VALUES ('100', 'Testorganisasjon');
--
-- AVTALE
--
INSERT INTO AVTALE (AVTALEIDENTIFIKATOR, AVTALEBESKRIVELSE, AVTALEDATO, VIRKSOMHET_ORGANISASJONSNUMMER) VALUES ('Avtale1', 'Første avtale', '2015-01-23 17:39:41.281', '100');
insert into Avtale (avtalebeskrivelse, avtaledato, avtaleidentifikator) values ('Avtale for testing', '2014-09-30 19:30:00', 'A1234');

--
-- AVLEVERING
--
INSERT INTO AVLEVERING (AVLEVERINGSIDENTIFIKATOR, ARKIVSKAPER, AVLEVERINGSBESKRIVELSE, OPPDATERTAV, PROSESSTRINN, SISTOPPDATERT, AVTALE_AVTALEIDENTIFIKATOR) VALUES ('Avlevering-1', 'meg', NULL, NULL, NULL, '2015-01-23 17:36:59.588', 'Avtale1');
insert into Avlevering (avleveringsidentifikator, arkivskaper, avleveringsbeskrivelse, oppdatertAv, prosesstrinn,  sistOppdatert, avtale_avtaleidentifikator) values ('Avlevering-2',           'meg',       'En beskrivelse',       'AS',       'Registrering', '2014-09-30 19:30:00', 'A1234');
--
-- KJØNN
--
insert into Kjonn(code,displayName) values ('K','Kvinne');
insert into Kjonn(code,displayName) values ('M','Mann');
-- PASIENTJOURNAL
--
INSERT INTO PASIENTJOURNAL (UUID, PID, JOURNALNUMMER, LOPENUMMER, DDATO, FDATO, KJONN, foersteKontaktAar, PNAVN, OPPDATERTAV, PROSESSTRINN, SISTOPPDATERT) VALUES ('uuid1', '01019912345', '1', '2', '2015-01-23 17:42:47.356', '2015-01-23 17:42:47.356', 'K', NULL, 'Hunden Fido', 'AS', 'Registrering', '2015-01-23 17:42:47.356');
INSERT INTO PASIENTJOURNAL (UUID, PID, JOURNALNUMMER, LOPENUMMER, DDATO, FDATO, KJONN, foersteKontaktAar, PNAVN, OPPDATERTAV, PROSESSTRINN, SISTOPPDATERT, SLETTET) VALUES ('uuid2', '01019912344', '2', '3', '2015-01-23 17:42:47.356', '2015-01-23 17:42:47.356', 'M', NULL, 'Fido', 'AS', 'Registrering', '2015-01-23 17:42:47.356', true);
INSERT INTO PASIENTJOURNAL (UUID, PID, JOURNALNUMMER, LOPENUMMER, DDATO, FDATO, KJONN, foersteKontaktAar, PNAVN, OPPDATERTAV, PROSESSTRINN, SISTOPPDATERT) VALUES ('uuid3', '01019912343', '3', '4', '2015-01-23 17:42:47.356', '2015-01-23 17:42:47.356', 'M', NULL, 'Rex', 'AS', 'Registrering', '2015-01-23 17:42:47.356');
--
-- LAGRINGSENHET
--
INSERT INTO LAGRINGSENHET VALUES('enhet-1', 'boks1');
--
-- PASIENTJOURNAL_LAGRINGSENHET
--
INSERT INTO PASIENTJOURNAL_LAGRINGSENHET VALUES('uuid1', 'enhet-1');
INSERT INTO PASIENTJOURNAL_LAGRINGSENHET VALUES('uuid2', 'enhet-1');
INSERT INTO PASIENTJOURNAL_LAGRINGSENHET VALUES('uuid3', 'enhet-1');
--
-- AVLEVERING/PASIENTJOURNAL
--
--
-- CV
--
INSERT INTO CV (CODESYSTEMVERSION, CODESYSTEM, CODE, DISPLAYNAME, ORIGINALTEXT) VALUES ('1.0', '1.2.3', 'Code0', 'Code Zero', NULL);
INSERT INTO CV (CODESYSTEMVERSION, CODESYSTEM, CODE, DISPLAYNAME, ORIGINALTEXT) VALUES ('1.0', '1.2.3', 'Code1', 'Code One', NULL);
insert into CV (displayName, originalText, codeSystemVersion, codeSystem, code) values ('displayName', 'originalText', 'codeSystemVersion', 'codeSystem', 'code');
--
-- DIAGNOSE
--
insert into Diagnosekode (codeSystemVersion, codeSystem, code) values ('1.0', '1.2.3', 'Code0');
insert into Diagnosekode (codeSystemVersion, codeSystem, code) values ('codeSystemVersion', 'codeSystem', 'code');
INSERT INTO DIAGNOSE (UUID, DATO, DIAGNOSETEKST, OPPDATERTAV, PROSESSTRINN, SISTOPPDATERT, DIAGNOSEKODE_CODESYSTEMVERSION, DIAGNOSEKODE_CODESYSTEM, DIAGNOSEKODE_CODE) VALUES ('uuid-diagnose-1', CURRENT_TIMESTAMP, 'Influensa', 'AS', 'Registrering', CURRENT_TIMESTAMP, '1.0', '1.2.3', 'Code0');
--
-- PASIENTJOURNAL_DIAGNOSE
--
INSERT INTO PASIENTJOURNAL_DIAGNOSE (PASIENTJOURNAL_UUID, DIAGNOSE_UUID) VALUES ('uuid1', 'uuid-diagnose-1');

--
-- AVLEVERING_PASIENTJOURNAL
--
INSERT INTO Avlevering_Pasientjournal(Avlevering_avleveringsidentifikator,pasientjournal_uuid) VALUES ('Avlevering-1', 'uuid1');
INSERT INTO Avlevering_Pasientjournal(Avlevering_avleveringsidentifikator,pasientjournal_uuid) VALUES ('Avlevering-1', 'uuid2');
INSERT INTO Avlevering_Pasientjournal(Avlevering_avleveringsidentifikator,pasientjournal_uuid) VALUES ('Avlevering-1', 'uuid3');