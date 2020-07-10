var appid = 'wxa239edbab1bc9683';//微信公众号的唯一id
// var appid = 'wxd54de38d3f859dc8';
let userinfo = {}
var access_token = '';
var open_id = '';
// var appSerect='06a8e07356dd978df4f7df396b4283e0'  //AppSecret
var appSerect = 'cf701651a01eedcc4b592309276be976' //AppSecret
var code = '';
let link = window.location.href;
let page='';
let uri = encodeURIComponent(link);
let authURL =`https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appid}&redirect_uri=${uri}&response_type=code&scope=snsapi_userinfo#wechat_redirect`;

window.onload = function() {
	let params = getUrlParam(window.location.search); // 地址解析,返回地址后面的东西，找出code
	// window.localStorage.removeItem('userinfo')
	if (localStorage.getItem("userinfo")) return; // 已经授权登录过的就不用再授权了
	if (params.code) {
		debugger
		code = params.code;
		// console.log(params.code)
		getAccessToken(code);
	} else {
			debugger
		console.log(link);
		window.location.href = authURL;

	}
}
//获取token
function getAccessToken(code) {
	// 001g8Yt727SNkP0nKTr72csFt72g8YtX
	let getAccessUrl =
		`https://api.weixin.qq.com/sns/oauth2/access_token?appid=${appid}&secret=${appSerect}&code=${code}&grant_type=authorization_code`;
	axios({
		method: 'get',
		url: getAccessUrl,
		dataType:'jsonp',
	}).then(async function(res) {
		access_token = await res.data.access_token;
		open_id = await res.data.openid;
		getUserInfo(access_token, open_id);
		// console.log(res.data, access_token, open_id);
	});
}
//获取userinfo
function getUserInfo(access_token, open_id) {
	let getUserUrl = `https://api.weixin.qq.com/sns/userinfo?access_token=${access_token}&openid=${open_id}&lang=zh_CN`;
	axios({
		method: 'get',
		url: getUserUrl,
		dataType:'jsonp',
	}).then(async function(res) {
		userinfo = await res.data;
		userinfo.page=getPage()
		console.log(res.data, userinfo, 111)
		postUserInfo(userinfo);
		window.localStorage.setItem('userinfo', JSON.stringify(userinfo))
	})
}
//向后端发送用户数据
function postUserInfo(userinfo) {
	let url = '/wxUserLogin';
	let xmlhttp = new XMLHttpRequest(); //获取对象
	xmlhttp.open("POST", url);
	xmlhttp.setRequestHeader("Content-Type", "application/json;charset=utf-8");
	xmlhttp.send(userinfo); //POST请求
}
//对url进行解析,得到一个参数数组
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
//对url进行解析,将页面的html的值取出来给后端
function getPage() {
	let patt1 = /\/([a-z]+)\.html/;
	let urlPage = window.location.href;
	arr = urlPage.match(patt1);
	return arr[1]

}
