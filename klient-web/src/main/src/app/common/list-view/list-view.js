angular.module('nha.common.list-view', [
        'nha.common.list-service',
        'nha.common.pager-service',
        'nha.common.http-service',
        'nha.common.error-service',
        'nha.common.modal-service',
        'nha.register.register-service',
        'ui.router'
    ])

    .config(function config($stateProvider) {
        $stateProvider.state('list', {
            url: '/list',
            views: {
                "main": {
                    controller: 'ListCtrl',
                    templateUrl: 'common/list-view/list-view.tpl.html'
                }
            }
        });
    })

    .controller('ListCtrl', function HomeController($scope, $location, listService, httpService, errorService, modalService, $filter, registerService, stateService, pagerService) {
        var antall = 15;

        $scope.$watch(
            function () {
                return $filter('translate')('konfig.ANTALL');
            },
            function (newval) {
                antall = Number(newval);
            }
        );
        $scope.tekster = {
            "tooltip": {}
        };
        $scope.$watch(
            function () {
                return $filter('translate')('home.PASIENTSOK');
            },
            function (newval) {
                $scope.tekster.pasientsok = newval;
            }
        );
        $scope.$watch(
            function () {
                return $filter('translate')('common.PASIENTJOURNAL');
            },
            function (newval) {
                $scope.tekster.pasientjournal = newval;
            }
        );

        $scope.navHome = function () {
            $location.path('/');
        };

        $scope.navLoggut = function () {
            $location.path('/login');
        };

        $scope.data = listService.getData();
        if ($scope.data === undefined) {
            $scope.navHome();
        }

        var setTittel = function (data) {
            var max = (data.side * data.antall);
            if (max > data.total) {
                max = data.total;
            }
            var undertittel = ((data.side - 1) * data.antall) + 1 + "..." + max + " / " + data.total;
            $scope.tittel.underTittel = undertittel;
        };

        $scope.tittel = listService.getTittel();
        setTittel($scope.data);


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

        $scope.actionSort = function (column, sortDirection) {
            console.log("sorting by:" + column + " dir:" + sortDirection);
            var direction = sortDirection ? "asc" : "desc";
            $scope.sortDirection = direction;
            $scope.sortColumn = column;

            httpService.getAll("pasientjournaler?side=" + $scope.pager.currentPage + "&antall=" + antall + listService.getQuery() + "&orderBy=" + $scope.sortColumn + "&sortDirection=" + $scope.sortDirection)
                .success(function (data, status, headers, config) {

                    setTittel(data);
                    $scope.data = data;
                    $scope.updatePager($scope.pager.currentPage);

                }).error(function (data, status, headers, config) {
                errorService.errorCode(status);
            });
        };

        $scope.actionRensSok = function(){
            $scope.sok.lagringsenhet = '';
            $scope.sok.fanearkId = '';
            $scope.sok.fodselsnummer = '';
            $scope.sok.navn = '';
            $scope.sok.fodt = '';
            $scope.sok.oppdatertAv = '';
            $scope.sok.sistOppdatert = '';
        };

        $scope.actionSok = function (sokestring) {
            var txt = $scope.tekster.sokeresultat;
            var viser = $scope.tekster.viser;
            var sok = {
                sokLagringsenhet: $scope.sok.lagringsenhet,
                sokFanearkId: $scope.sok.fanearkId,
                sokFodselsnummer: $scope.sok.fodselsnummer,
                sokNavn: $scope.sok.navn,
                sokFodt: $scope.sok.fodt,
                sokOppdatertAv: $scope.sok.oppdatertAv,
                sokSistOppdatert: $scope.sok.sistOppdatert
            };
            listService.setSok(sok);
            httpService.getAll("pasientjournaler?side=1&antall=" + antall + listService.getQuery())
                .success(function (data, status, headers, config) {

                    setTittel(data);
                    $scope.tittel.tittel = $scope.tekster.pasientsok;

                    $scope.data = data;


                    $scope.updatePager(1);


                }).error(function (data, status, headers, config) {
                errorService.errorCode(status);
            });
        };



        $scope.updatePager = function(page){
            $scope.pager = pagerService.getPager($scope.data.total, page, antall);
        };

        $scope.updatePager(1);

        $scope.setPage = function(page) {
            if (page < 1 || page > $scope.pager.totalPages) {
                return;
            }

            // get pager object from service
            $scope.updatePager(page);

            var ordering = "";
            if ($scope.sortColumn) {
                ordering += "&orderBy=" + $scope.sortColumn;
            }
            if ($scope.sortDirection) {
                ordering += "&sortDirection=" + $scope.sortDirection;
            }

            httpService.getAll("pasientjournaler?side=" + page + "&antall=" + $scope.pager.pageSize + listService.getQuery() + ordering)
                .success(function (data) {
                    setTittel(data);
                    $scope.data = data;

                }).error(function (data, status) {
                errorService.errorCode(status);
            });
        };

        $scope.actionFjernPasientjournal = function (pasientjournal) {
            modalService.deleteModal($scope.tekster.pasientjournal, pasientjournal.navn + " (" + pasientjournal.fodselsnummer + ") ", function () {
                httpService.deleteElement("pasientjournaler/" + pasientjournal.uuid)
                    .success(function () {
                        fjern($scope.data.liste, pasientjournal);
                        --$scope.data.antall;
                        --$scope.data.total;
                        setTittel($scope.data);
                    }).error(function (data, status) {
                    errorService.errorCode(status);
                });
            });
        };

        $scope.actionLeggTilPasientjournal = function () {

            var first = $scope.data.liste[0];

            httpService.get("pasientjournaler/" + first.uuid)
                .success(function (data) {
                    registerService.setVirksomhet(data.virksomhet);
                    registerService.setPasientjournalDTO(null);
                    registerService.setValgtAvtale(data.avtaleBeskrivelse);
                    registerService.setAvleveringsidentifikator(data.avleveringsidentifikator);
                    registerService.setAvleveringsbeskrivelse(data.avleveringBeskrivelse);

                    $location.path('/registrer');
                }).error(function (data, status) {
                    errorService.errorCode(status);
                });

        };

        $scope.showAddPatientJournalBtn = function () {
            var first = $scope.data.liste[0];

            if (first === undefined) {
                return false;
            }

            return $scope.data.liste.every(function (element) {
                return element.avleveringsidentifikator === first.avleveringsidentifikator;
            });
        };

        $scope.actionVisJournal = function (pasientjournal) {
            httpService.get("pasientjournaler/" + pasientjournal)
                .success(function (data, status, headers, config) {
                    registerService.setPasientjournalDTO(data);
                    var avlevering = {lagringsenhetformat:data.lagringsenhetformat};
                    registerService.setAvlevering(avlevering);
                    registerService.setVirksomhet(data.virksomhet);
                    registerService.setValgtAvtale(data.avtaleBeskrivelse);
                    registerService.setAvleveringsidentifikator(data.avleveringsidentifikator);
                    registerService.setAvleveringsbeskrivelse(data.avleveringBeskrivelse);
                    $location.path('/registrer');
                }).error(function (data, status, headers, config) {
                errorService.errorCode(status);
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