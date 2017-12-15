define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.indexCtrl', []).controller('indexCtrl',  function ($scope,$location,gridService) {
            $scope.listMenu = [
                {
                    "title":"参数管理",
                    "url":"/grabParamManage",
                    "icon":"icon-paramManage",
                    "nodechild": [
                        {
                            "nodechild": [
                                {"url":"/detailParam"},
                            ]
                        }
                    ]
                },
                {
                    "title":"APP分类",
                    "url":"/grabAppClassify",
                    "icon":"icon-APPClassify",
                    "nodechild": []
                },
                {
                    "title":"行为管理",
                    "url":"/grabBehaviorManage",
                    "icon":"icon-behaviorManage",
                    "nodechild": [
                    ]
                },
                {
                    "title":"抓取列表",
                    "icon":"icon-appList",
                    "nodechild": [
                        {
                            "title":"待抓取",
                            "url":"/grabWaitApp",
                            "icon":"icon-labelManage-01",
                            "nodechild": [
                                {"url":"/grabWaitAppIos"},
                                {"url":"/grabWaitAppPc"},
                                {"url":"/grabWaitAppMobile"},
                            ]
                        },
                        {
                            "title":"已抓取",
                            "url":"/grabAleadyApp",
                            "icon":"icon-finaceManage-01",
                            "nodechild": [
                                {"url":"/detailAleadyApp"},
                                {"url":"/addDetailAleadyAPP"},
                                {"url":"/detailAleadyAppDomation"},
                            ]
                        },
                    ]
                },
                {
                    "title":"统计信息",
                    "url":"/countInfoParam",
                    "icon":"icon-countInfo",
                    "nodechild": [
                        {
                            "nodechild": [
                                {"url":"/countInfoAppClassify"},
                                {"url":"/countInfoAppSource"},
                                {"url":"/countInfoGrabBehavior"},
                                {"url":"/countInfoUserNumber"},
                                {"url":"/countInfoWechat"},
                                {"url":"/detailcountInfoParam"},
                                {"url":"/detailcountInfoGrabBehavior"},
                            ]
                        }
                    ]
                },

            ];

        gridService.getMenu($scope.listMenu);
        })
});

