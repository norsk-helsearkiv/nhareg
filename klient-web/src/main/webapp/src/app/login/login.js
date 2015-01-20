angular.module( 'nha.login', [
  'ui.router'
])

.config(["$stateProvider", function config( $stateProvider ) {
  $stateProvider.state( 'login', {
    url: '/login',
    views: {
      "main": {
        controller: 'LoginCtrl',
        templateUrl: 'login/login.tpl.html'
      }
    }
  });
}])

.controller( 'LoginCtrl', ["$scope", "$location", "$filter", function HomeController( $scope, $location, $filter ) {

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
    console.log("TODO: logikk i LoginCtrl");
    $location.path('/home');
  };
  
}]);