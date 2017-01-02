angular.module('app', ['ngResource'])
    .config(['$resourceProvider', function($resourceProvider) {
        $resourceProvider.defaults.stripTrailingSlashes = false;
    }])
    .constant('HOST', '192.168.0.205:8080')
    .factory('Answer', ['$resource', 'HOST', function($resource, HOST) {
        return $resource('http://' + HOST + '/:teamID/answer/');
    }])
    .controller('Controller', ['$resource', '$scope', '$timeout', '$interval', 'HOST', 'Answer', function($resource, $scope, $timeout, $interval, HOST, Answer) {
        var GameStatus = $resource('http://' + HOST + '/');
        var GameConfiguration = $resource('http://' + HOST + '/game/');

        var vm = this;

        $scope.loading = true;
        startStatusLoop();

        $scope.postAnswer = postAnswer;

        //////////

        function startStatusLoop() {
            GameStatus.get(function(gameStatus) {
                if (gameStatus.ready)
                    initGame();
                else {
                    $timeout(function() {   //TODO use $interval and a stop variable
                        startStatusLoop();
                    }, 100);
                }
            });
        }

        function initGame() {
            GameConfiguration.get(function(configuration) {
                if (configuration.type == 'picture_flash')
                    playPictureFlash(configuration);

                $scope.loading = false;
            });
        }

        function playPictureFlash(configuration) {
            $scope.team = {};
            $scope.answer = {};
            $scope.inputEnabled = false;
            let currentWindow = configuration.windows.shift();

            // Init times
            let startingTime = Date.now();
            vm.windowTime = startingTime + currentWindow.start;

            let now;
            $interval(function() {
                now = Date.now();
                if (now > vm.windowTime) {
                    if ($scope.inputEnabled) {
                        $scope.inputEnabled = false;
                        postAnswer();
                        currentWindow = configuration.windows.shift();

                        if (configuration.windows.length > 0) {
                            currentWindow = configuration.windows.shift();
                            vm.windowTime = startingTime + currentWindow.start;
                        }
                        else
                            vm.windowTime = Number.MAX_SAFE_INTEGER;
                    }
                    else {
                        $scope.inputEnabled = true;
                        $timeout(function() {$('#answer').focus()});
                        vm.windowTime = startingTime + currentWindow.end;
                    }
                }
            }, 200);
            //TODO start with a run ($interval waits 'delay' ms first)
            //TODO progressBar
        }

        function postAnswer() {
            console.log($scope.answer.content);
            if ($scope.answer.content) {
                let answer = new Answer();
                answer.content = $scope.answer.content;
                answer.$save({teamID: $scope.team.id});
            }
            $scope.answer = {};
            $timeout(function () {
                $('#answer').focus()
            });
        }
    }]);