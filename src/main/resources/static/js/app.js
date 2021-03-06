var app = angular.module('RequestEnvelope', []);
app.controller('SnatchEnvelopeController', ['$scope', '$http', function($scope, $http){
    $scope.users = [];
    $scope.numOfUser =  2;
    $scope.numOfRedEnvelopeRequested = 2000;

    $scope.updateUsersInfo = function(){
        $scope.users = [];
        for (var i = 0; i < $scope.numOfUser; i++){
            $scope.users.push({
                "user": i,
                "remainingEnvelopes": $scope.numOfRedEnvelopeRequested,
                "obtainedEnvelopes": 0,
                "timeStart": null,
                "timeElapsed": null,
                "userIds": [],
                "results": []
            });
        }
    };
    $scope.updateUsersInfo();

    $scope.start = function(){
        $("input").prop('disabled', true);
        var userIds = $scope.generateRandomUserIds();

        // set start time for each user
        $scope.users.forEach(function(user){
            user.timeStart = new Date();
        });

        $scope.snatchEnvelope(userIds);
    };

    $scope.reset = function(){
        $("input").prop('disabled', false);
        $scope.updateUsersInfo();

        var resetUrl = "http://snatchenvelope-env.us-east-2.elasticbeanstalk.com/resetRedisForSnatchEnvelope";
        $http.get(resetUrl).then(function(response){
           console.log("[resetRedis]: ");
           console.log(response.data);
           alert("重設Redis訊息：" + response.data["message"]);
        });
    };

    $scope.snatchEnvelope = function(userIds){
        userIds.forEach(function(userId){
            var url = "http://snatchenvelope-env.us-east-2.elasticbeanstalk.com/snatchEnvelopeByRedis?envelopeId=1&userId=" + userId;
            $http.get(url).then(function(response){
                console.log(response.data);

                // identifier result to corresponding user
                // ex. userId = 5000
                // floor((5000 - 1) / numOfRequested) = 0
                var userIndex = Math.floor((userId - 1) / $scope.numOfRedEnvelopeRequested);
                $scope.users[userIndex].results.push(response.data);

                if (response.data["result"] === true) {
                    // update user info
                    $scope.users[userIndex].remainingEnvelopes -= 1;
                    $scope.users[userIndex].obtainedEnvelopes += 1;

                    // check if enveloped are all obtained
                    // if so, then record elapsed time

                    if (($scope.users[userIndex].remainingEnvelopes / $scope.numOfRedEnvelopeRequested < 0.4) &&
                        $scope.users[userIndex].timeElapsed === null) {
                        $scope.users[userIndex].timeElapsed =
                            $scope.calculateTimeElapsed($scope.users[userIndex].timeStart)
                    }
                }
            });
        });
    };

    $scope.calculateTimeElapsed = function(start){
        var end = new Date();
        var timeDiff = end - start;
        timeDiff /= 1000;
        var seconds = Math.round(timeDiff % 60);
        timeDiff = Math.floor(timeDiff / 60);
        var minutes =  Math.round(timeDiff % 60);
        timeDiff = Math.floor(timeDiff / 60);
        var hour = Math.round(timeDiff % 24);
        timeDiff = Math.floor(timeDiff / 24);
        var days = timeDiff;
        return days.toString() + " days, " + hour.toString() + " hrs, " +
            minutes.toString() + " mins, " + seconds.toString() + " secs";
    };

    $scope.generateRandomUserIds = function(){
        var totalRequests =
            $scope.numOfUser * $scope.numOfRedEnvelopeRequested; // ex 2 * 5000
        var userIds = [];
        for(var i = 1; i <= totalRequests; i++){ userIds.push(i)}
        userIds.sort(function () {
            return Math.random() > 0.5 ? -1 : 1
        });
        return userIds;
    }
}]);