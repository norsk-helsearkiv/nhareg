USE nhareg;

CREATE TABLE IF NOT EXISTS Virksomhet (
  organisasjonsnummer varchar(255) NOT NULL,
  foretaksnavn varchar(255) DEFAULT NULL,
  navn varchar(255) DEFAULT NULL,
  PRIMARY KEY (organisasjonsnummer)
);

CREATE TABLE IF NOT EXISTS Rolle (
  navn varchar(255) NOT NULL,
  PRIMARY KEY (navn)
);

CREATE TABLE IF NOT EXISTS Kjonn (
  code varchar(255) NOT NULL,
  displayName varchar(255) NOT NULL,
  PRIMARY KEY (code)
);

CREATE TABLE IF NOT EXISTS Konfigparam (
  navn varchar(255) NOT NULL,
  beskrivelse varchar(255) DEFAULT NULL,
  verdi varchar(255) DEFAULT NULL,
  PRIMARY KEY (navn)
);

CREATE TABLE IF NOT EXISTS Lagringsenhet (
  uuid varchar(255) NOT NULL,
  identifikator varchar(255) NOT NULL UNIQUE,
  utskrift bit(1) NOT NULL,
  PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS CS (
  code varchar(255) NOT NULL,
  displayName varchar(255) NOT NULL,
  PRIMARY KEY (code)
);

CREATE TABLE IF NOT EXISTS CV (
  codeSystemVersion varchar(255) NOT NULL,
  codeSystem varchar(255) NOT NULL,
  code varchar(255) NOT NULL,
  displayName varchar(255) DEFAULT NULL,
  originalText varchar(255) DEFAULT NULL,
  PRIMARY KEY (codeSystemVersion,codeSystem,code)
);

CREATE TABLE IF NOT EXISTS Diagnosekodeverk (
  kodeverkversjon varchar(255) NOT NULL,
  gyldig_fra_dato date DEFAULT NULL,
  gyldig_til_dato date DEFAULT NULL,
  PRIMARY KEY (kodeverkversjon)
);

CREATE TABLE IF NOT EXISTS Pasientjournal (
  uuid varchar(255) NOT NULL,
  fanearkid varchar(255) DEFAULT NULL,
  daar int(11) DEFAULT NULL,
  ddato datetime DEFAULT NULL,
  dodsdatoUkjent bit(1) DEFAULT NULL,
  fodtdatoUkjent bit(1) DEFAULT NULL,
  faar int(11) DEFAULT NULL,
  fdato datetime DEFAULT NULL,
  pid varchar(255) DEFAULT NULL,
  typePID varchar(255) DEFAULT NULL,
  foersteKontaktAar int(11) DEFAULT NULL,
  foersteKontaktDato datetime DEFAULT NULL,
  sisteKontaktAar int(11) DEFAULT NULL,
  sisteKontaktDato datetime DEFAULT NULL,
  pnavn varchar(255) DEFAULT NULL,
  journalnummer varchar(255) DEFAULT NULL,
  lopenummer varchar(255) DEFAULT NULL,
  merknad varchar(255) DEFAULT NULL,
  oppdatertAv varchar(255) DEFAULT NULL,
  prosesstrinn varchar(255) DEFAULT NULL,
  sistOppdatert datetime DEFAULT NULL,
  opprettetDato datetime DEFAULT NULL,
  slettet bit(1) DEFAULT NULL,
  kjonn varchar(255) DEFAULT NULL,
  PRIMARY KEY (uuid),
  KEY FK_4uryeovtka0bvq923uu4oagj6 (kjonn),
  FOREIGN KEY (kjonn) REFERENCES Kjonn (code)
);

CREATE TABLE IF NOT EXISTS Avtale (
  avtaleidentifikator varchar(255) NOT NULL,
  avtalebeskrivelse varchar(255) NOT NULL,
  avtaledato datetime NOT NULL,
  virksomhet_organisasjonsnummer varchar(255) NOT NULL,
  PRIMARY KEY (avtaleidentifikator),
  KEY FK_gb3iehit9svx8o0o08rqs8awf (virksomhet_organisasjonsnummer),
  FOREIGN KEY (virksomhet_organisasjonsnummer) REFERENCES Virksomhet (organisasjonsnummer)
);

CREATE TABLE IF NOT EXISTS Avlevering (
  avleveringsidentifikator varchar(255) NOT NULL,
  arkivskaper varchar(255) DEFAULT NULL,
  avleveringsbeskrivelse varchar(255) DEFAULT NULL,
  laast bit(1) NOT NULL,
  lagringsenhetformat varchar(255) DEFAULT NULL,
  oppdatertAv varchar(255) DEFAULT NULL,
  prosesstrinn varchar(255) DEFAULT NULL,
  sistOppdatert datetime DEFAULT NULL,
  avtale_avtaleidentifikator varchar(255) DEFAULT NULL,
  PRIMARY KEY (avleveringsidentifikator),
  KEY FK_pg9s04ts289j2uw9gpn6il799 (avtale_avtaleidentifikator),
  FOREIGN KEY (avtale_avtaleidentifikator) REFERENCES Avtale (avtaleidentifikator)
);

CREATE TABLE IF NOT EXISTS Avlevering_Pasientjournal (
  Avlevering_avleveringsidentifikator varchar(255) NOT NULL,
  pasientjournal_uuid varchar(255) NOT NULL,
  PRIMARY KEY (Avlevering_avleveringsidentifikator,pasientjournal_uuid),
  UNIQUE KEY UK_n0qqd7f5q7esb8c5rc1va3lpi (pasientjournal_uuid),
  FOREIGN KEY (Avlevering_avleveringsidentifikator) REFERENCES Avlevering (avleveringsidentifikator),
  FOREIGN KEY (pasientjournal_uuid) REFERENCES Pasientjournal (uuid)
);

CREATE TABLE IF NOT EXISTS Bruker (
  brukernavn varchar(255) NOT NULL,
  passord varchar(255) NOT NULL,
  rollenavn varchar(255) NOT NULL,
  authtoken varchar(255) DEFAULT NULL,
  lagringsenhet varchar(255) DEFAULT NULL,
  defaultAvleveringsUuid varchar(255) DEFAULT NULL,
  printerzpl varchar(255) DEFAULT NULL,
  resetPassord varchar(255) DEFAULT NULL,
  PRIMARY KEY (brukernavn),
  KEY fk_rolle_navn (rollenavn),
  FOREIGN KEY (rollenavn) REFERENCES Rolle (navn)
);

CREATE TABLE IF NOT EXISTS Diagnosekode (
  codeSystemVersion varchar(255) NOT NULL,
  codeSystem varchar(255) NOT NULL,
  code varchar(255) NOT NULL,
  PRIMARY KEY (codeSystemVersion,codeSystem,code),
  FOREIGN KEY (codeSystemVersion, codeSystem, code) REFERENCES CV (codeSystemVersion, codeSystem, code),
  FOREIGN KEY (codeSystemVersion) REFERENCES Diagnosekodeverk (kodeverkversjon)
);

CREATE TABLE IF NOT EXISTS Diagnose (
  uuid varchar(255) NOT NULL,
  aar int(11) DEFAULT NULL,
  dato datetime DEFAULT NULL,
  diagnosetekst varchar(255) NOT NULL,
  oppdatertAv varchar(255) DEFAULT NULL,
  prosesstrinn varchar(255) DEFAULT NULL,
  sistOppdatert datetime DEFAULT NULL,
  diagnosekode_codeSystemVersion varchar(255) DEFAULT NULL,
  diagnosekode_codeSystem varchar(255) DEFAULT NULL,
  diagnosekode_code varchar(255) DEFAULT NULL,
  PRIMARY KEY (uuid),
  KEY FK_t3qunbns45pwuj6naog5jfy1f (diagnosekode_codeSystemVersion,diagnosekode_codeSystem,diagnosekode_code),
  FOREIGN KEY (diagnosekode_codeSystemVersion, diagnosekode_codeSystem, diagnosekode_code) REFERENCES Diagnosekode (codeSystemVersion, codeSystem, code)
);

CREATE TABLE IF NOT EXISTS Pasientjournal_Diagnose (
  Pasientjournal_uuid varchar(255) NOT NULL,
  diagnose_uuid varchar(255) NOT NULL,
  PRIMARY KEY (Pasientjournal_uuid,diagnose_uuid),
  UNIQUE KEY UK_ae3ftd30v8fu45l24kxud7fdl (diagnose_uuid),
  FOREIGN KEY (diagnose_uuid) REFERENCES Diagnose (uuid),
  FOREIGN KEY (Pasientjournal_uuid) REFERENCES Pasientjournal (uuid)
);

CREATE TABLE IF NOT EXISTS Pasientjournal_Lagringsenhet (
  Pasientjournal_uuid varchar(255) NOT NULL,
  lagringsenhet_uuid varchar(255) NOT NULL,
  KEY FK_mjnypvx37r8wog8f2idywvg7a (lagringsenhet_uuid),
  KEY FK_eo35uphmr5rlmb4m3xo1g3ma8 (Pasientjournal_uuid),
  FOREIGN KEY (Pasientjournal_uuid) REFERENCES Pasientjournal (uuid),
  FOREIGN KEY (Lagringsenhet_uuid) REFERENCES Lagringsenhet (uuid)
);
