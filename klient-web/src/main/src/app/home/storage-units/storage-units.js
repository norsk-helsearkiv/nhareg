angular.module('nha.home')

    .controller('StorageUnitsCtrl', function($rootScope, $scope, $location, $filter, httpService, errorService, listService, modalService){
        $scope.lagringsenheter = {};
        $scope.lagrSok = {};
        $scope.lagrPasientjournaler = [];

        $scope.lagrActionSok = function () {

            httpService.getAll("lagringsenheter/sok?identifikatorSok=" + $scope.lagrSok.lagringsenhet, false)
                .success(function (data) {
                    $scope.lagringsenheter = data;
                }).error(function (data, status) {
                errorService.errorCode(status);
            });
        };

        $scope.selectedRow = null;// initialize our variable to null
        $scope.selectedRowIndex = null;
        $scope.selectedRowViewIndex = null;

        $scope.setClickedRow = function (item, index) {  //function that sets the value of selectedRow to current item.uuid
            if (item === $scope.selectedRow) {
                $scope.selectedRow = null;
                $scope.selectedRowIndex = null;
                $scope.selectedRowViewIndex = null;
            } else {
                for (var i=0;i<$scope.lagringsenheter.length;i++){
                    if ($scope.lagringsenheter[i].uuid === item){
                        $scope.selectedRowIndex = i;
                        break;
                    }
                }
                $scope.selectedRow = item;
                $scope.selectedRowViewIndex = index;
            }
        };

        $scope.lagrActionEndreLagringsenhet = function (lagringsenhet) {
            var uuid = lagringsenhet.uuid;
            httpService.get("lagringsenheter/"+uuid+"/maske", false)
                .success(function (data, status, headers, config) {
                    var maske = data;
                    var modal = modalService.changeStorageUnit('common/modal-service/change-storage-unit-modal.tpl.html',
                        'lagringsenheter/',
                        lagringsenhet,
                        maske
                    );
                    modal.result.then(function () {
                        //TODO
                    });
                }).error(function (data, status, headers, config) {
                errorService.errorCode(status);
            });
        };

        $scope.lagrActionHentPasientjournaler = function () {
            if ($scope.selectedRowIndex > -1) {
                var valgtLagringsenhet = $scope.lagringsenheter[$scope.selectedRowIndex];
                httpService.getAll("lagringsenheter/" + valgtLagringsenhet.identifikator + "/pasientjournaler", false)
                    .success(function (data, status, headers, config) {
                        $scope.lagrPasientjournaler = data;
                    }).error(function (data, status, headers, config) {
                    errorService.errorCode(status);
                });
            }
        };

        $scope.lagrSelection = {
            allSelected: false
        };

        $scope.lagrToggleAll = function () {
            var toggleStatus = !$scope.lagrSelection.allSelected;
            angular.forEach($scope.lagrPasientjournaler, function (itm) {
                itm.selected = toggleStatus;
            });
        };

        $scope.lagrOptionToggled = function () {
            $scope.lagrSelection.allSelected = $scope.lagrPasientjournaler.every(function (itm) {
                return itm.selected;
            });
        };

        $scope.lagrFlytt = {
            lagringsenhet: ""
        };

        $scope.lagrActionFlytt = function () {
            var selectedPasientjournaler = [];
            angular.forEach($scope.lagrPasientjournaler, function (pasientjournal) {
                if (pasientjournal.selected) {
                    selectedPasientjournaler.push(pasientjournal.uuid);
                }
            });

            var tpl = 'common/modal-service/warning-modal.tpl.html';
            var url = "lagringsenheter/flytt";
            var identifikator = $scope.lagrFlytt.lagringsenhet;
            var tittel = $filter('translate')('modal.warning_flytt.TITTEL');
            var beskrivelse = $filter('translate')('modal.warning_flytt.BESKRIVELSE');
            modalService.warningFlyttLagringsenheter(tpl, url, '', tittel, beskrivelse,
                function (removedUuids) {
                    $scope.lagrActionHentPasientjournaler();
                },
                selectedPasientjournaler,
                identifikator
            );
        };

    });