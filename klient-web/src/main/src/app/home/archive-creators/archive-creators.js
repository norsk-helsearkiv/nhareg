angular.module('nha.home')

    .controller('ArchiveCreatorsCtrl', function($rootScope, $scope, $location, $filter, httpService, errorService){
        $scope.archiveCreator = {};
        $scope.archiveCreators = [];
        $scope.selectedArchiveCreatorRow = null;

        $scope.selectArchiveCreator = function (selectedArchiveCreator, index) {
            if (index === $scope.selectedArchiveCreatorRow) {
                $scope.selectedArchiveCreatorRow = null;
                $scope.archiveCreator.uuid = null;
                $scope.archiveCreator.kortkode = null;
                $scope.archiveCreator.navn = null;
                $scope.archiveCreator.beskrivelse = null;
            } else {
                $scope.selectedArchiveCreatorRow = index;
                $scope.archiveCreator.uuid = selectedArchiveCreator.uuid;
                $scope.archiveCreator.kortkode = selectedArchiveCreator.kortkode;
                $scope.archiveCreator.navn = selectedArchiveCreator.navn;
                $scope.archiveCreator.beskrivelse = selectedArchiveCreator.beskrivelse;
            }
        };

        $scope.getArchiveCreators = function () {
            httpService.getAll("admin/archivecreators")
                .success(function (data) {
                    $scope.archiveCreators = data;
                })
                .error(function (data, status) {
                    errorService.errorCode(status);
            });
        };

        var resetArchiveCreator = function () {
            $scope.archiveCreator = {};
        };

        $scope.updateArchiveCreator = function () {
            httpService.create("admin/archivecreator", $scope.archiveCreator)
                .success(function () {
                    $scope.getArchiveCreators();
                    resetArchiveCreator();
                })
                .error(function (data, status) {
                    errorService.errorCode(status);
                });
        };
    });