/**
 * @Title AccountAOImpl.java 
 * @Package com.ibis.pz.ao.impl 
 * @Description 
 * @author miyb  
 * @date 2015-5-12 下午3:51:21 
 * @version V1.0   
 */
package com.xnjr.moom.front.ao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.xnjr.moom.front.ao.IAccountAO;
import com.xnjr.moom.front.exception.BizException;
import com.xnjr.moom.front.http.BizConnecter;
import com.xnjr.moom.front.http.JsonUtils;
import com.xnjr.moom.front.req.XN602906Req;
import com.xnjr.moom.front.req.XN802005Req;
import com.xnjr.moom.front.req.XN802010Req;
import com.xnjr.moom.front.req.XN802013Req;
import com.xnjr.moom.front.req.XN802021Req;
import com.xnjr.moom.front.req.XN802110Req;
import com.xnjr.moom.front.req.XN802211Req;
import com.xnjr.moom.front.req.XN802315Req;
import com.xnjr.moom.front.req.XN803900Req;
import com.xnjr.moom.front.req.XNfd0032Req;
import com.xnjr.moom.front.req.XNfd0050Req;
import com.xnjr.moom.front.res.Page;
import com.xnjr.moom.front.session.SessionTimeoutException;

/** 
 * @author: miyb 
 * @since: 2015-5-12 下午3:51:21 
 * @history:
 */
@Service
public class AccountAOImpl implements IAccountAO {

    @Override
    public Object getAccountByUserId(String userId, String currency) {
        if (StringUtils.isBlank(userId)) {
            throw new BizException("A010001", "账号不能为空");
        }
        if (StringUtils.isBlank(currency)) {
            throw new BizException("A010001", "币种不能为空");
        }
        XN802013Req req = new XN802013Req();
        req.setCurrency(currency);
        req.setUserId(userId);
        return BizConnecter.getBizData("802013", JsonUtils.object2Json(req),
            Object.class);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object queryAccountDetail(String userId, String accountNumber,
            String ajNo, String start, String limit, String bizType,
            String dateStart, String dateEnd) {
        if (StringUtils.isBlank(accountNumber)) {
            throw new BizException("A010001", "账号不能为空");
        }
        if (StringUtils.isBlank(start)) {
            throw new BizException("A010001", "页数不能为空");
        }
        if (StringUtils.isBlank(limit)) {
            throw new BizException("A010001", "限制条数不能为空");
        }
        if (StringUtils.isBlank(userId)) {
            throw new BizException("A010001", "用户编号不能为空");
        }
        XN802021Req req = new XN802021Req();
        req.setAccountNumber(accountNumber);
        req.setAjNo(ajNo);
        req.setBizType(bizType);
        req.setDateEnd(dateEnd);
        req.setDateStart(dateStart);
        req.setLimit(limit);
        req.setStart(start);
        req.setUserId(userId);
        return BizConnecter.getBizData("802021", JsonUtils.object2Json(req),
            Object.class);
    }

    public Page getAccountPageInfos(String userId, String accountNumber,
            String status, String realName, String dateStart, String dateEnd,
            String start, String limit) {
        if (StringUtils.isBlank(start)) {
            throw new BizException("A010001", "页数不能为空");
        }
        if (StringUtils.isBlank(limit)) {
            throw new BizException("A010001", "限制条数不能为空");
        }
        if (StringUtils.isBlank(userId)) {
            throw new BizException("A010001", "用户编号不能为空");
        }
        XN802010Req req = new XN802010Req();
        req.setAccountNumber(accountNumber);
        req.setDateEnd(dateEnd);
        req.setDateStart(dateStart);
        req.setLimit(limit);
        req.setRealName(realName);
        req.setStart(start);
        req.setStatus(status);
        req.setUserId(userId);
        return BizConnecter.getBizData("802010", JsonUtils.object2Json(req),
            Page.class);
    }

    @Override
    public Page queryFrozenDetail(String accountNumber, String bizType,
            String dateStart, String dateEnd, String start, String limit,
            String orderColumn, String orderDir) {
        if (StringUtils.isBlank(accountNumber)) {
            throw new BizException("A010001", "账号不能为空");
        }
        if (StringUtils.isBlank(start)) {
            throw new BizException("A010001", "页数不能为空");
        }
        if (StringUtils.isBlank(limit)) {
            throw new BizException("A010001", "限制条数不能为空");
        }
        XNfd0032Req req = new XNfd0032Req();
        req.setAccountNumber(accountNumber);
        req.setBizType(bizType);
        req.setDateStart(dateStart);
        req.setDateEnd(dateEnd);
        req.setStart(start);
        req.setLimit(limit);
        req.setOrderColumn(orderColumn);
        req.setOrderDir(orderDir);
        return BizConnecter.getBizData("fd0032", JsonUtils.object2Json(req),
            Page.class);
    }

    @Override
    public List queryBankList(String channelNo, String isEnable) {
        if (StringUtils.isBlank(channelNo)) {
            throw new BizException("A010001", "支付通道编号不能为空");
        }
        if (StringUtils.isBlank(isEnable)) {
            throw new BizException("A010001", "启用标示不能为空");
        }
        XN802005Req req = new XN802005Req();
        req.setChannelNo(channelNo);
        req.setIsEnable(isEnable);
        return BizConnecter.getBizData("802005", JsonUtils.object2Json(req),
            List.class);
    }

    @Override
    public Object getSumPP(String userId) {
        XN803900Req req = new XN803900Req();
        req.setUserId(userId);
        return BizConnecter.getBizData("yw4900",
            JsonUtils.string2Json("userId", userId), Object.class);
    }

    @Override
    public Page queryRechargeAndWithdraw(String accountNumber, String status,
            String dateStart, String dateEnd, String start, String limit,
            String orderColumn, String orderDir) {
        XNfd0050Req req = new XNfd0050Req();
        req.setAccountNumber(accountNumber);
        req.setStatus(status);
        req.setDateStart(dateStart);
        req.setDateEnd(dateEnd);
        req.setStart(start);
        req.setLimit(limit);
        req.setOrderColumn(orderColumn);
        req.setOrderDir(orderDir);
        return BizConnecter.getBizData("fd0050", JsonUtils.object2Json(req),
            Page.class);
    }

    public Object withdraw(String accountNumber, String amount, String toType,
            String toCode, String toBelong) {
        XN802211Req req = new XN802211Req();
        req.setAccountNumber(accountNumber);
        req.setAmount(amount);
        req.setToCode(toCode);
        req.setToType("alipay");
        req.setToBelong(toBelong);
        return BizConnecter.getBizData("802211", JsonUtils.object2Json(req),
            Object.class);
    }

    public Object recharge(String userId, String accountNumber, String amount,
            String fromType, String fromCode) {
        if (StringUtils.isBlank(userId)) {
            throw new SessionTimeoutException("登录链接已超时，请重新登录.");
        }
        if (StringUtils.isBlank(accountNumber)) {
            throw new BizException("A010001", "账号不能为空");
        }
        if (StringUtils.isBlank(amount)) {
            throw new BizException("A010001", "充值金额不能为空");
        }
        if (StringUtils.isBlank(fromCode)) {
            throw new BizException("A010001", "支付宝账号不能为空");
        }
        XN802110Req req = new XN802110Req();
        req.setAccountNumber(accountNumber);
        req.setAmount(amount);
        req.setFromCode(fromCode);
        req.setFromType("alipay");
        return BizConnecter.getBizData("802110", JsonUtils.object2Json(req),
            Object.class);
    }

    public Object fxIntegral(String fromUser, String toMerchant, String amount,
            String cnyAmount, String jfCashBack, String cnyCashBack) {
        if (StringUtils.isBlank(fromUser)) {
            throw new SessionTimeoutException("登录链接已超时，请重新登录.");
        }
        if (StringUtils.isBlank(amount)) {
            throw new BizException("A010001", "积分数量不能为空");
        }
        if (StringUtils.isBlank(cnyAmount)) {
            throw new BizException("A010001", "人民币数量不能为空");
        }
        toMerchant = "U201600000000000001";
        XN602906Req req = new XN602906Req();
        req.setAmount(amount);
        req.setCnyAmount(cnyAmount);
        req.setCnyCashBack(cnyCashBack);
        req.setFromUser(fromUser);
        req.setJfCashBack(jfCashBack);
        req.setToMerchant(toMerchant);
        return BizConnecter.getBizData("602906", JsonUtils.object2Json(req),
            Object.class);
    }

    public Object buyIntegral(String userId, String amount, String cnyAmount) {
        if (StringUtils.isBlank(userId)) {
            throw new SessionTimeoutException("登录链接已超时，请重新登录.");
        }
        if (StringUtils.isBlank(amount)) {
            throw new BizException("A010001", "积分数量不能为空");
        }
        XN802315Req req = new XN802315Req();
        req.setAmount(amount);
        req.setCnyAmount(cnyAmount);
        req.setUserId(userId);
        return BizConnecter.getBizData("802315", JsonUtils.object2Json(req),
            Object.class);
    }

    @Override
    public Object integralConsume(String fromUser, String toMerchant,
            String amount, String cnyAmount, String jfCashBack,
            String cnyCashBack) {
        if (StringUtils.isBlank(fromUser)) {
            throw new SessionTimeoutException("登录链接已超时，请重新登录.");
        }
        if (StringUtils.isBlank(toMerchant)) {
            throw new BizException("A010001", "商家不能为空");
        }
        if (StringUtils.isBlank(amount)) {
            throw new BizException("A010001", "积分数量不能为空");
        }
        cnyAmount = "0";
        XN602906Req req = new XN602906Req();
        req.setAmount(amount);
        req.setCnyAmount(cnyAmount);
        req.setCnyCashBack(cnyCashBack);
        req.setFromUser(fromUser);
        req.setJfCashBack(jfCashBack);
        req.setToMerchant(toMerchant);
        return BizConnecter.getBizData("602906", JsonUtils.object2Json(req),
            Object.class);
    }
}
