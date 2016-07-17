define([
    'app/controller/base',
    'app/util/ajax',
    'app/util/dict',
    'app/util/dialog',
    'Handlebars'
], function (base, Ajax, Dict, dialog, Handlebars) {
    $(function () {
    	var code = base.getUrlParam("code") || "",
	        type = base.getUrlParam("type") || "1",
	        q = base.getUrlParam("q") || "1",
	        receiptType = Dict.get("receiptType"),
	        contentTmpl = __inline("../ui/submit-order-imgs.handlebars");
    	init();    	
    	function init(){
			(function () {
	            var url = APIURL + '/user/queryAddresses',
	                config = {
	                    "isDefault": ""
	                },
	                addressTmpl = __inline("../ui/submit-order-address.handlebars");
	            Ajax.get(url, config, true)
	                .then(function (response) {
	                	$("#cont").hide();
	                    if(response.success){
	                        var data = response.data,
	                            html1 = "", len = data.length;;
	                        if(len){
	                        	for( var i = 0; i < len; i++ ){
	                        		if(data[i].isDefault == "1"){
	                        			break;
	                        		}
	                        	}
	                        	if(i == len){
	                        		i = 0;
	                        	}
	                        	var content = addressTmpl(data[i]);
            					$("#addressDiv").append(content);
            					$("#addressRight").removeClass("hidden");
	                        }else{
	                        	$("#noAddressDiv").removeClass("hidden");
	                        }
	                    }else{
	                        doError("#addressDiv");
	                    }
	                });
	        })();
	        if(type == 1){
	            getModel();
	        }else if(type == 2){
	            code = code.split(/_/);
	            getModel1();
	        }
	        (function () {
	            var html = '<option value="0">无</option>';
	            for(var rec in receiptType){
	                html += '<option value="'+rec+'">'+receiptType[rec]+'</option>';
	            }
	            $("#receipt").html(html);
	        })();
	        addListeners();
    	}
    	function doError(cc) {
            $(cc).html('<div class="bg_fff" style="text-align: center;line-height: 150px;">暂时无法获取数据</div>');
        }
    	function getModel1() {
	        var url = APIURL + '/operators/queryCart';
	        Ajax.get(url, true)
	            .then(function(response) {
	                if (response.success) {
	                    var data = response.data,
	                        html = "",
	                        totalCount = 0;
	                    if(data.length){
	                    	var items = [];
	                        for(var i = 0, len = code.length; i < len; i++){
	                            var d = data[code[i]];
	                        	var eachCount = (+d.salePrice) * (+d.quantity);
	                        	d.totalAmount = (eachCount / 1000).toFixed(0);
	                            totalCount += eachCount;
	                            items.push(d);
	                        }
	                        var html = contentTmpl({items: items});
	                        $("#cont").hide();
	                        $("#items-cont").append(html);
	                        $("#totalAmount").html( (totalCount / 1000).toFixed(0) );
	                    }else{
	                    	$("#cont").hide();
	                    	doError("#items-cont");
	                    }
	                }else{
	                	$("#cont").hide();
	                    doError("#items-cont");
	                }
	            });
	    }

	    function getModel() {
	        var url = APIURL + '/commodity/queryModel',
	            config = {
	                "code": code
	            };
	        Ajax.get(url, config)
	            .then(function(response){
	                if(response.success){
	                    var data = response.data,
	                        items = [];
                        var eachCount = +data.buyGuideList[0].discountPrice * +q;
                        	data.totalAmount = (eachCount / 1000).toFixed(0);
                        data.quantity = q;
                        data.modelName = data.name;
                        items.push(data);
	                    var html = contentTmpl({items: items});
	                    $("#items-cont").append(html);
	                    $("#totalAmount").html( ((+data.buyGuideList[0].discountPrice) * q / 1000).toFixed(0) );
	                    $("#cont").hide();
	                }else{
	                    doError("#items-cont");
	                }
	            });
	    }
    	function addListeners(){
    		$("#addressDiv").on("click", "a", function(){
    			if(this.id=="add-addr"){
    				location.href = "./add_address.html?return=" + encodeURIComponent(location.pathname + location.search);
    			}else{
    				location.href = "./address_list.html?c=" + $(this).attr("code") + "&return=" + encodeURIComponent(location.pathname + location.search);
    			}
    		});
			$("#sbtn").on("click", function () {
				var $a = $("#addressDiv>a");
	            if($a.length){
	            	var receiptT = $("#receiptTitle").val(),
	            		receiptV = $("#receipt").val();
	                if(receiptT.length > 32){
						showMsg("发票抬头字数必须少于32位");
	                    return;
	                }
	                if($("#apply_note").val().length > 255){
	                	showMsg("备注字数必须少于255位");
	                	return;
	                }
	                if(receiptT.length && receiptV == "0" || !receiptT.length && receiptV != "0"){
	                	if(receiptT.length){
	                		$("#od-tipbox>div:eq(1)").text("您还未选择发票类型，确定提交订单吗？");
	                	}else{
	                		$("#od-tipbox>div:eq(1)").text("您还未填写发票抬头，确定提交订单吗？");
	                	}
	                	$("#od-mask, #od-tipbox").removeClass("hidden");
	                	return;
	                }
	                PrepareConfig();
	            }else{
					showMsg("未选择地址");
	            }
	        });
			
			$("#odOk").on("click", function(){
				PrepareConfig();
	        	$("#od-mask, #od-tipbox").addClass("hidden");
	        });
	        $("#odCel").on("click", function(){
	        	$("#od-mask, #od-tipbox").addClass("hidden");
	        });
    	}
    	
    	function PrepareConfig(){
    		var url = APIURL + '/operators/submitOrder',
				config;
	        if(type == 1){
	        	var tPrice = (+$("#items-cont").find(".item_totalP").text()) * 1000;
	            config = {
	                "modelCode": code,
	                "quantity":	q,
	                "salePrice": tPrice / +q,
	                "addressCode": $("#addressDiv>a").attr("code"),
	                "receiptType": ($("#receipt").val() == "0" ? "": $("#receipt").val()),
	                "receiptTitle": $("#receiptTitle").val(),
	                "applyNote": $("#apply_note").val() || ""
	            };
	        }else if(type == 2){
	            var cartList = [],
	                $lis = $("#items-cont > ul > li");
	            for(var i = 0, len = $lis.length; i < len; i++){
	                cartList.push($($lis[i]).attr("modelCode"));
	            }
	            var config = {
	                "addressCode": $("#addressDiv>a").attr("code"),
	                "receiptType": ($("#receipt").val() == "0" ? "": $("#receipt").val()),
	                "receiptTitle": $("#receiptTitle").val(),
	                "applyNote": "",
	                "cartCodeList": cartList
	            };
	            url = APIURL + '/operators/submitCart';
	        }else{
	        	showMsg("类型错误，无法提交订单");
	            return;
	        }
	        doSubmitOrder(config, url);
    	}
    	
    	function showMsg(cont){
    		var d = dialog({
						content: cont,
						quickClose: true
					});
			d.show();
			setTimeout(function () {
				d.close().remove();
			}, 2000);
    	}

    	function doSubmitOrder(config, url){
    		Ajax.post(url, config)
				.then(function (response) {
					if(response.success){
						var code = response.data || response.data.code;
						location.href = './pay_order.html?code='+code;
					}else{
						showMsg(response.msg);
					}
				});
    	}
    });
});