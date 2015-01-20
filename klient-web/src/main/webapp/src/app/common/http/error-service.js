var mod = angular.module('nha.common.error-service', [
    'ui.bootstrap'
]);

mod.factory('errorService', ['$modal', errorService]);

function errorService($modal) {
    var template = {
        backdrop: 'static',
        windowClass: "modal-center"
    };

    return {

        badRequest: function () {
            template.templateUrl = 'common/http/error-modal-400.tpl.html';
            template.controller = function ($scope, $modalInstance) {
                $scope.ok = function () {
                    $modalInstance.close();
                };
            };
            template.controller.$inject = ['$scope', '$modalInstance'];
            return $modal.open(template);
        },

        notFound: function (message) {
            template.templateUrl = 'common/http/error-modal-404.tpl.html';
            template.controller = function ($scope, $modalInstance) {
                $scope.message = message;
                $scope.ok = function () {
                    $modalInstance.close();
                };
            };
            template.controller.$inject = ['$scope', '$modalInstance'];
            return $modal.open(template);
        },

        serverError: function () {
            template.templateUrl = 'common/http/error-modal-500.tpl.html';
            template.controller = function ($scope, $modalInstance) {
                $scope.ok = function () {
                    $modalInstance.close();
                };
            };
            template.controller.$inject = ['$scope', '$modalInstance'];
            return $modal.open(template);
        },

        errorCode: function (status) {
            switch(status){
                case 400:
                    return this.badRequest();
                case 404:
                    return this.notFound();
                case 422:
                    return this.notFound();
                default:
                    return this.serverError();
            }
        }
        
    };
}