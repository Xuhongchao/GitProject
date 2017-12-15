//在列表有子页面的时候，从子页面返回到当前页的时候，保持上一次的历史记录翻页
angular.module('remberHistory',[]).service('remberHistory', function($location) {
    var optionscopy = "";
    var uiGridCtrlcopy = "";
    var scopecopy = "";
    var histroy = [];
    var writehistory="";
    return {
        write:function(options,uiGridCtrl,$scope){
            if(arguments.length==0){
                return {
                    optionscopy:optionscopy,
                    uiGridCtrlcopy:uiGridCtrlcopy,
                    scopecopy:scopecopy
                }
            }
            optionscopy = options;
            uiGridCtrlcopy = uiGridCtrl;
            scopecopy = $scope;

        },
        save:function(obj){
            for(var i=0;i<histroy.length;i++){
                if(histroy[i].url == obj.url){
                    histroy.splice(i,1);
                    break;
                }
            }
            histroy.push(obj);
        },
        get:function(){
            writehistory=this.write();
            if(histroy.length>0){
                var flag=false;
                for(var i=0;i<histroy.length;i++){
                    if(histroy[i].url == $location.path()){
                        histroycopy=JSON.parse(JSON.stringify(histroy[i]));
                        flag=true;
                        return histroycopy
                        break;
                    }
                }
                if(!flag){
                    return ""
                }
            }else{
                return ""
            }
        },
        rever:function(d){
            if(d !== "" && writehistory !== ""){
                writehistory=this.write();
                for(var i=0;i<histroy.length;i++){
                    if(histroy[i].url == $location.path()){
                        if(writehistory && histroy[i].pageNumber !== writehistory.optionscopy.paginationCurrentPage && writehistory.optionscopy.paginationCurrentPage == 1 ){
                            writehistory.optionscopy.paginationCurrentPage = d.pageNumber;
                            writehistory.optionscopy.paginationPageSize = d.pageSize;
                            // writehistory.optionscopy.hpage = d.pageNumber;
                            // writehistory.scopecopy.paginationApi.seek(d.pageNumber);/*GotoPage(4,d.pageNumber)*/
                            writehistory.scopecopy.GotoPage(d.pageNumber,d.pageNumber);
                            break;
                        }else if(writehistory && histroy[i].pageNumber == writehistory.optionscopy.paginationCurrentPage && writehistory.optionscopy.paginationCurrentPage !== 1){
                            writehistory.optionscopy.paginationCurrentPage = d.pageNumber;
                            writehistory.optionscopy.paginationPageSize = d.pageSize;
                            // writehistory.optionscopy.hpage = d.pageNumber;
                            // writehistory.scopecopy.paginationApi.seek(d.pageNumber);/*GotoPage(4,d.pageNumber)*/
                            writehistory.scopecopy.GotoPage(d.pageNumber,d.pageNumber);
                            writehistory = "";
                            break;
                        }else if(writehistory && histroy[i].pageNumber == writehistory.optionscopy.paginationCurrentPage && writehistory.optionscopy.paginationCurrentPage == 1){
                            writehistory.optionscopy.paginationPageSize = d.pageSize;
                            writehistory = "";
                        }
                    }
                }
            }
        }

    };
})
