--
-- VIRKSOMHET
--
INSERT INTO VIRKSOMHET (ORGANISASJONSNUMMER, NAVN) VALUES ('100', 'Testorganisasjon');
--
-- AVTALE
--
INSERT INTO AVTALE (AVTALEIDENTIFIKATOR, AVTALEBESKRIVELSE, AVTALEDATO, VIRKSOMHET_ORGANISASJONSNUMMER) VALUES ('Avtale1', 'Første avtale', '2015-01-23 17:39:41.281', '100');
INSERT INTO AVTALE (AVTALEIDENTIFIKATOR, AVTALEBESKRIVELSE, AVTALEDATO, VIRKSOMHET_ORGANISASJONSNUMMER) VALUES ('A1234', 'Avtale for testing', '2014-09-30 19:30:00', '100');
--
-- ARKIVSKAPER
--
INSERT INTO ARKIVSKAPER (UUID, KODE, NAVN) VALUES ('1', 'meg', 'Meg');
--
-- AVLEVERING
--
INSERT INTO AVLEVERING (AVLEVERINGSIDENTIFIKATOR, AVLEVERINGSBESKRIVELSE, LAAST, OPPDATERTAV, PROSESSTRINN, SISTOPPDATERT, AVTALE_AVTALEIDENTIFIKATOR, LAGRINGSENHETFORMAT) VALUES ('Avlevering-1', 'avlevering 1', false, NULL, NULL, '2015-01-23 17:36:59.588', 'Avtale1', 'PQ-[0-9]{2}');
INSERT INTO AVLEVERING (AVLEVERINGSIDENTIFIKATOR, AVLEVERINGSBESKRIVELSE, LAAST, OPPDATERTAV, PROSESSTRINN,  SISTOPPDATERT, AVTALE_AVTALEIDENTIFIKATOR, LAGRINGSENHETFORMAT) VALUES ('Avlevering-2', 'En beskrivelse', false, 'AS', 'Registrering', '2014-09-30 19:30:00', 'A1234', 'FB-[0-9]{3}');
--
-- KJØNN
--
INSERT INTO KJONN (CODE,DISPLAYNAME) VALUES ('K','Kvinne');
INSERT INTO KJONN (CODE,DISPLAYNAME) VALUES ('M','Mann');
-- 
-- PASIENTJOURNAL
--
INSERT INTO PASIENTJOURNAL (UUID, FANEARKID, PID, JOURNALNUMMER, LOPENUMMER, DDATO, FDATO, KJONN, FOERSTEKONTAKTAAR, PNAVN, OPPDATERTAV, PROSESSTRINN, SISTOPPDATERT) VALUES ('uuid1', '123456789100', '19090040165', '1', '2', '2005-12-31 17:42:47.356', '2005-01-01 17:42:47.356', 'K', NULL, 'Hunden Fido', 'AS', 'Registrering', '2015-01-23 17:42:47.356');
INSERT INTO PASIENTJOURNAL (UUID, FANEARKID, PID, JOURNALNUMMER, LOPENUMMER, DDATO, FDATO, KJONN, FOERSTEKONTAKTAAR, PNAVN, OPPDATERTAV, PROSESSTRINN, SISTOPPDATERT, SLETTET) VALUES ('uuid2', '123456789100', '19090040165', '2', '3', '2005-12-31 17:42:47.356', '2005-01-01 17:42:47.356', 'M', NULL, 'Fido', 'AS', 'Registrering', '2015-01-23 17:42:47.356', true);
INSERT INTO PASIENTJOURNAL (UUID, FANEARKID, PID, JOURNALNUMMER, LOPENUMMER, DDATO, FDATO, KJONN, FOERSTEKONTAKTAAR, PNAVN, OPPDATERTAV, PROSESSTRINN, SISTOPPDATERT) VALUES ('uuid3', '123456789100', '19090040165', '3', '4', '2005-12-31 17:42:47.356', '2005-01-01 17:42:47.356', 'M', NULL, 'Rex', 'AS', 'Registrering', '2015-01-23 17:42:47.356');
--
-- LAGRINGSENHET
--
INSERT INTO LAGRINGSENHET (UUID, IDENTIFIKATOR, UTSKRIFT) VALUES ('enhet-1', 'boks1', false);
INSERT INTO LAGRINGSENHET (UUID, IDENTIFIKATOR, UTSKRIFT) VALUES ('enhet-2', 'boks2', false);
INSERT INTO LAGRINGSENHET (UUID, IDENTIFIKATOR, UTSKRIFT) VALUES ('enhet-3', 'boks3', false);
--
-- PASIENTJOURNAL_LAGRINGSENHET
--
INSERT INTO PASIENTJOURNAL_LAGRINGSENHET VALUES ('uuid1', 'enhet-1');
INSERT INTO PASIENTJOURNAL_LAGRINGSENHET VALUES ('uuid1', 'enhet-2');
INSERT INTO PASIENTJOURNAL_LAGRINGSENHET VALUES ('uuid1', 'enhet-3');
INSERT INTO PASIENTJOURNAL_LAGRINGSENHET VALUES ('uuid2', 'enhet-1');
INSERT INTO PASIENTJOURNAL_LAGRINGSENHET VALUES ('uuid3', 'enhet-1');
--
-- CV
--
INSERT INTO CV (CODESYSTEMVERSION, CODESYSTEM, CODE, DISPLAYNAME, ORIGINALTEXT) VALUES ('1.0', '1.2.3', 'Code0', 'Code Zero', NULL);
INSERT INTO CV (CODESYSTEMVERSION, CODESYSTEM, CODE, DISPLAYNAME, ORIGINALTEXT) VALUES ('1.0', '1.2.3', 'Code1', 'Code One', NULL);
INSERT INTO CV (DISPLAYNAME, ORIGINALTEXT, CODESYSTEMVERSION, CODESYSTEM, CODE) VALUES ('displayName', 'originalText', 'codeSystemVersion', 'codeSystem', 'code');
--
-- DIAGNOSE
--
INSERT INTO DIAGNOSEKODE (CODESYSTEMVERSION, CODESYSTEM, CODE) VALUES ('1.0', '1.2.3', 'Code0');
INSERT INTO DIAGNOSEKODE (CODESYSTEMVERSION, CODESYSTEM, CODE) VALUES ('codeSystemVersion', 'codeSystem', 'code');
INSERT INTO DIAGNOSE (UUID, DATO, DIAGNOSETEKST, OPPDATERTAV, PROSESSTRINN, SISTOPPDATERT, DIAGNOSEKODE_CODESYSTEMVERSION, DIAGNOSEKODE_CODESYSTEM, DIAGNOSEKODE_CODE) VALUES ('uuid-diagnose-1', CURRENT_TIMESTAMP, 'Influensa', 'AS', 'Registrering', CURRENT_TIMESTAMP, '1.0', '1.2.3', 'Code0');
INSERT INTO DIAGNOSE (UUID, DATO, DIAGNOSETEKST, OPPDATERTAV, PROSESSTRINN, SISTOPPDATERT, DIAGNOSEKODE_CODESYSTEMVERSION, DIAGNOSEKODE_CODESYSTEM, DIAGNOSEKODE_CODE) VALUES ('uuid-diagnose-2', CURRENT_TIMESTAMP, 'Influensa', 'AS', 'Registrering', CURRENT_TIMESTAMP, '1.0', '1.2.3', 'Code0');
--
-- PASIENTJOURNAL_DIAGNOSE 
--
INSERT INTO PASIENTJOURNAL_DIAGNOSE (PASIENTJOURNAL_UUID, DIAGNOSE_UUID) VALUES ('uuid1', 'uuid-diagnose-1');
INSERT INTO PASIENTJOURNAL_DIAGNOSE (PASIENTJOURNAL_UUID, DIAGNOSE_UUID) VALUES ('uuid1', 'uuid-diagnose-2');
--
-- AVLEVERING_PASIENTJOURNAL
--
INSERT INTO AVLEVERING_PASIENTJOURNAL (AVLEVERING_AVLEVERINGSIDENTIFIKATOR, PASIENTJOURNAL_UUID) VALUES ('Avlevering-1', 'uuid2');
INSERT INTO AVLEVERING_PASIENTJOURNAL (AVLEVERING_AVLEVERINGSIDENTIFIKATOR, PASIENTJOURNAL_UUID) VALUES ('Avlevering-2', 'uuid1');
INSERT INTO AVLEVERING_PASIENTJOURNAL (AVLEVERING_AVLEVERINGSIDENTIFIKATOR, PASIENTJOURNAL_UUID) VALUES ('Avlevering-1', 'uuid3');
--
-- KONFIGPARAM
--
INSERT INTO KONFIGPARAM (NAVN, VERDI) VALUES ('LowLim', '01.01.1900');
INSERT INTO KONFIGPARAM (NAVN, VERDI) VALUES ('WaitLim', '10');
INSERT INTO KONFIGPARAM (NAVN, VERDI) VALUES ('MaxAge', '200');
INSERT INTO KONFIGPARAM (NAVN, VERDI) VALUES ('fanearkid', '12');
INSERT INTO KONFIGPARAM (NAVN, VERDI) VALUES ('aarhundre', '20');
--
-- ROLLER
--
INSERT INTO ROLLE (NAVN) VALUES ('admin');
INSERT INTO ROLLE (NAVN) VALUES ('bruker');
--
-- BRUKERE
--
INSERT INTO BRUKER (BRUKERNAVN, PASSORD, ROLLENAVN) VALUES ('nhabruker1', 'admin', 'admin');
INSERT INTO BRUKER (BRUKERNAVN, PASSORD, ROLLENAVN) VALUES ('nhabruker2', 'bruker', 'bruker');
INSERT INTO BRUKER (BRUKERNAVN, PASSORD, ROLLENAVN) VALUES ('anonymous', 'anon', 'admin');
