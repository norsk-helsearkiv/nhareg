var mod = angular.module('nha.common.modal-service', [
    'ui.bootstrap',
    'nha.common.http-service',
    'nha.common.error-service',
    'cfp.hotkeys'
]);

mod.factory('modalService', ['$modal', 'httpService', 'errorService', 'hotkeys', modalService]);

function modalService($modal, httpService, errorService, hotkeys) {
    var template = {
        backdrop: 'static',
        windowClass: "modal-center"
    };

    /*
        Sletter et element
        elementType: Typen av elementet som skal slettes
        id: iden til elementet
        okFunction: overskrevet metoden for å kunne validere på OK knappen
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

            $scope.avbryt = function() {
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
                    httpService.ny(relativUrl, $scope.formData)
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
                httpService.ny(relativeUrl, $scope.formData)
                    .success(function(data, status, headers, config) {
                        $modalInstance.close();
                        okFunction();
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

    function endreModal(tpl, list, relativUrl, valideringFunction, entitet) {
        template.templateUrl = tpl;
        template.controller = function ($scope, $modalInstance) {
            $scope.formData = entitet;
            $scope.erEndring = true;

            $scope.ok = function() {
                var success = valideringFunction($scope.formData);
                if(success) {
                    httpService.oppdater(relativUrl, $scope.formData)
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

    function endreLagringsenhet(tpl, relativUrl, lagringsenhet){
        template.templateUrl = tpl;
        template.controller = function( $scope, $modalInstance) {
            $scope.formData = {
                "error" : {},
                "lagringsenhet": lagringsenhet
            };
            $scope.lagre = function(){
                var lagr = $scope.formData.lagringsenhet;
                if (lagr.nyIdentifikator === undefined || lagr.nyIdentifikator === '') {
                    return;
                }
                var nyLagringsenhet = {
                    uuid :lagr.uuid,
                    identifikator : lagr.nyIdentifikator
                };

                httpService.oppdater(relativUrl, nyLagringsenhet)
                    .success(function(data, status, headers, config) {
                        $scope.formData.lagringsenhet.identifikator = nyLagringsenhet.identifikator;
                        $scope.formData.lagringsenhet.nyIdentifikator = '';
                        $modalInstance.close();
                    }).error(function(data, status, headers, config) {
                        if (status==400){
                            if (data.length>0){
                                var attr = data[0].attributt;
                                if (attr === 'identifikator'){
                                    $scope.formData.error.identifikator = data[0].constriant;
                                }
                            }
                        }else{
                            errorService.errorCode(status);
                        }
                    });
            };

            $scope.avbryt = function(){
                $modalInstance.close();
            };
        };
        template.controller.$inject = ['$scope', '$modalInstance'];
        return $modal.open(template);
    }
    
    function velgLagringsenhet(tpl, callback, lagringsenhetmaske, lagringsenheter){
        template.templateUrl = tpl;
        template.controller = function( $scope, $modalInstance){
            $scope.txtMaske = lagringsenhetmaske;

            $scope.formData = {
                "error" : {},
                "lagringsenheter": lagringsenheter
            };
            $scope.lagre = function(){
                if ($scope.nyLagringsenhet()){
                    callback($scope.formData);
                    $modalInstance.close();
                }

            };
            $scope.actionFjernLagringsenhet = function(enhet){
                for (var i = 0; i < $scope.formData.lagringsenheter.length; i++) {
                    if (enhet === $scope.formData.lagringsenheter[i]) {
                        $scope.formData.lagringsenheter.splice(i, 1);
                        document.getElementById("lagringsenhet").focus();
                    }
                }
            };

            $scope.nyLagringsenhet = function() { //legger til en ny lagringsenhet i listen..
                $scope.feilFormat = undefined;
                if ($scope.formData.lagringsenhet === undefined || $scope.formData.lagringsenhet === '') {
                    return true;
                }
                if (lagringsenhetmaske !== undefined || lagringsenhetmaske !== "") {
                    var regexp = new RegExp("(" + lagringsenhetmaske + ")$");
                    if (!regexp.test($scope.formData.lagringsenhet)) {
                        $scope.feilFormat = "(Feil format i lagringsenhet)";
                        return false;
                    }
                }
                for (var i = 0; i < $scope.formData.lagringsenheter.length; i++) {
                    if ($scope.formData.lagringsenhet === $scope.formData.lagringsenheter[i]) {
                        $scope.formData.lagringsenhet = "";
                        return true;
                    }
                }
                $scope.formData.lagringsenheter.push($scope.formData.lagringsenhet);
                $scope.formData.lagringsenhet = "";
                return true;
            };
            $scope.utskrift = function(){
                $modalInstance.close();
            };

            $scope.avbryt = function(){
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
        velgModal : velgModal,
        velgLagringsenhet : velgLagringsenhet,
        endreLagringsenhet : endreLagringsenhet

    };

}