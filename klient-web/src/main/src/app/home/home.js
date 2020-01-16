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

  })

  .controller('HomeCtrl', function HomeController($rootScope, $scope, $location, $filter, httpService, errorService, listService, modalService, registerService, stateService, $modal, $window) {

      //Displays the correct template based on current state/path
      $scope.$on('$stateChangeSuccess', function () {
          $scope.sokVisible = false;
          $scope.lagringsenheterVisible = false;
          $scope.brukereVisible = false;

          var path = $location.path();

          if (path === '/') {
              $scope.sokVisible = true;
          } else if (path === '/lagringsenheter') {
              $scope.lagringsenheterVisible = true;
          } else if (path === '/brukere') {
              $scope.brukereVisible = true;
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

      $scope.size = listService.getSize();

      //Tekster i vinduet lastet fra kontroller
      $scope.text = {
          "tooltip": {}
      };

      $scope.$watch(
        function () {
            return $filter('translate')('konfig.ANTALL');
        },
        function (newval) {
            listService.setSize(Number(newval));
            $scope.size = listService.getSize();
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.PASIENTSOK');
        },
        function (newval) {
            $scope.text.pasientsok = newval;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.AVLEVERING');
        },
        function (newval) {
            $scope.text.avlevering = newval;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.AVTALE');
        },
        function (newval) {
            $scope.text.avtale = newval;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.SOKERESULTAT');
        },
        function (newval) {
            $scope.text.sokeresultat = newval;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.VISER');
        },
        function (newval) {
            $scope.text.viser = newval;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.tooltip.LIST');
        },
        function (newval) {
            $scope.text.tooltip.list = newval;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.tooltip.ADD');
        },
        function (newval) {
            $scope.text.tooltip.add = newval;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.tooltip.FAVORITE');
        },
        function (newval) {
            $scope.text.tooltip.favorite = newval;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.tooltip.FOLDER');
        },
        function (newval) {
            $scope.text.tooltip.folder = newval;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.tooltip.ENDRE');
        },
        function (newval) {
            $scope.text.tooltip.endre = newval;
        }
      );
      $scope.$watch(
        function () {
            return $filter('translate')('home.tooltip.DELETE');
        },
        function (newval) {
            $scope.text.tooltip.deleteElement = newval;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.tooltip.LAAS');
        },
        function (newval) {
            $scope.text.tooltip.laas = newval;
        }
      );

      $scope.$watch(
        function () {
            return $filter('translate')('home.tooltip.LAAST');
        },
        function (newval) {
            $scope.text.tooltip.laast = newval;
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
                              $scope.setValgtAvtale(data[i]);
                              break;
                          }
                      }
                  } else {
                      $scope.setValgtAvtale(data[0]);
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
        }).error(function (data, status) {
          errorService.errorCode(status);
      });

      //Avtale
      $scope.setValgtAvtale = function (avtale) {
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
                    fjern($scope.avtaler, element);
                    $scope.setValgtAvtale($scope.avtaler[0]);
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
          modalService.nyModal('common/modal-service/new-agreement.tpl.html', $scope.avtaler, "avtaler", validerAvtale);
      };

      $scope.actionEndreAvtale = function (avtale) {
          modalService.endreModal('common/modal-service/new-agreement.tpl.html', $scope.avtaler, "avtaler", validerAvtale, avtale);
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
          modalService.nyModal('common/modal-service/new-delivery.tpl.html', $scope.avleveringer, "avleveringer/ny", validering);
      };

      $scope.actionEndreAvlevering = function (avlevering) {
          modalService.endreModal('common/modal-service/new-delivery.tpl.html', $scope.avleveringer, "avleveringer/ny", validering, avlevering);
      };

      $scope.actionFjernAvlevering = function (elementType, id, element) {
          modalService.deleteModal(elementType, id, function () {
              httpService.deleteElement("avleveringer/" + id)
                .success(function () {
                    fjern($scope.avleveringer, element);
                }).error(function (data, status) {
                  errorService.errorCode(status);
              });
          });
      };

      $scope.actionSettDefaultAvlevering = function(avlevering){
          httpService.get("avleveringer/" + avlevering.avleveringsidentifikator + "/aktiv")
            .success(function() {
                $scope.setValgtAvtale($scope.valgtAvtale);
            }).error(function(data, status) {
              errorService.errorCode(status);
          });
      };

      $scope.actionVisAvlevering = function (avlevering) {
          registerService.setAvleveringsbeskrivelse(avlevering.avleveringsbeskrivelse);
          registerService.setAvlevering(avlevering);

          var endpoint = "avleveringer/" + avlevering.avleveringsidentifikator + "/pasientjournaler";
          var params = {
              "page": "1",
              "size": $scope.size
          };

          httpService.getAll(endpoint, false, params)
            .success(function (data) {
                var title = {
                    "tittel": avlevering.virksomhet + "/" + avlevering.avtale.avtalebeskrivelse + "/" + avlevering.avleveringsbeskrivelse,
                    "underTittel": avlevering.arkivskaper
                };

                listService.init(title, data);
                listService.setAvlevering(avlevering);

                $location.path("/list");

            }).error(function (data, status) {
              errorService.errorCode(status);
          });
      };

      $scope.actionAvleveringLeveranse = function (avlevering) {
          window.location = httpService.getRoot() + "avleveringer/" + avlevering.avleveringsidentifikator + "/leveranse";
      };

      //Util
      $scope.loggUt = function () {
          httpService.logout();
          $window.location="logout";
      };

      $scope.actionLeggTilPasientjournald = function (avlevering) {
          registerService.setAvlevering(avlevering);
          registerService.setPasientjournalDTO(null);
          registerService.setVirksomhet($scope.virksomhet.navn);
          registerService.setValgtAvtale($scope.valgtAvtale.avtalebeskrivelse);
          registerService.setAvleveringsidentifikator(avlevering.avleveringsidentifikator);
          registerService.setAvleveringsbeskrivelse(avlevering.avleveringsbeskrivelse);
          $location.path('/registrer');
      };

      $scope.actionLaasAvlevering = function (avlevering) {
          var tpl = 'common/modal-service/warning-modal.tpl.html';
          var url = "avleveringer/" + avlevering.avleveringsidentifikator + "/laas";
          var id = avlevering.avleveringsidentifikator;
          var tittel = $filter('translate')('modal.warning_laas.TITTEL');
          var beskrivelse = $filter('translate')('modal.warning_laas.BESKRIVELSE');
          modalService.warningModal(tpl, url, id, tittel, beskrivelse, function () {
              $scope.setValgtAvtale($scope.valgtAvtale);
          });
      };

      $scope.actionLaasOppAvlevering = function (avlevering) {
          var tpl = 'common/modal-service/warning-modal.tpl.html';
          var url = "avleveringer/" + avlevering.avleveringsidentifikator + "/laasOpp";
          var id = avlevering.avleveringsidentifikator;
          var tittel = $filter('translate')('modal.warning_laas_opp.TITTEL');
          modalService.warningModal(tpl, url, id, tittel, null, function () {
              $scope.setValgtAvtale($scope.valgtAvtale);
          });
      };

      //Hjelpe metode for å fjerne fra liste
      var fjern = function (list, element) {
          for (var i = 0; i < list.length; i++) {
              if (element === list[i]) {
                  list.splice(i, 1);
              }
          }
      };
  });