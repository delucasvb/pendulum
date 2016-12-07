angular.module('app', ['ngResource'])
    .config(['$resourceProvider', function($resourceProvider) {
        $resourceProvider.defaults.stripTrailingSlashes = false;
    }])
    .controller('Controller', ['$resource', '$scope', '$timeout', '$interval', function($resource, $scope, $timeout, $interval) {
        var GameStatus = $resource("http://127.0.0.1:8080/");
        var GameConfiguration = $resource("http://127.0.0.1:8080/configuration/");

        $scope.loading = true;
        startStatusLoop();

        //////////

        function startStatusLoop() {
            GameStatus.get(function(gameStatus) {
                if (gameStatus.loaded)
                    initGame();
                else {
                    $timeout(function() {
                        startStatusLoop();
                    }, 100);
                }
            });
        }

        function initGame() {
            $scope.loading = false;

            GameConfiguration.get(function(configuration) {
                var image = 0;
                $interval(function() {
                    $scope.image = configuration.images[image];

                    image = ++image % configuration.images.length;
                }, 50);

                var audio = new Audio(configuration.audio);
                audio.play();

                $scope.loading = false;
            });
        }
    }]);