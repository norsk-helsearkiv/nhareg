var mod = angular.module('nha.common.error-service', [
    'ui.bootstrap'
]);

mod.factory('errorService', ['$modal', '$filter', '$location', errorService]);

function errorService($modal, $filter) {
    var template = {
        backdrop: 'static',
        windowClass: "modal-center"
    };

    return {

        //ERROR 400 BAD REQUEST
        badRequest: function (message) {
            template.templateUrl = 'common/http-service/error-modal-400.tpl.html';

            template.controller = function ($scope, $modalInstance) {
                if (!message) {
                    $scope.message = $filter('translate')('error.BAD_REQUEST');
                } else {
                    $scope.message = message;
                }
                $scope.ok = function () {
                    $modalInstance.close();
                };
            };

            template.controller.$inject = ['$scope', '$modalInstance'];

            return $modal.open(template);
        },

        //ERROR 401 UNAUTHORIZED ERROR
        unauthorizedError: function (message) {
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

        //ERROR 404 NOT FOUND
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

        //ERROR 409 RESOURCE CONFLICT
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

        //ERROR 417 EXPECTATION FAILED
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

        //ERROR 500 SERVER ERROR
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

        errorCode: function (status, message) {
            switch(status){
                case 400:
                    return this.badRequest(message);
                case 401:
                    return this.unauthorizedError();
                case 403:
                    return this.unauthorizedError();
                case 404:
                    return this.notFound();
                case 409:
                    return this.resourceConflict();
                case 417:
                    return this.expectationFailed();
                case 422:
                    return this.notFound();
                default:
                    return this.serverError();
            }
        }

    };
}