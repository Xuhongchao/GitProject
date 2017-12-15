// 菜单导航模块
angular.module('moduleMenu',['ui.tree']).directive('moduleMenu',function ($location,$http) {
    return {
        restrict:'A',
        templateUrl:"app/template/viewModule/viewModuleMenu.html",
        scope:true,
        link:function (scope,element,attrs) {
            scope.nodes="";
            var moduleMenuList=attrs.moduleMenu
            scope.menuList= scope.$eval(moduleMenuList)

            var path=$location.path()

            for(var i=0;i<scope.menuList.length;i++){
                if(!scope.nodes){
                    for(obj in scope.menuList[i]){
                        var str= Object.prototype.toString.call(scope.menuList[i][obj])
                        if(str.split(" ")[1].slice(0,5) == 'Array'){
                            scope.nodes= obj;
                        }
                    }
                }
            }
            scope.showDropFlag = function (item) {
                var nodes=item[scope.nodes];
                if(nodes){
                    for(var i=0;i<nodes.length;i++){
                        if(nodes[i].title){
                            return true;
                        }
                    }
                }
                return false;
            }

            scope.initItem=function (item) {
                if(path==""){
                    return false;
                }else if(item.url==path){
                    return true;
                }else {
                    var nodes=item[scope.nodes];
                    if(nodes){
                        for(var i=0;i<nodes.length;i++){
                            if(nodes[i].url==path){
                                return true;
                            }
                            var childNodes = nodes[i][scope.nodes];
                            if(childNodes){
                                for(var j=0;j<childNodes.length;j++){
                                    if(childNodes[j].url==path){
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            scope.initSubItem = function (item,sub) {
                if(sub.url==path){
                    return true;
                }else{
                    var subNodes=sub[scope.nodes];
                    if(subNodes){
                        for(var i=0;i<subNodes.length;i++){
                            if(subNodes[i].url==path){
                                return true;
                            }

                        }
                    }
                }
            }
            scope.toggled = function (s,item) {
                s.$nodeScope.collapsed=!s.$nodeScope.collapsed;
                angular.forEach(scope.menuList, function(main, index) {
                    if(item === main){
                        item.selected = !item.selected;
                    }
                    else{
                        main.selected = false;
                    }
                });

            }

            scope.loadPage = function (item) {
                $location.url(item.url)
                angular.forEach(scope.menuList,function (main,index) {
                    main.selected = false;
                    if(main[scope.nodes]){
                        angular.forEach(main[scope.nodes],function (mainchild,index) {
                            mainchild.selected = false;
                        })
                    }
                })
                item.selected=true;
            }
            scope.subloadPage = function (item,subItem) {
                angular.forEach(scope.menuList,function (main,index) {
                    main.selected = false;
                    if(main[scope.nodes]){
                        angular.forEach(main[scope.nodes],function (mainchild,index) {
                            mainchild.selected = false;
                        })
                    }
                })
                item.selected = true;
                subItem.selected = true;
            }
        }
    }
})
