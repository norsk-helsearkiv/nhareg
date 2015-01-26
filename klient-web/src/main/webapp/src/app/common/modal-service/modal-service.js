var mod = angular.module('nha.common.modal-service', [
    'ui.bootstrap'
]);

mod.factory('modalService', ['$modal', modalService]);

function modalService($modal) {
    var template = {
        backdrop: 'static',
        windowClass: "modal-center"
    };

    function deleteModal(element, id) {
        template.templateUrl = 'common/modal-service/delete-modal.tpl.html';
        template.controller = function ($scope, $modalInstance) {
            $scope.element = element;
            $scope.id = id;

            $scope.ok = function () {
                console.log("TODO: delete");
                $modalInstance.close();
            };

            $scope.avbryt = function() {
                $modalInstance.close();
            };
        };
        template.controller.$inject = ['$scope', '$modalInstance'];
        return $modal.open(template);
    }

    return {
        deleteModal : deleteModal
    };

}