window.onload=function(){
    postRePage()
}
//得到参数传给后台
function postRePage(){
    //发送get请求
    let params=getUrlParam(window.location.search)
    let page=params.page
    axios.get('/'+page)
    //获取页面的参数
    function getUrlParam(url) {
        var params = {};
        var search = url;
        search = /\?/.test(search) && search.split("?")[1];
        var searchs = /\&/.test(search) ? search.split("&") : [search];
        for (var i = 0; i < searchs.length; i++) {
            if (/\=/.test(searchs[i])) {
                var item = searchs[i].split("=");
                params[item[0]] = item[1];
            };
        };
        return params;
    };