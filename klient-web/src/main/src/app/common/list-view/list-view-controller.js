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

        $scope.$watch(
            function () {
                var tmp = listService.getTransfer();
                return tmp == null ? false : tmp.laast;
            },
            function (value) {
                $scope.laast = value;
            }
        );

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

        if (listService.getData() === undefined) {
            $rootScope.navHome();
        } else {
            $scope.updatePager(1);
            setSubtitle();
        }

        function setSubtitle() {
            var data = listService.getData();
            var subtitle = listService.createSubtitle(data);
            listService.setSubtitle(subtitle);
        }

        $scope.setPage = function (page) {
            if (page < 1 || page > $scope.pager.totalPages) {
                return;
            }

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
                    listService.setData(response.data);
                    setSubtitle();
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

        function navigateToRegister(transfer) {
            registerService.setTransfer(transfer);
            registerService.setMedicalRecordDTO(null);
            registerService.setChosenAgreement(transfer.avtale.avtalebeskrivelse);
            registerService.setTransferId(transfer.avleveringsidentifikator);
            registerService.setTransferDescription(transfer.avleveringsbeskrivelse);
            $location.path('/registrer');
        }

        $scope.actionAddNewMedicalRecord = function () {
            var transfer = registerService.getTransfer();
            if (transfer != null) {
                registerService.setMedicalRecordDTO(null);
                $location.path("/registrer");
            } else {
                httpService.getAll("avleveringer/?locked=false")
                    .then(function (response) {
                        var transfers = response.data;
                        if (transfers.length === 1) {
                            navigateToRegister(transfers[0]);
                        } else if (transfers.length > 1) {
                            var modal = modalService.openSelectModal(
                                'common/modal-service/transfer-list-modal.tpl.html', transfers);
                            modal.result.then(function (result) {
                                navigateToRegister(result);
                            });
                        } else {
                            console.log("Something went horribly wrong");
                        }
                    }, function (response) {
                        errorService.errorCode(response.status);
                    });
            }
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