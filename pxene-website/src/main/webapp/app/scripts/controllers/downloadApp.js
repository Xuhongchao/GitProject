define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.downloadAppCtrl', [])
        .controller('downloadAppCtrl', ['$scope', '$location', '$http','$uibModal','getPage','gridService','$uibModalInstance','objselect', function ($scope,$location,$http,$uibModal,getPage,gridService,$uibModalInstance,objselect) {
            var $ctrl=this;
            var int="";
            $ctrl.headpath = objselect.app_logo_url;     //最上方的小图片
            $ctrl.appname = objselect.app_name;
            $ctrl.qrpath = objselect.app_download_url;     //二维码图片
            repeatQrcode = function () {
                var d=document.getElementById("code")
                if(d==null){
                    return;
                }
                var qrcode = new QRCode(window.document.getElementById("code"), {
                    width : 165,
                    height : 165
                });
                qrcode.makeCode($ctrl.qrpath );
                clearInterval(int);
            }
            if( $ctrl.qrpath ){
                int=setInterval("repeatQrcode()",10);
            }

            $ctrl.ok = function () {
                $uibModalInstance.dismiss('cancel');
            };
            $ctrl.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

        }])
});
