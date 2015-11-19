angular.module( 'nha', [
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
  //'nha.login',
  'nha.registrering',
  'nha.registrering.registrering-service'
])

.config( function myAppConfig ($stateProvider, $urlRouterProvider, $translateProvider, $httpProvider) {
  $urlRouterProvider.otherwise( '/' );

	$translateProvider.useStaticFilesLoader({
		prefix: 'assets/i18n/',
		suffix: '.json'
	});
	$translateProvider.preferredLanguage('nb');

    var interceptor = ['$rootScope', '$q', '$window', function(scope, $q, $window) {

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
                //scope.requests401.push(req);
                //scope.$broadcast('event:loginRequired');
                $window.location.reload();
                return deferred.promise;
            }
            // otherwise
            return $q.reject(response);

        }

        return function(promise) {
            return promise.then(success, error);
        };

    }];
    $httpProvider.responseInterceptors.push(interceptor);
        /*
    $httpProvider.responseInterceptors.push(function (scope, $q, $log) {
        function success(response) {
            $log.info('Successful response: ' + response);
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
                $log.error('event:loginRequired broadcasted: ' + status + '. ' + response);
                scope.$broadcast('event:loginRequired');
                return deferred.promise;
            }
            $log.error('Response status: ' + status + '. ' + response);
            return $q.reject(response); //similar to throw response;
        }
        return function(promise) {
            return promise.then(success, error);
        };
    });*/
})

.directive('ngEnter', function() {
  return function (scope, element, attrs) {
    element.bind("keydown keypress", function (event) {
        if(event.which === 13) {
            scope.$apply(function (){
                scope.$eval(attrs.ngEnter);
            });

            event.preventDefault();
        }
    });
  };
})

.controller( 'AppCtrl', function AppCtrl ($http, $scope, $location, diagnoseService ) {
});

