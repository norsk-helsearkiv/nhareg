var mod = angular.module('nha.home.avleveringer-service', [
  'ui.bootstrap',
  'nha.common.http-service',
  'nha.common.error-service'
]);

mod.factory('avleveringerService', ['$modal', 'httpService', 'errorService', avleveringerService]);

function avleveringerService($modal, httpService, errorService) {
    var template = {
        backdrop: 'static',
        windowClass: "modal-center"
    };

    var avleveringer;

    function leggTil() {
      template.templateUrl = 'home/modal-avlevering-add.tpl.html';
      template.controller = function ($scope, $modalInstance) {

        $scope.formData = {
          "error" : {}
        };

        $scope.ok = function () {
          var validated = true;
          if($scope.formData.avleveringsidentifikator === undefined || $scope.formData.avleveringsidentifikator === '') {
            $scope.formData.error.avleveringsidentifikator = "Identifikator kan ikke være null";
            validated = false;
          }

          if(validated) {
            console.log("TODO: server kall legge til");

            avleveringer.push($scope.formData);
            $modalInstance.close();
          }
        };

        $scope.avbryt = function() {
            $modalInstance.close();
        };
      };
      template.controller.$inject = ['$scope', '$modalInstance'];
      return $modal.open(template);
    }

    function setAvleveringer(avtale) {
      httpService.getAvleveringer(avtale.avtaleidentifikator)
      .success(function(data, status, headers, config) {
        avleveringer = data.avleveringer;
      }).error(function(data, status, headers, config) {
        errorService.errorCode(status);
      });
    }

    function getAvleveringer() {
      return avleveringer;
    }

    function slett() {
      console.log("slett");
    }

    return {
      leggTil: leggTil,
      slett: slett,

      setAvleveringer: setAvleveringer,
      getAvleveringer: getAvleveringer
    };

}