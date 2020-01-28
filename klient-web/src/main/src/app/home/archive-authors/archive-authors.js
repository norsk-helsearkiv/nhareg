angular.module('nha.home')

    .controller('ArchiveAuthorsCtrl', function($rootScope, $scope, $location, $filter, httpService, errorService){
        $scope.archiveAuthor = {};
        $scope.archiveAuthors = [];
        $scope.selectedArchiveAuthorRow = null;

        $scope.selectArchiveAuthor = function (selectedArchiveAuthor, index) {
            if (index === $scope.selectedArchiveAuthorRow) {
                $scope.selectedArchiveAuthorRow = null;
                $scope.archiveAuthor.uuid = null;
                $scope.archiveAuthor.kortkode = null;
                $scope.archiveAuthor.navn = null;
                $scope.archiveAuthor.beskrivelse = null;
            } else {
                $scope.selectedArchiveAuthorRow = index;
                $scope.archiveAuthor.uuid = selectedArchiveAuthor.uuid;
                $scope.archiveAuthor.kortkode = selectedArchiveAuthor.kortkode;
                $scope.archiveAuthor.navn = selectedArchiveAuthor.navn;
                $scope.archiveAuthor.beskrivelse = selectedArchiveAuthor.beskrivelse;
            }
        };

        $scope.getArchiveAuthors = function () {
            httpService.getAll("archiveAuthors")
                .success(function (data) {
                    $scope.archiveAuthors = data;
                })
                .error(function (data, status) {
                    errorService.errorCode(status);
            });
        };

        var resetArchiveAuthor = function () {
            $scope.archiveAuthor = {};
        };

        $scope.updateArchiveAuthor = function () {
            httpService.create("archiveAuthors", $scope.archiveAuthor)
                .success(function () {
                    $scope.getArchiveAuthors();
                    resetArchiveAuthor();
                })
                .error(function (data, status) {
                    errorService.errorCode(status);
                });
        };
    });