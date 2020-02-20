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
     valideringFunction: overskrevet metode for validering, returnerer boolean om det gikk bra.
     */
    function nyModal(templateUrl, list, relativUrl, valideringFunction, allArchiveAuthors) {
        template.templateUrl = templateUrl;
        template.controller = function ($scope, $modalInstance) {
            $scope.formData = {
                "error" : {}
            };
            $scope.allArchiveAuthors = allArchiveAuthors;

            $scope.ok = function() {
                var success = valideringFunction($scope.formData);
                if (success) {
                    httpService.create(relativUrl, $scope.formData)
                        .then(function (response) {
                            var data = response.data;
                            data.lagringsenhetformat = $scope.formData.lagringsenhetformat;
                            list.push(data);
                        }, function (response) {
                            errorService.errorCode(response.status);
                        }).then(function () {
                            $modalInstance.close();
                        });
                }
            };

            $scope.avbryt = function() {
                $modalInstance.close();
            };
        };
        template.controller.$inject = ['$scope', '$modalInstance'];

        return $modal.open(template);
    }

    function warningMessageModal(msg, title, desc) {
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

    function warningModal(templateUrl, relativeUrl, msg, title, desc, okFunction) {
        template.templateUrl = templateUrl;
        template.controller = function ($scope, $modalInstance) {
            $scope.formData = {
                "error" : {}
            };
            $scope.melding = msg;
            $scope.tittel = title;
            $scope.beskrivelse = desc;

            $scope.ok = function() {
                if (relativeUrl) {
                    httpService.create(relativeUrl, $scope.formData)
                        .then(function() {
                            $modalInstance.close();
                            okFunction();
                        }, function(response) {
                            errorService.errorCode(response.status);
                        });
                } else {
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

    function endreModal(templateUrl, list, relativUrl, valideringFunction, entitet, allArchiveAuthors) {
        template.templateUrl = templateUrl;
        template.controller = function ($scope, $modalInstance) {
            $scope.formData = entitet;
            $scope.erEndring = true;
            $scope.allArchiveAuthors = allArchiveAuthors;

            $scope.ok = function() {
                var success = valideringFunction($scope.formData);
                if (success) {
                    httpService.update(relativUrl, $scope.formData)
                        .then(function() {},
                            function (response) {
                                errorService.errorCode(response.status);
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

    function velgModal(templateUrl, list, formDiagnose) {
        template.templateUrl = templateUrl;
        template.controller = function ($scope, $modalInstance) {
            hotkeys.bindTo($scope)
                .add({
                    combo: 'down',
                    callback: function () {
                        if(valgtIndex === $scope.modalListe.length - 1) {
                            $scope.oppdaterValg($scope.modalListe[valgtIndex]);
                            return;
                        }
                        $scope.oppdaterValg($scope.modalListe[++valgtIndex]);
                    }
                })
                .add({
                    combo: 'up',
                    callback: function () {
                        if (valgtIndex === 0) {
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

            var resetValg = function () {
                angular.forEach($scope.modalListe, function (e) {
                    e.selected = false;
                });
            };

            $scope.oppdaterValg = function (element) {
                resetValg();

                for(var i = 0; i < $scope.modalListe.length; i++) {
                    if(element === $scope.modalListe[i]) {
                        $scope.modalListe[i].selected = true;
                        valgtIndex = i;
                    }
                }
            };

            $scope.oppdaterValg($scope.modalListe[0]);

            $scope.ok = function () {
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

    function changeStorageUnit(templateUrl, relativeUrl, storageUnit, storageUnitFormat) {
        template.templateUrl = templateUrl;

        template.controller = function ($scope, $modalInstance) {
            $scope.storageUnitFormat = storageUnitFormat;

            $scope.formData = {
                "error" : {},
                "storageUnit": storageUnit
            };

            $scope.save = function () {
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
                    .then(function () {
                        $scope.formData.storageUnit.identification = newStorageUnit.identification;
                        $scope.formData.storageUnit.newIdentification = '';

                        $modalInstance.close();
                    }, function (response) {
                        var status = response.status;
                        var data = response.data;

                        if (status === 400) {
                            if (data.length > 0) {
                                var attribute = data[0].attribute;
                                if (attribute === 'identifikator') {
                                    $scope.formData.error.identification = data[0].constraint;
                                }
                            }
                        } else {
                            errorService.errorCode(status);
                        }
                    });
            };

            $scope.cancel = function () {
                $modalInstance.close();
            };
        };
        template.controller.$inject = ['$scope', '$modalInstance'];
        return $modal.open(template);
    }

    function warningFlyttLagringsenheter(templateUrl, relativeUrl, msg, title, desc,
                                         okFunction, uuids, identifikator) {
        template.templateUrl = templateUrl;
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
                    .then(function() {
                        $modalInstance.close();
                        okFunction();
                    }, function(response) {
                        var data = response.data;
                        var status = response.status;

                        if (data[0].attribute === 'identifikator') {
                            $modalInstance.close();
                            var msg = $filter('translate')('modal.FlyttLagringsenhetFeil.msg');
                            var title = $filter('translate')('modal.FlyttLagringsenhetFeil.title');
                            var desc = $filter('translate')('modal.FlyttLagringsenhetFeil.descr');
                            warningMessageModal(msg, title, desc);
                        } else {
                            errorService.errorCode(status);
                        }
                    });
            };

            $scope.avbryt = function () {
                $modalInstance.close();
            };
        };
        template.controller.$inject = ['$scope', '$modalInstance'];

        return $modal.open(template);
    }

    function endrePassord() {
        template.templateUrl = 'common/modal-service/change-password-modal.tpl.html';
        template.controller = function ($scope, $modalInstance) {
            $scope.formData = {
                "error" : {}
            };

            $scope.ok = function () {
                $scope.formData.error.passord = '';

                if (($scope.formData.passord !== $scope.formData.passordConfirm)) {
                    $scope.formData.error.passord= $filter('translate')('home.brukere.PASSORD_ULIKT');
                    return;
                }

                httpService.create("admin/oppdaterPassord", $scope.formData.passord)
                    .then(function () {
                        $modalInstance.close();
                    }, function (response) {
                        errorService.errorCode(response.status);
                    });
            };

            $scope.avbryt = function () {
                $modalInstance.close();
            };
        };
        template.controller.$inject = ['$scope', '$modalInstance'];

        return $modal.open(template);
    }

    function manageStorageUnits(templateUrl, callback, storageUnitFormat, storageUnits) {
        template.templateUrl = templateUrl;

        template.controller = function ($scope, $modalInstance) {
            $scope.storageUnitFormat = storageUnitFormat;

            $scope.formData = {
                "error" : {},
                "storageUnits": storageUnits
            };

            $scope.save = function () {
                if ($scope.newStorageUnit()) {
                    callback($scope.formData);
                    $modalInstance.close();
                }
            };

            $scope.removeStorageUnit = function (unit) {
                for (var i = 0; i < $scope.formData.storageUnits.length; i++) {
                    if (unit === $scope.formData.storageUnits[i]) {
                        $scope.formData.storageUnits.splice(i, 1);
                        document.getElementById("lagringsenhet").focus();
                    }
                }
            };

            $scope.newStorageUnit = function () {
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

            $scope.print = function () {
                if ($scope.newStorageUnit()) {
                    callback($scope.formData);
                    httpService.get("lagringsenheter/" + $scope.formData.lagringsenheter[0] + "/print")
                        .then(function () {
                            $modalInstance.close();
                        }, function (response) {
                            errorService.errorCode(response.status);
                        });
                }
            };

            $scope.cancel = function () {
                $modalInstance.close();
            };
        };

        template.controller.$inject = ['$scope', '$modalInstance'];

        return $modal.open(template);
    }

    function manageArchiveAuthors(templateUrl, callback, selectedArchiveAuthors, allArchiveAuthors) {
        template.templateUrl = templateUrl;

        template.controller = function ($scope, $modalInstance) {
            $scope.allArchiveAuthors = allArchiveAuthors;
            $scope.formData = {
                "archiveAuthors": selectedArchiveAuthors
            };

            $scope.save = function () {
                if ($scope.newArchiveAuthor()) {
                    callback($scope.formData);
                    $modalInstance.close();
                }
            };

            $scope.removeArchiveAuthor = function (archiveAuthor) {
                for (var i = 0; i < $scope.formData.archiveAuthors.length; i++) {
                    if (archiveAuthor === $scope.formData.archiveAuthors[i]) {
                        $scope.formData.archiveAuthors.splice(i, 1);
                        document.getElementById("archiveAuthor").focus();
                    }
                }
            };

            $scope.newArchiveAuthor = function () {
                if ($scope.formData.archiveAuthor === undefined || $scope.formData.archiveAuthor === '') {
                    return true;
                }

                for (var i = 0; i < $scope.formData.archiveAuthors.length; i++) {
                    if ($scope.formData.archiveAuthor === $scope.formData.archiveAuthors[i]) {
                        $scope.formData.archiveAuthor = "";
                        return true;
                    }
                }

                $scope.formData.archiveAuthors.push($scope.formData.archiveAuthor);
                $scope.formData.archiveAuthor = "";
                return true;
            };

            $scope.cancel = function () {
                $modalInstance.close();
            };
        };

        template.controller.$inject = ['$scope', '$modalInstance'];

        return $modal.open(template);
    }

    function openSelectModal(templateUrl, items, validateFunction) {
        var selectModalInstance = function ($scope, $modalInstance) {
            $scope.items = items;
            $scope.selected = $scope.items[0];
            
            var selectPrev = function () {
                var list = $scope.items;
                var selected = $scope.selected;
                for (var i = 1; i < list.length; i++) {
                    if (list[i] === selected) {
                        return list[i - 1];
                    }
                }
                
                return $scope.selected;
            };

            function selectNext() {
                var list = $scope.items;
                var selected = $scope.selected;
                for (var i = 0; i < list.length - 1; i++) {
                    if (list[i] === selected) {
                        return list[i + 1];
                    }
                }
                
                return $scope.selected;
            }

            hotkeys.bindTo($scope)
                .add({
                    combo: 'enter',
                    callback: function() {
                        $scope.ok();
                    }
                })
                .add({
                    combo: 'up',
                    callback: function() {
                        $scope.selected = selectPrev();
                    }
                })
                .add({
                    combo: 'down',
                    callback: function() {
                        $scope.selected = selectNext();
                    }
                });
            
            $scope.updateSelected = function (item) {
                $scope.selected = item;
            };

            $scope.ok = function () {
                if (validateFunction) {
                    validateFunction();
                }
                
                $modalInstance.close($scope.selected);
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        };

        template.templateUrl = templateUrl;
        template.controller = selectModalInstance;
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
        manageArchiveAuthors : manageArchiveAuthors,
        warningFlyttLagringsenheter : warningFlyttLagringsenheter,
        changeStorageUnit : changeStorageUnit,
        endrePassord : endrePassord,
        openSelectModal : openSelectModal
    };

}