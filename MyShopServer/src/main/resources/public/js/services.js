loremIpsumShop.factory('peopleService', function () {
                       var peopleResponse = {};

                       return {
                           savePeopleResponse:function (data) {
                               peopleResponse = data;
                               console.log(data);
                           },
                           getPeopleResponse:function () {
                               return peopleResponse;
                           }
                       };
                   });

loremIpsumShop.factory('productService', function () {
                       var productResponse = {};

                       return {
                           saveProductResponse:function (data) {
                               productResponse = data;
                               console.log(data);
                           },
                           getProductResponse:function () {
                               return productResponse;
                           }
                       };
                   });