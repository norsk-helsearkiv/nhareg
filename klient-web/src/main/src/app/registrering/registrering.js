angular.module( 'nha.registrering', [
  'ui.router',
  'nha.registrering.registrering-service'
])

.config(function config( $stateProvider ) {
  $stateProvider.state( 'registrer', {
    url: '/registrer',
    views: {
      "main": {
        controller: 'RegistrerCtrl',
        templateUrl: 'registrering/registrering.tpl.html'
      }
    }
  });
})

.controller( 'RegistrerCtrl', function HomeController($scope, $location, $filter, registreringService) {
  $scope.$watch(
    function() { return $filter('translate')('home.SOK'); },
    function(newval) { $scope.sok = newval; }
  );

  console.log(registreringService.getAvlevering());
});