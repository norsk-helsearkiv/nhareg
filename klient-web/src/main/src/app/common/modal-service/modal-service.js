var mod = angular.module('nha.common.modal-service', [
    'ui.bootstrap',
    'nha.common.http-service',
    'nha.common.error-service',
    'cfp.hotkeys'
]);

mod.factory('modalService', ['$modal', 'httpService', 'errorService', 'hotkeys', '$filter', modalService]);

function modalService($modal, httpService, errorService, hotkeys, $filter) {
    var template = {
        backdrop: 'static',
        windowClass: "modal-center"
    };

    /*
        Deletes an element
        elementType: the type of the element that is to be deleted
        id: the id of the element
        okFunction: method for validation
    */
    function deleteModal(elementType, id, okFunction) {
        template.templateUrl = 'common/modal-service/delete-modal.tpl.html';

        template.controller = function ($scope, $modalInstance) {
            $scope.elementType = elementType;
            $scope.id = id;

            $scope.ok = function() {
                okFunction();
                $modalInstance.close();
            };

            $scope.cancel = function() {
                $modalInstance.close();
            };
        };

        template.controller.$inject = ['$scope', '$modalInstance'];

        return $modal.open(template);
    }

    /*
        Modal for å opprette ny

        tpl: link til tpl.html som skal brukes
        list: liste over elementer som det skal legges til et element i scope
        valideringFunction: overskrevet metode for validering, returnerer boolean om det
            gikk bra.
    */

    function nyModal(tpl, list, relativUrl, valideringFunction) {
        template.templateUrl = tpl;
        template.controller = function ($scope, $modalInstance) {
            $scope.formData = {
                "error" : {}
            };

            $scope.ok = function() {
                var success = valideringFunction($scope.formData);
                if(success) {
                    httpService.create(relativUrl, $scope.formData)
                        .success(function(data, status, headers, config) {
                            list.push(data);
                        }).error(function(data, status, headers, config) {
                        errorService.errorCode(status);
                    });
                    $modalInstance.close();
                }
            };

            $scope.avbryt = function() {
                $modalInstance.close();
            };
        };
        template.controller.$inject = ['$scope', '$modalInstance'];
        return $modal.open(template);
    }

    function warningMessageModal(msg, title, desc){
        template.templateUrl = "common/modal-service/warning-message-modal.tpl.html";
        template.controller = function ($scope, $modalInstance) {
            $scope.melding = msg;
            $scope.tittel = title;
            $scope.beskrivelse = desc;

            $scope.ok = function() {
                $modalInstance.close();
            };

        };
        template.controller.$inject = ['$scope', '$modalInstance'];
        return $modal.open(template);
    }

    function warningModal(tpl, relativeUrl, msg, title, desc, okFunction){
        template.templateUrl = tpl;
        template.controller = function ($scope, $modalInstance) {
            $scope.formData = {
                "error" : {}
            };
            $scope.melding = msg;
            $scope.tittel = title;
            $scope.beskrivelse = desc;

            $scope.ok = function() {
                if (relativeUrl){
                    httpService.create(relativeUrl, $scope.formData)
                        .success(function(data, status, headers, config) {
                            $modalInstance.close();
                            okFunction();
                        }).error(function(data, status, headers, config) {
                        errorService.errorCode(status);
                    });
                }else{
                    $modalInstance.close();
                    okFunction();
                }
            };

            $scope.avbryt = function() {
                $modalInstance.close();
            };
        };
        template.controller.$inject = ['$scope', '$modalInstance'];
        return $modal.open(template);
    }

    function endreModal(tpl, list, relativUrl, valideringFunction, entitet) {
        template.templateUrl = tpl;
        template.controller = function ($scope, $modalInstance) {
            $scope.formData = entitet;
            $scope.erEndring = true;

            $scope.ok = function() {
                var success = valideringFunction($scope.formData);
                if(success) {
                    httpService.update(relativUrl, $scope.formData)
                        .error(function(data, status, headers, config) {
                            errorService.errorCode(status);
                        });
                    $modalInstance.close();
                }
            };

            $scope.avbryt = function() {
                $modalInstance.close();
            };
        };
        template.controller.$inject = ['$scope', '$modalInstance'];
        return $modal.open(template);
    }

    function velgModal(tpl, list, formDiagnose) {
        template.templateUrl = tpl;
        template.controller = function($scope, $modalInstance) {

            hotkeys.bindTo($scope)
                .add({
                    combo: 'down',
                    callback: function() {
                        if(valgtIndex === $scope.modalListe.length - 1) {
                            $scope.oppdaterValg($scope.modalListe[valgtIndex]);
                            return;
                        }
                        $scope.oppdaterValg($scope.modalListe[++valgtIndex]);
                    }
                })
                .add({
                    combo: 'up',
                    callback: function() {
                        if(valgtIndex === 0) {
                            $scope.oppdaterValg($scope.modalListe[valgtIndex]);
                            return;
                        }
                        $scope.oppdaterValg($scope.modalListe[--valgtIndex]);
                    }
                })
                .add({
                    combo: 'enter',
                    callback: function() {
                        $scope.ok();
                    }
                });

            $scope.modalListe = list;
            var valgtIndex = 0;

            var resetValg = function() {
                angular.forEach($scope.modalListe, function(e) {
                    e.selected = false;
                });
            };

            $scope.oppdaterValg = function(element) {
                resetValg();

                for(var i = 0; i < $scope.modalListe.length; i++) {
                    if(element === $scope.modalListe[i]) {
                        $scope.modalListe[i].selected = true;
                        valgtIndex = i;
                    }
                }
            };
            $scope.oppdaterValg($scope.modalListe[0]);

            $scope.ok = function() {
                formDiagnose.diagnosetekst = $scope.modalListe[valgtIndex].displayName;
                formDiagnose.diagnosekodeverk = $scope.modalListe[valgtIndex].codeSystemVersion;
                $modalInstance.close();
            };

            $scope.avbryt = function() {
                $modalInstance.dismiss('cancel');
            };
        };
        template.controller.$inject = ['$scope', '$modalInstance'];
        return $modal.open(template);
    }

    function changeStorageUnit(templateUrl, relativeUrl, storageUnit, storageUnitFormat){
        template.templateUrl = templateUrl;

        template.controller = function( $scope, $modalInstance) {
            $scope.storageUnitFormat = storageUnitFormat;

            $scope.formData = {
                "error" : {},
                "storageUnit": storageUnit
            };

            $scope.save = function(){
                var input = $scope.formData.storageUnit;

                if (input.newIdentification === undefined || input.newIdentification === '') {
                    return;
                }

                var newStorageUnit = {
                    uuid : input.uuid,
                    identifikator : input.newIdentification
                };

                var regexp = new RegExp("(" + storageUnitFormat + ")$");

                if (!regexp.test($scope.formData.storageUnit.newIdentification)) {
                    $scope.wrongFormat = "(Feil format i storageUnit)";
                    return false;
                }

                httpService.update(relativeUrl, newStorageUnit)
                    .success(function() {
                        $scope.formData.storageUnit.identification = newStorageUnit.identification;
                        $scope.formData.storageUnit.newIdentification = '';

                        $modalInstance.close();
                    }).error(function(data, status) {
                    if (status === 400){
                        if (data.length > 0){
                            var attribute = data[0].attribute;
                            if (attribute === 'identifikator'){
                                $scope.formData.error.identification = data[0].constraint;
                            }
                        }
                    } else {
                        errorService.errorCode(status);
                    }
                });
            };

            $scope.cancel = function(){
                $modalInstance.close();
            };
        };
        template.controller.$inject = ['$scope', '$modalInstance'];
        return $modal.open(template);
    }

    function warningFlyttLagringsenheter(tpl, relativeUrl, msg, title, desc, okFunction, uuids, identifikator){
        template.templateUrl = tpl;
        template.controller = function ($scope, $modalInstance) {
            $scope.formData = {
                "error" : {}
            };
            $scope.melding = msg;
            $scope.tittel = title;
            $scope.beskrivelse = desc;

            $scope.ok = function() {
                var data = {
                    pasientjournalUuids : uuids,
                    lagringsenhetIdentifikator : identifikator
                };
                httpService.create(relativeUrl, data)
                    .success(function(data, status, headers, config) {
                        $modalInstance.close();
                        okFunction();
                    }).error(function(data, status, headers, config) {
                    if (data[0].attribute==='identifikator'){
                        $modalInstance.close();
                        var msg = $filter('translate')('modal.FlyttLagringsenhetFeil.msg');
                        var title = $filter('translate')('modal.FlyttLagringsenhetFeil.title');
                        var desc = $filter('translate')('modal.FlyttLagringsenhetFeil.descr');
                        warningMessageModal(msg, title, desc);
                    }else{
                        errorService.errorCode(status);
                    }
                });

            };

            $scope.avbryt = function() {
                $modalInstance.close();
            };
        };
        template.controller.$inject = ['$scope', '$modalInstance'];
        return $modal.open(template);
    }

    function endrePassord(){
        template.templateUrl = 'common/modal-service/change-password-modal.tpl.html';
        template.controller = function ($scope, $modalInstance) {
            $scope.formData = {
                "error" : {}
            };

            $scope.ok = function() {
                $scope.formData.error.passord='';
                if (($scope.formData.passord !== $scope.formData.passordConfirm)){
                    $scope.formData.error.passord= $filter('translate')('home.brukere.PASSORD_ULIKT');
                    return;
                }
                httpService.create("admin/oppdaterPassord", $scope.formData.passord)
                    .success(function(data, status, headers, config) {
                        $modalInstance.close();
                    }).error(function(data, status, headers, config) {
                    errorService.errorCode(status);
                });
            };

            $scope.avbryt = function() {
                $modalInstance.close();
            };
        };
        template.controller.$inject = ['$scope', '$modalInstance'];
        return $modal.open(template);
    }

    function manageStorageUnits(templateUrl, callback, storageUnitFormat, storageUnits){
        template.templateUrl = templateUrl;

        template.controller = function( $scope, $modalInstance){
            $scope.storageUnitFormat = storageUnitFormat;

            $scope.formData = {
                "error" : {},
                "storageUnits": storageUnits
            };

            $scope.save = function(){
                if ($scope.newStorageUnit()){
                    callback($scope.formData);
                    $modalInstance.close();
                }
            };

            $scope.removeStorageUnit = function(unit){
                for (var i = 0; i < $scope.formData.storageUnits.length; i++) {
                    if (unit === $scope.formData.storageUnits[i]) {
                        $scope.formData.storageUnits.splice(i, 1);
                        document.getElementById("lagringsenhet").focus();
                    }
                }
            };

            $scope.newStorageUnit = function() {
                $scope.wrongFormat = undefined;

                if ($scope.formData.storageUnit === undefined || $scope.formData.storageUnit === '') {
                    return true;
                }

                if (storageUnitFormat) {
                    var regexp = new RegExp("(" + storageUnitFormat + ")$");

                    if (!regexp.test($scope.formData.storageUnit)) {
                        $scope.wrongFormat = "(Feil format i lagringsenhet)";
                        return false;
                    }
                }

                for (var i = 0; i < $scope.formData.storageUnits.length; i++) {
                    if ($scope.formData.storageUnit === $scope.formData.storageUnits[i]) {
                        $scope.formData.storageUnit = "";
                        return true;
                    }
                }

                $scope.formData.storageUnits.push($scope.formData.storageUnit);
                $scope.formData.storageUnit = "";
                return true;
            };

            $scope.print = function(){
                if ($scope.newStorageUnit()){
                    callback($scope.formData);
                    httpService.get("lagringsenheter/" + $scope.formData.lagringsenheter[0] + "/print")
                        .success(function() {
                            $modalInstance.close();
                        }).error(function(data, status) {
                        errorService.errorCode(status);
                    });
                }
            };

            $scope.cancel = function(){
                $modalInstance.close();
            };
        };

        template.controller.$inject = ['$scope', '$modalInstance'];

        return $modal.open(template);
    }


    return {
        deleteModal : deleteModal,
        nyModal : nyModal,
        endreModal : endreModal,
        warningModal : warningModal,
        warningMessageModal : warningMessageModal,
        velgModal : velgModal,
        manageStorageUnits : manageStorageUnits,
        warningFlyttLagringsenheter : warningFlyttLagringsenheter,
        changeStorageUnit : changeStorageUnit,
        endrePassord : endrePassord

    };

}