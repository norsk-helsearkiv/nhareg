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

    .controller('ListCtrl', function HomeController($rootScope, $scope, $location, listService, httpService,
                                                    errorService, modalService, $filter,
                                                    registerService, pagerService) {
        var baseEndpointUrl = "pasientjournaler/";
        var size = listService.getSize();

        $scope.text = {
            "tooltip": {}
        };

        $scope.$watch(
            function () {
                return $filter('translate')('home.PASIENTSOK');
            },
            function (value) {
                $scope.text.patientsearch = value;
            }
        );

        $scope.$watch(
            function () {
                return $filter('translate')('common.PASIENTJOURNAL');
            },
            function (value) {
                $scope.text.pasientjournal = value;
            }
        );

        $scope.$watch(
            function () {
                return listService.getData();
            },
            function (value) {
                listService.setData(value);
                $scope.data = value;
            }
        );

        $scope.$watch(
            function () {
                return listService.getTitle();
            },
            function (value) {
                listService.setTitle(value);
                $scope.text.title = value;
            }
        );

        $scope.$watch(
            function () {
                return listService.getSubtitle();
            },
            function (value) {
                listService.setSubtitle(value);
                $scope.text.subtitle = value;
            }
        );

        $scope.navHome = function () {
            $location.path('/');
        };

        // $scope.navLoggut = function () {
        //   $location.path('/login');
        // };

        if (listService.getData() === undefined) {
            $scope.navHome();
        }

        $scope.actionSort = function (column, sortDirection) {
            $scope.sortDirection = sortDirection ? "asc" : "desc";
            $scope.sortColumn = column;

            httpService.getAll(baseEndpointUrl + 
                    "?page=" + $scope.pager.currentPage +
                    "&size=" + size +
                    listService.getQuery() +
                    "&orderBy=" + $scope.sortColumn +
                    "&sortDirection=" + $scope.sortDirection
                )
                .then(function (response) {
                    var data = listService.getData();
                    var subtitle = listService.createSubtitle(data);
                    listService.setSubtitle(subtitle);
                    listService.setData(response.data);
                    $scope.updatePager($scope.pager.currentPage);
                }, function (response) {
                    errorService.errorCode(response.status);
                });
        };

        $scope.updatePager = function (page) {
            var data = listService.getData();
            $scope.pager = pagerService.getPager(data.total, page, size);
        };

        $rootScope.$on("UpdatePager", function (event, data) {
            $scope.updatePager(data);
        });

        $scope.updatePager(1);

        $scope.setPage = function (page) {
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

            httpService.getAll(baseEndpointUrl + "?page=" + page + "&size=" +
                $scope.pager.pageSize + listService.getQuery() + ordering)
                .then(function (response) {
                    var data = listService.getData();
                    var subtitle = listService.createSubtitle(data);
                    listService.setSubtitle(subtitle);
                    listService.setData(response.data);
                }, function (response) {
                    errorService.errorCode(response.status);
                });
        };

        $scope.actionRemoveMedicalRecord = function (medicalRecord) {
            modalService.deleteModal($scope.text.pasientjournal,
                medicalRecord.navn + " (" + medicalRecord.fodselsnummer + ") ",
                function () {
                    httpService.deleteElement(baseEndpointUrl + medicalRecord.uuid)
                        .then(function () {
                            var data = listService.getData();
                            removeFromList(data.liste, medicalRecord);
                            --data.size;
                            --data.total;

                            var subtitle = listService.createSubtitle(data);
                            listService.setSubtitle(subtitle);
                            listService.setData(data);
                        }, function (response) {
                            errorService.errorCode(response.status);
                        });
                });
        };

    $scope.actionAddMedicalRecordFromSearch = function () {
      console.log("You pressed me!");
    };

        $scope.actionEditRecord = function (medicalRecord) {
            httpService.get(baseEndpointUrl + medicalRecord)
                .then(function (response) {
                    registerService.setMedicalRecordDTO(response.data);
                    $location.path('/registrer');
                }, function (response) {
                    errorService.errorCode(response.status);
                });
        };

        // Utility method for removing from a given list
        var removeFromList = function (list, element) {
            for (var i = 0; i < list.length; i++) {
                if (element === list[i]) {
                    list.splice(i, 1);
                }
            }
        };

    });