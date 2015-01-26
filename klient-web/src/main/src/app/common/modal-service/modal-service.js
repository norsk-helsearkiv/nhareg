var mod = angular.module('nha.common.modal-service', [
    'ui.bootstrap',
    'nha.common.http-service',
    'nha.common.error-service'
]);

mod.factory('modalService', ['$modal', 'httpService', 'errorService', modalService]);

function modalService($modal, httpService, errorService) {
    var template = {
        backdrop: 'static',
        windowClass: "modal-center"
    };

    var fjern = function(list, element) {
        for(var i = 0; i < list.length; i++) {
            if(element === list[i]) {
                list.splice(i, 1);
            }
        }
    };

    /*
        Sletter et element

        elementType: Typen av elementet som skal slettes
        id: iden til elementet
        list: liste over alle elementer i samlingen, dette for å kunne
            fjerne elemntet fra listen når det er slettet
        element: Det fulle elementet som skal slettes, for å kunne finne
            det i listen over elementet
        okFunction: overskrevet metoden for å kunne validere på OK knappen
    */
    function deleteModal(elementType, id, list, element, okFunction) {
        template.templateUrl = 'common/modal-service/delete-modal.tpl.html';
        template.controller = function ($scope, $modalInstance) {
            $scope.elementType = elementType;
            $scope.id = id;

            $scope.ok = function() {
                fjern(list, element);
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
        okFunction: overskrevet metode for validering, returnerer boolean om det
            gikk bra.
    */
    function nyModal(tpl, list, relativUrl, okFunction) {
        template.templateUrl = tpl;
        template.controller = function ($scope, $modalInstance) {
            $scope.formData = {
              "error" : {}
            };

            $scope.ok = function() {
                var success = okFunction($scope.formData);
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

    return {
        deleteModal : deleteModal,
        nyModal : nyModal
    };

}