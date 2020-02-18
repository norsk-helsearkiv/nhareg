angular.module('nha.home')

  .controller('UserAdminCtrl', function($rootScope, $scope, $location, $filter, httpService, errorService){
    $scope.createNewUser = true;
    $scope.bruker = {};
    $scope.bruker.printer = '127.0.0.1';
    $scope.brukere = [];
    $scope.selectedBrukerRow = null;
    $scope.error = [];

    $scope.selectUser = function (selectedUser, index) {
      if (index === $scope.selectedBrukerRow) {
        $scope.createNewUser = true;
        $scope.selectedBrukerRow = null;
        $scope.bruker.brukernavn = null;
        $scope.bruker.rolle = null;
        $scope.bruker.printer = '127.0.0.1';
      } else {
        var rolleIndex = $scope.roller.map(function (e) {
          return e.navn;
        }).indexOf(selectedUser.rolle.navn);
        $scope.createNewUser = false;
        $scope.selectedBrukerRow = index;
        $scope.bruker.brukernavn = selectedUser.brukernavn;
        $scope.bruker.printer = selectedUser.printer;
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
      var printer = $scope.bruker.printer;
      $scope.bruker = {};
      $scope.bruker.printer = printer;
      $scope.createNewUser = true;
      $scope.selectUser(null, $scope.selectedBrukerRow);
    };

    var arePasswordsIdentical = function () {
      return $scope.bruker.password === $scope.bruker.passwordConfirm;
    };

    $scope.createOrUpdateUser = function() {
      $scope.error = [];

      if (!arePasswordsIdentical()) {
        $scope.error['passord'] = $filter('translate')('formError.NotIdentical');
        return;
      }

      if ($scope.createNewUser) {
        $scope.createUser();
      } else {
        $scope.updateUser();
      }
    };

    $scope.createUser = function () {
      httpService.create("admin/brukere", $scope.bruker)
        .then(function () {
          $scope.getUsers();
          resetUser();
        }, function (reason) {
          if (reason.status !== 400) {
            errorService.errorCode(reason.status);
          } else {
            setErrorMessages(reason.data);
          }
        });
    };

    $scope.updateUser = function () {
      httpService.update("admin/brukere", $scope.bruker)
        .then(function() {
          $scope.getUsers();
          resetUser();
        }, function (reason) {
          if (reason.status !== 400) {
            errorService.errorCode(reason.status);
          } else {
            setErrorMessages(reason.data);
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