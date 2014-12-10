loremIpsumShop.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/login', {
          templateUrl: 'views/login.html',
          controller: 'loginController'
        }).
        when('/mainView/:userId', {
          templateUrl: 'views/main-view.html',
          controller: 'mainViewController'
        }).
        otherwise({
          redirectTo: '/login'
        });
    }]);