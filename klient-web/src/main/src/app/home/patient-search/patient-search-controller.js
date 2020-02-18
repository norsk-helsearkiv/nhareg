angular.module('nha.home')

  .controller('PatientSearchCtrl', function($rootScope, $scope, $location, $filter, httpService, errorService,
                                            listService, modalService, registerService, stateService) {
    //$scope.sok = stateService.sokState;
    $scope.sok = {};

    var baseEndpointUrl = "pasientjournaler/";
    
    $scope.lagringsenhetAsc = false;
    $scope.fodselsnummerAsc = false;
    $scope.fanearkidAsc = false;
    $scope.jnrAsc = false;
    $scope.lnrAsc = false;
    $scope.navnAsc = false;
    $scope.faarAsc = false;
    $scope.daarAsc = false;
    $scope.oppdatertAvAsc = false;
    $scope.sortDirection = null;
    $scope.sortColumn = null;
    $scope.sok = stateService.sokState;

    $scope.actionCleanSearch = function () {
      $scope.sok.lagringsenhet = '';
      $scope.sok.fanearkId = '';
      $scope.sok.fodselsnummer = '';
      $scope.sok.navn = '';
      $scope.sok.fodt = '';
      $scope.sok.oppdatertAv = '';
      $scope.sok.sistOppdatert = '';
    };
    
    if (listService.getClean()) {
      $scope.actionCleanSearch();
      listService.setClean(false);
    }

    $scope.actionSearch = function () {
      var sok = {
        sokLagringsenhet: $scope.sok.lagringsenhet,
        sokFanearkId: $scope.sok.fanearkId,
        sokFodselsnummer: $scope.sok.fodselsnummer,
        sokNavn: $scope.sok.navn,
        sokFodt: $scope.sok.fodt,
        sokOppdatertAv: $scope.sok.oppdatertAv,
        sokSistOppdatert: $scope.sok.sistOppdatert
      };

      registerService.setAvlevering(undefined);
      registerService.setAvleveringsidentifikator(undefined);
      listService.setSok(sok);
      
      stateService.sokState = $scope.sok;
      var size = listService.getSize();
      listService.setTitle($filter('translate')('home.SOKERESULTAT'));
      
      httpService.getAll(baseEndpointUrl + "?page=1&size=" + size + listService.getQuery())
        .then(function (response) {
          var data = response.data;
          var subtitle = listService.createSubtitle(data);

          listService.setSubtitle(subtitle);
          listService.setData(data);
          listService.setSok(sok);

          $scope.data = data;
          $rootScope.$emit("UpdatePager", 1);

          $location.path('/list');
        }, function (response) {
          errorService.errorCode(response.status);
        });
    };

  });