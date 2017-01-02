angular.module('app', ['ngResource'])
    .config(['$resourceProvider', function($resourceProvider) {
        $resourceProvider.defaults.stripTrailingSlashes = false;
    }])
    .factory('HueSpot', ['$resource', function($resource) {
        return $resource('http://192.168.0.226/api/tinO-scc47uetEHewvKdeoWczOcMvUHooc9BKbZI/lights/:lightID/state', null,
            {
                'update': { method:'PUT', isArray: true}
            });
    }])
    .factory('HueRoom', ['$resource', function($resource) {
        return $resource('http://192.168.0.226/api/tinO-scc47uetEHewvKdeoWczOcMvUHooc9BKbZI/groups/:roomID/action', null,
            {
                'update': { method:'PUT', isArray: true}
            });
    }])
    .controller('Controller', ['$resource', '$scope', '$timeout', '$interval', 'HueSpot', 'HueRoom', function($resource, $scope, $timeout, $interval, HueSpot, HueRoom) {
        var GameStatus = $resource('http://127.0.0.1:8080/');
        var GameConfiguration = $resource('http://127.0.0.1:8080/game/');

        var vm = this;

        $scope.loading = true;
        startStatusLoop();

        $scope.cardSelected = cardSelected;

        //////////

        function startStatusLoop() {
            GameStatus.get(function(gameStatus) {
                if (gameStatus.ready)
                    initGame();
                else {
                    $timeout(function() {   //TODO use interval and a stop variable
                        startStatusLoop();
                    }, 100);
                }
            });
        }

        function initGame() {
            $scope.loading = true;

            GameConfiguration.get(function(configuration) {
                if (configuration.type == 'picture_flash')
                    startPictureFlash(configuration);
                else if (configuration.type == 'deal_or_no_deal')
                    dealOrNoDeal(configuration);
                else if (configuration.type == 'intro')
                    startIntro(configuration);
                else
                    console.log('unknown game', configuration);

                $scope.loading = false;
            });
        }

        function startPictureFlash(configuration) {
            let start = 0;
            let audioDelay = 0;

            // Skip start scenes
            if (start > 0) {
                // Audio
                audioDelay = configuration.scenes[0].time;
                // Scenes
                let now = configuration.scenes[0].time;
                while (now < start) {
                    if (configuration.scenes[0].time <= start) {
                        now = configuration.scenes[0].time;
                        configuration.scenes.shift();
                        console.log('Skipping scene');
                    }
                    else {
                        configuration.scenes[0].time -= (start - now);
                        break;
                    }
                }
                for (let i = 0; i < configuration.scenes.length; i++)
                    configuration.scenes[i].time -= start;
                // Lights
                now = configuration.lights[0].time;
                while (now < start) {
                    if (configuration.lights[0].time <= start) {
                        now = configuration.lights[0].time;
                        configuration.lights.shift();
                        console.log('Skipping light');
                    }
                    else {
                        configuration.lights[0].time -= (start - now);
                        break;
                    }
                }
                for (let i = 0; i < configuration.lights.length; i++)
                    configuration.lights[i].time -= start;
            }

            // Turn off lights
            HueRoom.update({roomID: 1}, {on: false});

            // Init audio
            var audio = new Audio(configuration.audio.location);

            // Tell template our media type to set the correct background
            $scope.type = configuration.scenes[0].type;

            // Init times
            let startingTime = Date.now();
            if (start == 0) {
                vm.sceneTime = startingTime + configuration.scenes[0].time;
                vm.audioTime = vm.sceneTime;
                vm.lightsTime = startingTime + configuration.lights[0].time;
            }
            else {
                vm.sceneTime = -1;
                vm.audioTime = -1;
                vm.lightsTime = -1;
            }

            console.log(configuration, audioDelay);

            // Action loop
            $interval(function() {
                loop(start, startingTime, configuration, audio, audioDelay);
            }, 100);
            loop(start, startingTime, configuration, audio, audioDelay);
        }

        function loop(start, startingTime, configuration, audio, audioDelay) {
            console.log('loop');
            let now = Date.now();

            // Lights
            if (now > vm.lightsTime) {
                console.log('Time to change the lights!');
                let illumination = configuration.lights.shift();
                if (illumination.state)
                    HueRoom.update({roomID: illumination.state.id}, illumination.state.state);
                else {
                    for (var i = 0; i < illumination.states.length; i++)
                        HueSpot.update({lightID: illumination.states[i].id}, illumination.states[i].state);
                }

                if (configuration.lights.length > 0)
                    vm.lightsTime = startingTime + configuration.lights[0].time;
                else
                    vm.lightsTime = Number.MAX_SAFE_INTEGER;
                console.log('Lights changed');
            }

            // Video/pictures
            if (now > vm.sceneTime) {
                let scene = configuration.scenes.shift();
                $scope.content = scene.content;
                $scope.type = scene.type;
                $scope.showProgress = scene.showProgress;

                if (configuration.scenes.length > 0)
                    vm.sceneTime = startingTime + configuration.scenes[0].time;
                else
                    vm.sceneTime = startingTime + configuration.windows[configuration.windows.length - 1].end;

                if (scene.showProgress) {
                    vm.progressStarted = now;
                    vm.progressInterval = (vm.sceneTime - now) / 100;
                    $scope.progress = 100;
                }
            }

            // Audio
            if (now > vm.audioTime) {
                audio.currentTime = (start + audioDelay) / 1000;
                audio.play();

                vm.audioTime = Number.MAX_SAFE_INTEGER;
            }

            // Progress bar
            if ($scope.showProgress)
                $scope.progress = 100 - Math.floor((now - vm.progressStarted) / vm.progressInterval);
            return now;
        }

        function dealOrNoDeal(configuration) {
            for (var i = 0; i < configuration.distribution.values.length; i++)
                configuration.distribution.values[i] = {value: configuration.distribution.values[i]};
            $scope.board = angular.copy(configuration.distribution.values);

            $scope.points = configuration.distribution.values;
            $scope.type = 'deal';

            vm.bankerTimes = [6, 5, 4, 3, 2, 1, 1, 1, 1, 1, 1];
        }

        function cardSelected(index) {
            $scope.box = index + 1;
            $scope.point = $scope.points[index].value;
            $scope.points[index].hidden = true;
            $scope.board[index].hidden = true;
            $('#cardSelectedModal').modal({});

            $scope.offer = 0;
            var n = 0;
            for (var i = 0; i < $scope.points.length; i++) {
                if (!$scope.points[i].hidden) {
                    $scope.offer += Math.pow($scope.points[i].value, 2);
                    n++;
                }
            }
            $scope.offer = Math.sqrt($scope.offer / n) * (-0.00128 * Math.pow($scope.points.length - n, 2) + 0.032 * ($scope.points.length - n));
            $scope.offer = Math.round($scope.offer);
            console.log(vm.bankerTimes[0], vm.bankerTimes);
            if (--vm.bankerTimes[0] == 0) {
                vm.bankerTimes.shift();
                $('#cardSelectedModal').one('hidden.bs.modal', function () {
                    $('#bankerModal').modal({});

                    if (vm.bankerTimes.length == 0) {
                        $('#bankerModal').one('hidden.bs.modal', function () {
                            initGame(); // Start a new game
                        });
                    }
                });
            }
        }

        function startIntro(configuration) {
            // Init times
            let startingTime = Date.now();
            vm.sceneTime = startingTime;

            // Action loop
            $interval(function() {
                loopIntro(startingTime, configuration);
            }, 100);
            loopIntro(startingTime, configuration);
        }

        function loopIntro(startingTime, configuration) {
            let now = Date.now();

            if (now > vm.sceneTime) {
                let scene = configuration.scenes.shift();
                $scope.content = scene.content;
                $scope.type = 'video';

                if (configuration.scenes.length > 0)
                    vm.sceneTime = startingTime + scene.length;
                else
                    vm.sceneTime = Number.MAX_SAFE_INTEGER;
            }
        }
    }]);