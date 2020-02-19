var mod = angular.module('nha.register.register-service', [
]);

mod.factory('registerService', [registerService]);

function registerService() {
    var transfer;
    var medicalRecordDTO;
    var transferId;
    var chosenAgreement;
    var business;
    var transferDescription;

    function setMedicalRecordDTO(data) {
        medicalRecordDTO = data;
    }

    function getMedicalRecordDTO() {
        return medicalRecordDTO;
    }

    function setTransfer(value) {
        transfer = value;
    }

    function getTransfer() {
        return transfer;
    }

    function getTransferId() {
        return transferId;
    }

    function setTransferId(value) {
        transferId = value;
    }

    function setChosenAgreement(value) {
        chosenAgreement = value;
    }

    function getChosenAgreement() {
        return chosenAgreement;
    }

    function setBusiness(value) {
        business = value;
    }

    function getBusiness() {
        return business;
    }

    function setTransferDescription(value) {
        transferDescription = value;
    }

    function getTransferDescription() {
        return transferDescription;
    }

    return {
        setTransfer: setTransfer,
        getTransfer: getTransfer,

        setMedicalRecordDTO: setMedicalRecordDTO,
        getMedicalRecordDTO: getMedicalRecordDTO,

        setTransferId : setTransferId,
        getTransferId : getTransferId,

        getChosenAgreement : getChosenAgreement,
        setChosenAgreement : setChosenAgreement,

        getBusiness : getBusiness,
        setBusiness : setBusiness,

        setTransferDescription : setTransferDescription,
        getTransferDescription : getTransferDescription
    };
}