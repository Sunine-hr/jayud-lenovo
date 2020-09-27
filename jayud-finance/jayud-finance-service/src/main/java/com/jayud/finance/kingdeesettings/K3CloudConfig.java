package com.jayud.finance.kingdeesettings;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "kingdee")
@Component
public class K3CloudConfig implements K3CloudConfigBase {

    /**
     * 金蝶url前缀
     */
    private String url;

    /**
     * 账套ID
     */
    private String dbId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passwd;

    /**
     * 登录接口
     */
    private String login;

    /**
     * 查看接口
     */
    private String view;

    /**
     * 保存接口
     */
    private String save;

    /**
     * 提交接口
     */
    private String submit;


    /**
     * 审核接口
     */
    private String audit;

    /**
     * 状态改变接口
     */
    private String statusConver;

    /**
     * 单据查询
     */
    private String query;

    /**
     * 删除表单
     */
    private String delete;

    /**
     * cookie 失效时间 单位 秒
     */
    private Long expire;

    private String getUrl() {
        return this.url;
    }

    @Override
    public String getLogin() {
        return getUrl() + this.login;
    }

    @Override
    public String getView() {
        return getUrl() + this.view;
    }

    @Override
    public String getSave() {
        return getUrl() + this.save;
    }

    @Override
    public String getSubmit() {
        return getUrl() + this.submit;
    }

    @Override
    public String getAudit() {
        return getUrl() + this.audit;
    }

    @Override
    public String getDelete() {
        return getUrl() + this.delete;
    }

    @Override
    public String getQuery() {
        return getUrl() + this.query;
    }


}
