loremIpsumShop = angular.module('loremIpsumShop',['ngRoute']);

loremIpsumShop.controller('loginController', function($scope, $http, $location, peopleService) {
    $scope.loginClick = function() {
        $http.post('/signIn', $scope.email).success(function(data) {
            peopleService.savePeopleResponse(data);
            $location.path('/mainView/' + data.id)
        }).error(function (data) {
            alert('Error !!! ' + data)
        })
    }
});

loremIpsumShop.controller('mainViewController', function($scope, peopleService, $location, $http) {

    $scope.noresultfound=false;
    $scope.user = peopleService.getPeopleResponse();
    $scope.products = [];
    $scope.basketProducts = [];

    $scope.logoutClick = function() {
        $location.path('/login');
    }

    $scope.addToBasket = function() {
        angular.forEach($scope.products, function(product) {
            if (product.selected === true) {
                $scope.basketProducts.push(product)
                product.selected = false
            }
        });
    }

    $scope.clearBasket = function() {
        $scope.basketProducts = [];
    }

    $scope.doSearch = function() {
        $scope.noresultfound=false;
        $http.get('/products/prices/'+$scope.minPrice+'/' + $scope.maxPrice)
            .success(function(data) {
                console.log(data);
                $scope.products = data;
                if (data.length == 0) {
                    $scope.noresultfound=true;
                }
            })
            .error(function (data) {
              alert('Error !!! ' + data)
            })
    }
});


