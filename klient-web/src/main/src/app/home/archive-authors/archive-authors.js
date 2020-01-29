angular.module('nha.home')

    .controller('ArchiveAuthorsCtrl', function($rootScope, $scope, $location, $filter, httpService, errorService){
        $scope.archiveAuthor = {};
        $scope.archiveAuthors = [];
        $scope.selectedArchiveAuthorRow = null;
        $scope.isCurrentAuthorNew = true;

        $scope.selectArchiveAuthor = function (selectedArchiveAuthor, index) {
            $scope.isCurrentAuthorNew = false;
            if (index === $scope.selectedArchiveAuthorRow) {
                $scope.selectedArchiveAuthorRow = null;
                $scope.archiveAuthor.uuid = null;
                $scope.archiveAuthor.code = null;
                $scope.archiveAuthor.name = null;
                $scope.archiveAuthor.description = null;
            } else {
                $scope.selectedArchiveAuthorRow = index;
                $scope.archiveAuthor.uuid = selectedArchiveAuthor.uuid;
                $scope.archiveAuthor.code = selectedArchiveAuthor.code;
                $scope.archiveAuthor.name = selectedArchiveAuthor.name;
                $scope.archiveAuthor.description = selectedArchiveAuthor.description;
            }
        };

        $scope.getArchiveAuthors = function () {
            httpService.getAll("authors/all")
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

        $scope.createArchiveAuthor = function () {
            httpService.create("authors", $scope.archiveAuthor)
                .success(function () {
                    $scope.getArchiveAuthors();
                    resetArchiveAuthor();
                })
                .error(function (data, status) {
                    errorService.errorCode(status);
                });
        };

        $scope.deleteArchiveAuthor = function () {
            httpService.deleteElement("authors", $scope.archiveAuthor)
                .success(function () {
                    $scope.getArchiveAuthors();
                    resetArchiveAuthor();
                })
                .error(function (data, status) {
                    errorService.errorCode(status);
                });
        };
    });