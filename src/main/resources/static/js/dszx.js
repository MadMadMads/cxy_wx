function blurStyle(e){
	let x = document.getElementsByClassName(e)[0];
		x.style.border="1px solid lightgray";
		if(e==='advicePhone'){
			surePhone()
		}
		else if(e==='adviceEmail'){
			sureEmail()
}
}
	function focusStyle(e){
		let x = document.getElementsByClassName(e)[0];
			 x.style.border="1px solid blue"
	}
	(function() {
    if (typeof WeixinJSBridge == "object" && typeof WeixinJSBridge.invoke == "function") {
        handleFontSize();
    } else {
        if (document.addEventListener) {
            document.addEventListener("WeixinJSBridgeReady", handleFontSize, false);
        } else if (document.attachEvent) {
            document.attachEvent("WeixinJSBridgeReady", handleFontSize);
            document.attachEvent("onWeixinJSBridgeReady", handleFontSize);
        }
    }

    function handleFontSize() {
        // 设置网页字体为默认大小
        WeixinJSBridge.invoke('setFontSizeCallback', { 'fontSize' : 0 });
        // 重写设置网页字体大小的事件
        WeixinJSBridge.on('menu:setfont', function() {
            WeixinJSBridge.invoke('setFontSizeCallback', { 'fontSize' : 0 });
        });
    }
})();
//校验电话
function surePhone(){
	let tipError=document.getElementsByClassName("phoneFormat")[0]
		let advicePhoneDome=document.getElementsByClassName("advicePhone")[0]
		let advicePhone=advicePhoneDome.value;
		let  reg= /^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/;
		if(advicePhone==''){
			 tipError.style.display='inline-block'
			 tipError.textContent='手机号不能为空'
		}
		 if(!reg.test(advicePhone)){
			 tipError.style.display='inline-block'
		 }
}
//校验邮箱
function sureEmail(){
	let tipError=document.getElementsByClassName("emailFormat")[0]
		let adviceEmailDome=document.getElementsByClassName("adviceEmail")[0]
		let adviceEmail=adviceEmailDome.value;
		let  reg= /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.]){1,2}[A-Za-z\d]{2,5}$/g;
		// console.log(adviceEmail,111)
		if(adviceEmail==''){
			 tipError.style.display='inline-block'
			 tipError.textContent='邮箱不能为空'
		}
		else if(!reg.test(adviceEmail)){
			 tipError.style.display='inline-block'
		 }
}
//填写某个建议上交到后台
function submitAdviceForm(){
	//获取页面的值
	let formAdvice={
		adviceText:'',
		adviceName:'',
		advicePhone:'',
		adviceJob:'',
		adviceDev:'',
		adviceEmail:''
	};
	let adviceText=document.getElementsByClassName("adviceText")[0].innerText;
	let adviceName=document.getElementsByClassName("adviceName")[0].value;
	let advicePhone=document.getElementsByClassName("advicePhone")[0].value;
	let adviceJob=document.getElementsByClassName("adviceJob")[0].value;
	let adviceDev=document.getElementsByClassName("adviceDev")[0].value;
	let adviceEmail=document.getElementsByClassName("adviceEmail")[0].value;
	formAdvice.adviceText=adviceText;
	formAdvice.adviceName=adviceName;
	formAdvice.advicePhone=advicePhone;
	formAdvice.adviceJob=adviceJob;
	formAdvice.adviceDev=adviceDev;
	formAdvice.adviceEmail=adviceEmail;

	console.log(formAdvice,111)
	//向后端发送异步请求
	var xmlhttp = new XMLHttpRequest();//获取对象
	xmlhttp.onreadystatechange = function(){//设置回调函数
	if(xmlhttp.readyState == 4)//这里的4是请求的状态码，代表请求已经完成
		if(xmlhttp.status == 200 || xmlhttp.status == 304)//这里是获得响应的状态码，200代表成功，304代表无修改可以直接从缓存中读取
			var result = xmlhttp.responseText;//这里获取的是响应文本，也可以获得响应xml或JSON
			alert(xmlhttp.status);
      }
	  let url = " ";//POST请求
	  xmlhttp.open("POST",url);
	  xmlhttp.setRequestHeader("Content-Type","application/json;charset=utf-8");
	  xmlhttp.send(formAdvice);//POST请求
	}