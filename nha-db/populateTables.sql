USE nhareg;

-- CREATE USERS AND ROLES
INSERT IGNORE INTO Rolle(navn) VALUES ('admin');
INSERT IGNORE INTO Rolle(navn) VALUES ('bruker');
INSERT IGNORE INTO Bruker(brukernavn, passord, rollenavn) VALUES ('nhabruker1', 'jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=', 'admin');
INSERT IGNORE INTO Bruker(brukernavn, passord, rollenavn) VALUES ('nhabruker2', 'BgWGA+ADDzl11fikSWj2FYtZl4xNNfm5cln0oUWC49w=', 'bruker');

-- CREATE CONFIGS
INSERT IGNORE INTO Konfigparam(navn, verdi, beskrivelse) VALUES ('LowLim', '01.01.1800', 'Tidligste tillatte dato i systemet');
INSERT IGNORE INTO Konfigparam(navn, verdi, beskrivelse) VALUES ('MaxAge', '110', 'Høyeste tillatte alder for en pasient som skal legges inn i systemet');
INSERT IGNORE INTO Konfigparam(navn, verdi, beskrivelse) VALUES ('WaitLim', '10', 'Antall år en pasient må være død før journalen kan legges inn i systemet');
INSERT IGNORE INTO Konfigparam(navn, verdi, beskrivelse) VALUES ('fanearkid', '12', 'Konfigurasjon for validering av feltlengde for fanearkid, kan være null');
INSERT IGNORE INTO Konfigparam(navn, verdi, beskrivelse) VALUES ('aarhundre', '19', 'Standardårhundre ved brukav 2 og 6 sifrete datoer');
INSERT IGNORE INTO Konfigparam(navn, verdi, beskrivelse) VALUES ('templateFilePath', '/Users/haraldk/template.txt', 'Filplasseringen til malen som benyttes til printing av etiketter');
INSERT IGNORE INTO Konfigparam(navn, verdi, beskrivelse) VALUES ('printerPort', '9100', 'Portnummeret som benyttes av Etikettprinter');

-- INSERT IGNORE DIAGNOSEKODEVERK
INSERT IGNORE INTO Diagnosekodeverk VALUES
  ('ICD6', '1800-01-01', '1899-12-31'),
  ('ICD7', '1900-01-01', '1968-12-31'),
  ('ICD8', '1969-01-01', '1987-12-31'),
  ('ICD9', '1988-01-01', '1998-12-31'),
  ('ICD10', '1999-01-01', '2099-12-31');

-- INSERT IGNORE SEX
INSERT IGNORE INTO Kjonn(code,displayName) VALUES ('K','Kvinne');
INSERT IGNORE INTO Kjonn(code,displayName) VALUES ('M','Mann');
INSERT IGNORE INTO Kjonn(code,displayName) VALUES ('U','Ikke kjent');
INSERT IGNORE INTO Kjonn(code,displayName) VALUES ('I','Ikke spesifisert');

-- INSERT IGNORE VIRKSOMHET
INSERT IGNORE INTO Virksomhet(organisasjonsnummer, navn) VALUES ('100', 'Testorganisasjon');

-- INSERT IGNORE CV
INSERT IGNORE INTO CV (CODESYSTEMVERSION, CODESYSTEM, CODE, DISPLAYNAME, ORIGINALTEXT) VALUES ('1.0', '1.2.3', 'Code0', 'Code Zero', 'hallaaa');
INSERT IGNORE INTO CV (CODESYSTEMVERSION, CODESYSTEM, CODE, DISPLAYNAME, ORIGINALTEXT) VALUES ('1.0', '1.2.3', 'Code0', 'Code Zero', 'kekkk');
INSERT IGNORE INTO CV (CODESYSTEMVERSION, CODESYSTEM, CODE, DISPLAYNAME, ORIGINALTEXT) VALUES ('1.0', '1.2.3', 'Code1', 'Code One', NULL);
INSERT IGNORE INTO CV (DISPLAYNAME, ORIGINALTEXT, CODESYSTEMVERSION, CODESYSTEM, CODE) VALUES ('displayName', 'originalText', 'codeSystemVersion', 'codeSystem', 'code');

-- INSERT IGNORE DIAGNOSEKODEVERK
INSERT IGNORE INTO Diagnosekodeverk (kodeverkversjon, gyldig_fra_dato, gyldig_til_dato) VALUES ('1.0', '2001-01-01', '2019-01-01');

-- INSERT IGNORE DIAGNOSEKODE
INSERT IGNORE INTO Diagnosekode (CODESYSTEMVERSION, CODESYSTEM, CODE) VALUES ('1.0', '1.2.3', 'Code0');

-- INSERT IGNORE DIAGNOSE
INSERT IGNORE INTO Diagnose (UUID, DATO, DIAGNOSETEKST, OPPDATERTAV, PROSESSTRINN, SISTOPPDATERT, DIAGNOSEKODE_CODESYSTEMVERSION, DIAGNOSEKODE_CODESYSTEM, DIAGNOSEKODE_CODE) VALUES ('uuid-diagnose-1', '2001-01-23 00:00:00', 'Influensa', 'AS', 'Registrering', CURRENT_TIMESTAMP, '1.0', '1.2.3', 'Code0');
INSERT IGNORE INTO Diagnose (UUID, DATO, DIAGNOSETEKST, OPPDATERTAV, PROSESSTRINN, SISTOPPDATERT, DIAGNOSEKODE_CODESYSTEMVERSION, DIAGNOSEKODE_CODESYSTEM, DIAGNOSEKODE_CODE) VALUES ('uuid-diagnose-2', '2008-01-23 00:00:00', 'Influensa', 'AS', 'Registrering', CURRENT_TIMESTAMP, '1.0', '1.2.3', 'Code0');
