angular.module('nha.home')

  .controller('UserAdminCtrl', function($rootScope, $scope, $location, $filter, httpService, errorService){
    $scope.bruker = {};
    $scope.bruker.printerzpl = '127.0.0.1';
    $scope.brukere = [];
    $scope.selectedBrukerRow = null;
    $scope.error = [];

    $scope.selectUser = function (selectedUser, index) {
      if (index === $scope.selectedBrukerRow) {
        $scope.selectedBrukerRow = null;
        $scope.bruker.brukernavn = null;
        $scope.bruker.rolle = null;
        $scope.bruker.printerzpl = null;
      } else {
        var rolleIndex = $scope.roller.map(function (e) {
          return e.navn;
        }).indexOf(selectedUser.rolle.navn);
        $scope.selectedBrukerRow = index;
        $scope.bruker.brukernavn = selectedUser.brukernavn;
        $scope.bruker.printerzpl = selectedUser.printerzpl;
        $scope.bruker.rolle = $scope.roller[rolleIndex];
      }
    };

    $scope.getUsers = function () {
      httpService.getAll("admin/brukere", false)
        .success(function (data) {
          $scope.brukere = data;
        }).error(function (data, status) {
        errorService.errorCode(status);
      });
    };

    var resetUser = function () {
      $scope.bruker = {'printerpzl': $scope.bruker.printerpzl };
    };

    var arePasswordsIdentical = function () {
      return $scope.bruker.password === $scope.bruker.passwordConfirm;
    };

    $scope.updateUser = function () {
      $scope.error = [];

      if (!arePasswordsIdentical()) {
        $scope.error['passord'] = $filter('translate')('formError.NotIdentical');
        return;
      }

      httpService.create("admin/brukere", $scope.bruker)
        .success(function () {
          $scope.getUsers();
          resetUser();
        })
        .error(function (data, status) {
          if (status !== 400) {
            errorService.errorCode(status);
          } else {
            setErrorMessages(data);
          }
        });
    };

    $scope.showError = function (attribute) {
      return $scope.error[attribute] !== undefined;
    };

    var setErrorMessages = function (data) {
      angular.forEach(data, function (element) {
        $scope.error[element.attribute] = $filter('translate')('formError.' + element.constraint);
      });
    };
  });