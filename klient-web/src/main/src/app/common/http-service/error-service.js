var mod = angular.module('nha.common.error-service', [
    'ui.bootstrap'
]);

mod.factory('errorService', ['$modal', '$location', errorService]);

function errorService($modal, $location) {
    var template = {
        backdrop: 'static',
        windowClass: "modal-center"
    };

    return {

        badRequest: function () {
            template.templateUrl = 'common/http-service/error-modal-400.tpl.html';
            template.controller = function ($scope, $modalInstance) {
                $scope.ok = function () {
                    $modalInstance.close();
                };
            };
            template.controller.$inject = ['$scope', '$modalInstance'];
            return $modal.open(template);
        },

        notFound: function (message) {
            template.templateUrl = 'common/http-service/error-modal-404.tpl.html';
            template.controller = function ($scope, $modalInstance) {
                $scope.message = message;
                $scope.ok = function () {
                    $modalInstance.close();
                };
            };
            template.controller.$inject = ['$scope', '$modalInstance'];
            return $modal.open(template);
        },

        resourceConflict: function () {
            template.templateUrl = 'common/http-service/error-modal-409.tpl.html';
            template.controller = function ($scope, $modalInstance) {
                $scope.ok = function () {
                    $modalInstance.close();
                };
            };
            template.controller.$inject = ['$scope', '$modalInstance'];
            return $modal.open(template);
        },

        serverError: function () {
            template.templateUrl = 'common/http-service/error-modal-500.tpl.html';
            template.controller = function ($scope, $modalInstance) {
                $scope.ok = function () {
                    $modalInstance.close();
                };
            };
            template.controller.$inject = ['$scope', '$modalInstance'];
            return $modal.open(template);
        },

        unauthorizedError: function(message) {
            template.templateUrl = 'common/http-service/error-modal-401.tpl.html';
            template.controller = function ($scope, $modalInstance) {
                $scope.message = message;
                $scope.ok = function () {
                    $modalInstance.close();
                };
            };
            template.controller.$inject = ['$scope', '$modalInstance'];
            return $modal.open(template);
        },
        expectationFailed: function (message) {
            template.templateUrl = 'common/http-service/error-modal-417.tpl.html';
            template.controller = function ($scope, $modalInstance) {
                $scope.message = message;
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
                case 401:
                    return this.unauthorizedError();
                case 409:
                    return this.resourceConflict();
                case 417:
                    return this.expectationFailed();
                default:
                    return this.serverError();
            }
        }
        
    };
}