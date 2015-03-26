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

