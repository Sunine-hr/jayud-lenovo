package com.jayud.finance.kingdeesettings;

/**
 * @author william
 * @description
 * @Date: 2020-06-28 18:56
 */
public interface K3CloudConfigBase {


    String getLogin();

    String getView();

    String getSave();

    String getSubmit();

    String getAudit();

    String getQuery();

    void setUrl(String url);

    String getDbId();

    void setDbId(String dbId);

    String getUserName();

    void setUserName(String userName);

    String getPasswd();

    void setPasswd(String passwd);

    void setLogin(String login);

    void setView(String view);

    void setSave(String save);

    void setSubmit(String submit);

    void setAudit(String audit);

    String getStatusConver();

    void setStatusConver(String statusConver);

    void setQuery(String query);

    Long getExpire();

    void setExpire(Long expire);
}
