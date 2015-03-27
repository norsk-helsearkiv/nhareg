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
