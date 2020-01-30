angular.module('nha.home')

    .controller('ArchiveAuthorsCtrl', function($rootScope, $scope, $location, $filter, httpService, errorService){
        $scope.archiveAuthor = {};
        $scope.archiveAuthors = [];
        $scope.selectedArchiveAuthorRow = null;
        $scope.isCurrentAuthorNew = true;
        $scope.error = [];

        $scope.selectArchiveAuthor = function (selectedArchiveAuthor, index) {
            if (index === $scope.selectedArchiveAuthorRow) {
                $scope.isCurrentAuthorNew = true;
                $scope.selectedArchiveAuthorRow = null;
                $scope.archiveAuthor.uuid = null;
                $scope.archiveAuthor.code = null;
                $scope.archiveAuthor.name = null;
                $scope.archiveAuthor.description = null;
            } else {
                $scope.isCurrentAuthorNew = false;
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
            $scope.isCurrentAuthorNew = true;
        };

        $scope.createOrUpdateAuthor = function () {
            if($scope.isCurrentAuthorNew){
                $scope.createArchiveAuthor();
            } else {
                $scope.updateArchiveAuthor();
            }
        };

        $scope.createArchiveAuthor = function () {
            $scope.error = [];

            httpService.create("authors", $scope.archiveAuthor)
                .success(function () {
                    $scope.getArchiveAuthors();
                    resetArchiveAuthor();
                })
                .error(function (data, status) {
                    if (status !== 400) {
                        errorService.errorCode(status);
                    } else {
                        setErrorMessages(data);
                    }
                });
        };

        $scope.updateArchiveAuthor = function () {
            $scope.error = [];

            httpService.update("authors", $scope.archiveAuthor)
                .success(function () {
                    $scope.getArchiveAuthors();
                    resetArchiveAuthor();
                })
                .error(function (data, status) {
                    if (status !== 400) {
                        errorService.errorCode(status);
                    } else {
                        setErrorMessages(data);
                    }
                });
        };

        $scope.deleteArchiveAuthor = function () {
            httpService.deleteElement("authors/" + $scope.archiveAuthor.uuid)
                .success(function () {
                    $scope.getArchiveAuthors();
                    resetArchiveAuthor();
                })
                .error(function (data, status) {
                    if (status === 400) {
                        var errorMessage = $filter('translate')('formError.' + data[0].constraint);
                        errorService.errorCode(status, errorMessage);
                    } else {
                        errorService.errorCode(status);
                    }
                });
        };

        $scope.showError = function (attribute) {
          return $scope.error[attribute] !== undefined;
        };

        var setErrorMessages = function (data) {
            angular.forEach(data, function (element) {
                $scope.error[element.attribute] = $filter('translate')('formError.' + element.constraint);
            });
        };
    });