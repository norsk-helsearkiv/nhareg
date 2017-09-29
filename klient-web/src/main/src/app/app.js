angular.module('nha', [
        'templates-app',
        'templates-common',
        'ui.router',
        'cfp.hotkeys',
        'tableSort',
        'underscore',
        'pascalprecht.translate',
        'nha.common.error-service',
        'nha.common.http-service',
        'nha.common.modal-service',
        'nha.common.list-service',
        'nha.common.list-view',
        'nha.common.diagnose-service',
        'nha.home',
        'nha.state',
        //'nha.login',
        'nha.registrering',
        'nha.registrering.registrering-service'
    ])

    .config(function myAppConfig($stateProvider, $urlRouterProvider, $translateProvider, $httpProvider) {
        $urlRouterProvider.otherwise('/');

        $translateProvider.useStaticFilesLoader({
            prefix: 'assets/i18n/',
            suffix: '.json'
        });
        $translateProvider.preferredLanguage('nb');

        var interceptor = ['$rootScope', '$q', '$window', function (scope, $q, $window) {

            function success(response) {
                return response;
            }

            function error(response) {
                var status = response.status;

                if (status === 403) {
                    var deferred = $q.defer();
                    var req = {
                        config: response.config,
                        deferred: deferred
                    };
                    // Refresh token!
                    $injector.get('AuthenticationFactory').getToken().then(function (token) {
                        response.config.headers.Authorization = token;

                        $http(response.config).then(deferred.resolve, deferred.reject);
                    });

                    return deferred.promise;
                }
                // otherwise
                return $q.reject(response);

            }

            return function (promise) {
                return promise.then(success, error);
            };

        }];
        $httpProvider.responseInterceptors.push(interceptor);
    })

    .directive('ngEnter', function () {
        return function (scope, element, attrs) {
            element.bind("keydown keypress", function (event) {
                if (event.which === 13) {
                    scope.$apply(function () {
                        scope.$eval(attrs.ngEnter);
                    });

                    event.preventDefault();
                }
            });
        };
    })

    .directive('focusToMe', function($timeout) {
        return {
            restrict: 'A',
            compile: function() {
                var directiveName = this.name;

                return function(scope, elem, attrs) {
                    scope.$watch(attrs[directiveName], function(newVal, oldVal) {
                        if (newVal) {
                            $timeout(function() {
                                elem[0].focus();
                            }, 0);
                        }
                    });
                };
            }
        };
    })

    .controller('AppCtrl', function AppCtrl($http, $scope, $location, diagnoseService) {
    });

