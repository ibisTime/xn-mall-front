define([
    'app/controller/base',
    'app/util/ajax',
    'app/module/loading/loading',
    'Handlebars'
], function (base, Ajax, loading, Handlebars) {
    init();
    function init(){
        loading.createLoading();
        getBankCardList();
        addListeners();
    }
    // 获取银行卡列表
    function getBankCardList(){
        Ajax.get("802016", {
            userId: base.getUserId(),
            status: "1"
        }).then(function(res){
            loading.hideLoading();
            if(res.success){
                if(res.data.length){
                    var html = "";
                    res.data.forEach(function(item){
                        html += '<div class="bankcard-item-wrap">'+
                            '<div class="bankcard-item">'+
                                '<img src="/static/images/backcard_icon.png?__inline"/>'+
                                '<div class="bankcard-item-right">'+
                                    '<div class="bankcard-item-title">'+item.bankName+'</div>'+
                                    '<div class="bankcard-item-number">'+base.getBankCard(item.bankcardNumber)+'</div>'+
                                '</div>'+
                                '<span class="bankcard-delete" data-code="'+item.code+'">删除</span>'+
                            '</div>'+
                        '</div>';
                    });
                    $("#bankCardContainer").html(html);
                }else{
                    doError();
                }
            }else{
                base.showMsg(res.msg);
            }
        });
    }
    function addListeners(){
        $("#sbtn").on("click", function(){
            location.href = "./add_bankCard.html";
        });
        $("#bankCardContainer").on("click", ".bankcard-delete", function(e){
            e.preventDefault();
            e.stopPropagation();
            var me = $(this);
            deleteBankCard(me.attr("data-code"), me.closest(".bankcard-item-wrap"));
        });
    }
    function deleteBankCard(code, wrap){
        loading.createLoading("删除中...");
        Ajax.post("802011", {
            json: {
                code: code
            }
        }).then(function(res){
            loading.hideLoading();
            if(res.success){
                if($("#bankCardContainer").find(".bankcard-item-wrap").length == 1){
                    doError();
                }else{
                    wrap.remove();
                }
            }else{
                base.showMsg(res.msg);
            }
        });
    }
    function doError() {
        $("#bankCardContainer").html('<div class="bg_fff" style="text-align: center;line-height: 150px;">暂无银行卡</div>');
    }
});
