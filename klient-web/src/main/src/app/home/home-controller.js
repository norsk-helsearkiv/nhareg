angular.module('nha.home', [
    'ui.router',
    'nha.common.http-service',
    'nha.common.error-service',
    'nha.common.list-service',
    'nha.common.modal-service',
    'nha.register.register-service'
])

  .filter('parseCustomDate', function() {
      return function(date){
          var d = new Date(date);
          return d.getTime();
      };
  })

  .config(function config($stateProvider) {
      $stateProvider.state('home', {
          url: '/',
          views: {
              "main": {
                  controller: 'HomeCtrl',
                  templateUrl: 'home/home.tpl.html'
              }
          }
      });
      $stateProvider.state('storageUnits', {
          url: '/lagringsenheter',
          views: {
              "main": {
                  controller: 'HomeCtrl',
                  templateUrl: 'home/home.tpl.html'
              }
          }
      });
      $stateProvider.state('userAdmin', {
          url: '/brukere',
          views: {
              "main": {
                  controller: 'HomeCtrl',
                  templateUrl: 'home/home.tpl.html'
              }
          }
      });
      $stateProvider.state('archiveAuthors', {
          url: '/arkivskapere',
          views: {
              "main": {
                  controller: 'HomeCtrl',
                  templateUrl: 'home/home.tpl.html'
              }
          }
      });

  })

  .controller('HomeCtrl', function HomeController($rootScope, $scope, $location, $filter, httpService, errorService, 
                                                  listService, modalService, registerService, stateService, $modal, $window) {
      //Displays the correct template based on current state/path
      $scope.$on('$stateChangeSuccess', function () {
          $scope.sokVisible = false;
          $scope.lagringsenheterVisible = false;
          $scope.brukereVisible = false;
          $scope.arkivskapereVisible = false;

          var path = $location.path();

          if (path === '/') {
              $scope.sokVisible = true;
          } else if (path === '/lagringsenheter') {
              $scope.lagringsenheterVisible = true;
          } else if (path === '/brukere') {
              $scope.brukereVisible = true;
          } else if (path === '/arkivskapere') {
              $scope.arkivskapereVisible = true;
          }
      });

      httpService.userRole().success(function (data) {
          $rootScope.userrole = data;
      }).error(function () {
          $scope.loggUt();
      });

      httpService.userName().success(function (data) {
          $rootScope.username = data;
      }).error(function (status) {});

      httpService.getAll("admin/roller", false).success(function (data) {
          $scope.roller = data;
      }).error(function (status) {});

      $scope.endrePassord = function () {
          var modal = modalService.endrePassord();
          modal.result.then(function () {});
      };

      httpService.get("admin/resetPassord", false).success(function (data) {
          if (data === 'true') {
              $scope.endrePassord();
          }
      });

      httpService.getAll("authors/all").success(function (data) {
          $scope.allArchiveAuthors = data;
      }).error(function (status) {
          errorService.errorCode(status);
      });

      $scope.size = listService.getSize();

      // These are texts that are dynamically loaded into the template.
      $scope.text = {
          "tooltip": {}
      };

      $scope.$watch(
        function () {
            return $filter('translate')('konfig.ANTALL');
        },
        function (value) {
            listService.setSize(Number(value));
            $scope.size = listService.getSize();
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.AVLEVERING');
        },
        function (value) {
            $scope.text.avlevering = value;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.AVTALE');
        },
        function (value) {
            $scope.text.avtale = value;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.VISER');
        },
        function (value) {
            $scope.text.viser = value;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.tooltip.LIST');
        },
        function (value) {
            $scope.text.tooltip.list = value;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.tooltip.ADD');
        },
        function (value) {
            $scope.text.tooltip.add = value;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.tooltip.FAVORITE');
        },
        function (value) {
            $scope.text.tooltip.favorite = value;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.tooltip.FOLDER');
        },
        function (value) {
            $scope.text.tooltip.folder = value;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.tooltip.ENDRE');
        },
        function (value) {
            $scope.text.tooltip.endre = value;
        }
      );
      $scope.$watch(
        function () {
            return $filter('translate')('home.tooltip.DELETE');
        },
        function (value) {
            $scope.text.tooltip.deleteElement = value;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.tooltip.LAAS');
        },
        function (value) {
            $scope.text.tooltip.laas = value;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.tooltip.LAAST');
        },
        function (value) {
            $scope.text.tooltip.laast = value;
        }
      );

      $scope.defaultAvlevering = null;

      httpService.getAll("avtaler", false)
        .success(function (data) {
            $scope.avtaler = data;

            httpService.get("avtaler/default", false)
              .success(function(avtaleIdent) {
                  if (avtaleIdent) {
                      $scope.defaultAvlevering = avtaleIdent;

                      for (var i = 0; i < data.length; i++) {
                          if (data[i].avtaleidentifikator.toLowerCase() === avtaleIdent.toLowerCase()) {
                              $scope.setChosenAgreement(data[i]);
                              break;
                          }
                      }
                  } else {
                      $scope.setChosenAgreement(data[0]);
                  }
              }).error(function (data, status) {
                errorService.errorCode(status);
            });
        }).error(function (data, status) {
          errorService.errorCode(status);
      });

      httpService.get("avtaler/virksomhet", false)
        .success(function (data) {
            $scope.virksomhet = data;
            registerService.setBusiness($scope.virksomhet.navn);
        }).error(function (data, status) {
            errorService.errorCode(status);
      });

      //Avtale
      $scope.setChosenAgreement = function (avtale) {
          if (avtale === undefined) {
              return;
          }
          httpService.getAll("avtaler/" + avtale.avtaleidentifikator + "/avleveringer", false)
            .success(function (data) {
                $scope.avleveringer = data;
                $scope.valgtAvtale = avtale;
            }).error(function (data, status) {
              errorService.errorCode(status);
          });
      };

      $scope.actionDeleteAvtale = function (elementType, id, element) {
          modalService.deleteModal(elementType, id, function () {
              httpService.deleteElement("avtaler/" + id)
                .success(function () {
                    removeFromList($scope.avtaler, element);
                    $scope.setChosenAgreement($scope.avtaler[0]);
                }).error(function (data, status) {
                  errorService.errorCode(status);
              });
          });
      };

      var validerAvtale = function (formData) {
          formData.error = {};
          var success = true;

          if (formData.avtaleidentifikator === undefined || formData.avtaleidentifikator === '') {
              formData.error.avtaleidentifikator = "ID kan ikke være tom";
              success = false;
          }
          if (formData.avtalebeskrivelse === undefined || formData.avtalebeskrivelse === '') {
              formData.error.avtalebeskrivelse = "Beskrivelsen kan ikke være tom";
              success = false;
          }
          if (formData.avtaledato === undefined || formData.avtaledato === '') {
              formData.error.avtaledato = "Dato må være satt";
              success = false;
          }

          if (success) {
              formData.error = undefined;
          }
          return success;
      };

      $scope.actionLeggTilAvtale = function () {
          modalService.nyModal('common/modal-service/agreement-modal.tpl.html', 
              $scope.avtaler, "avtaler", validerAvtale);
      };

      $scope.actionEndreAvtale = function (avtale) {
          modalService.endreModal('common/modal-service/agreement-modal.tpl.html', 
              $scope.avtaler, "avtaler", validerAvtale, avtale);
      };

      //Avlevering
      var validering = function (formData) {
          formData.error = {};
          var success = true;

          if (formData.avleveringsidentifikator === undefined || formData.avleveringsidentifikator === '') {
              formData.error.avleveringsidentifikator = "ID kan ikke være tom";
              success = false;
          }

          if (success) {
              formData.error = undefined;
              formData.avtale = $scope.valgtAvtale;
          }

          return success;
      };

      $scope.actionLeggTilAvlevering = function () {
          modalService.nyModal('common/modal-service/transfer-modal.tpl.html',
              $scope.avleveringer, "avleveringer/ny", validering, $scope.allArchiveAuthors);
      };

      $scope.actionEndreAvlevering = function (avlevering) {
          modalService.endreModal('common/modal-service/transfer-modal.tpl.html',
              $scope.avleveringer, "avleveringer/ny", validering, avlevering, $scope.allArchiveAuthors);
      };

      $scope.actionFjernAvlevering = function (elementType, id, element) {
          modalService.deleteModal(elementType, id, function () {
              httpService.deleteElement("avleveringer/" + id)
                .success(function () {
                    removeFromList($scope.avleveringer, element);
                }).error(function (data, status) {
                  var errorMessage = $filter('translate')('formError.' + data[0].constraint);
                  errorService.errorCode(status, errorMessage);
              });
          });
      };

      $scope.actionSettDefaultAvlevering = function(avlevering){
          httpService.get("avleveringer/" + avlevering.avleveringsidentifikator + "/aktiv")
            .success(function() {
                $scope.setChosenAgreement($scope.valgtAvtale);
            }).error(function(data, status) {
              errorService.errorCode(status);
          });
      };

      $scope.actionShowTransfer = function (avlevering) {
          registerService.setTransferDescription(avlevering.avleveringsbeskrivelse);
          registerService.setTransferId(avlevering.avleveringsidentifikator);
          registerService.setTransfer(avlevering);

          var endpoint = "pasientjournaler/" + avlevering.avleveringsidentifikator + "/all";
          var params = {
              "page": "1",
              "size": $scope.size
          };

          httpService.getAll(endpoint, false, params)
            .then(function (response) {
              var title = $scope.virksomhet.navn + "/" + avlevering.avtale.avtalebeskrivelse + "/" + 
                avlevering.avleveringsbeskrivelse;
              var subtitle = avlevering.arkivskaper.name;
              
              listService.setTitle(title);
              listService.setSubtitle(subtitle);
              listService.setData(response.data);
              listService.setTransfer(avlevering);
              listService.setClean(true);

              $location.path("/list");
            }, function (response) {
              errorService.errorCode(response.status);
          });
      };

      $scope.actionAvleveringLeveranse = function (avlevering) {
          window.location = httpService.getRoot() + "avleveringer/" + 
              avlevering.avleveringsidentifikator + "/leveranse";
      };

      //Util
      $scope.loggUt = function () {
          httpService.logout();
          $window.location = "logout";
      };

      $scope.actionCreateNewMedicalRecord = function (avlevering) {
          registerService.setTransfer(avlevering);
          registerService.setMedicalRecordDTO(null);
          registerService.setChosenAgreement($scope.valgtAvtale.avtalebeskrivelse);
          registerService.setTransferId(avlevering.avleveringsidentifikator);
          registerService.setTransferDescription(avlevering.avleveringsbeskrivelse);
          $location.path('/registrer');
      };

      $scope.actionLockTransfer = function (avlevering) {
          var tpl = 'common/modal-service/warning-modal.tpl.html';
          var url = "avleveringer/" + avlevering.avleveringsidentifikator + "/laas";
          var id = avlevering.avleveringsidentifikator;
          var tittel = $filter('translate')('modal.warning_laas.TITTEL');
          var beskrivelse = $filter('translate')('modal.warning_laas.BESKRIVELSE');
          modalService.warningModal(tpl, url, id, tittel, beskrivelse, function () {
              $scope.setChosenAgreement($scope.valgtAvtale);
          });
      };

      $scope.actionUnlockTransfer = function (avlevering) {
          var tpl = 'common/modal-service/warning-modal.tpl.html';
          var url = "avleveringer/" + avlevering.avleveringsidentifikator + "/laasOpp";
          var id = avlevering.avleveringsidentifikator;
          var tittel = $filter('translate')('modal.warning_laas_opp.TITTEL');
          modalService.warningModal(tpl, url, id, tittel, null, function () {
              $scope.setChosenAgreement($scope.valgtAvtale);
          });
      };

      //Hjelpe metode for å fjerne fra liste
      var removeFromList = function (list, element) {
          for (var i = 0; i < list.length; i++) {
              if (element === list[i]) {
                  list.splice(i, 1);
              }
          }
      };
    
  });