angular.module( 'nha.login', [
  'ui.router',
  'nha.common.authentication-service',
    'nha.common.auth-http-request-interceptor',
  'nha.common.error-service'
])

.config(function config( $stateProvider ) {
  $stateProvider.state( 'login', {
    url: '/login',
    views: {
      "main": {
        controller: 'LoginCtrl',
        templateUrl: 'login/login.tpl.html'
      }
    }
  });
})

.controller( 'LoginCtrl', function HomeController($scope, $location, $filter, authenticationService, errorService) {
  $scope.formLogin = {};
  $scope.feilmeldinger = false;
  
  //Henter tekster fra fil
  $scope.$watch(
    function() { return $filter('translate')('login.BRUKERNAVN'); },
    function(newval) { $scope.brukernavn = newval; }
  );
  $scope.$watch(
    function() { return $filter('translate')('login.PASSORD'); },
    function(newval) { $scope.passord = newval; }
  );

  $scope.submit = function() {
    $scope.feilmeldinger = false;

    authenticationService.login($scope.formLogin)
    .success(function(data, status, headers, config) {
      authenticationService.setAuthData(data);
      $location.path('/');
    }).error(function(data, status, headers, config) {
      if(status === 401) {
        $scope.feilmeldinger = true;
      } else {
        errorService.errorCode(status);
      }
    });
  };

});