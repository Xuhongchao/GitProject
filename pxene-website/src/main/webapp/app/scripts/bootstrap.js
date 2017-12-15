define(['angular', 'jquery','appAll'], function () {

    /*require(['domReady!'], function (document) {
        angular.bootstrap(document, [app.name])
    });*/

    $(document).ready(function() {
        angular.bootstrap(document, ['dmpwebApp'])
    });
});