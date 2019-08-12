package com.capitalone.dashboard.exec.config;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "singleton")
public class LdapHandler {

    private static final Logger LOGGER = Logger.getLogger(LdapHandler.class);

    private static LdapTemplate ldapTemplate = null;

    private String ldapurl;

    private String ldapbase;

    private String username;

    private String password;

    public LdapHandler(@Value("${ldap.urls:none}") String url, @Value("${ldap.base:none}") String base,
                       @Value("${ldap.username:none}") String username, @Value("${ldap.password:none}") String ldappassword) {

        this.ldapurl = url;
        this.ldapbase = base;
        this.username = username;
        this.password = ldappassword;
    }

    public LdapTemplate initLdaptemplate() {
        if (ldapTemplate == null) {

            LdapContextSource contextSource = new LdapContextSource();
            contextSource.setUrl(ldapurl);
            contextSource.setBase(ldapbase);
            contextSource.setUserDn(username + "@gsm1900.org");
            contextSource.setPassword(password);
            contextSource.afterPropertiesSet();

            try {
                ldapTemplate = new LdapTemplate(contextSource);
                ldapTemplate.afterPropertiesSet();
                Filter filter = new EqualsFilter("cn", username);
                ldapTemplate.authenticate("OU=Technology,OU=User Accounts", filter.encode(), password);
                LOGGER.info("LDAP template initialization successful ! ");
            } catch (org.springframework.ldap.CommunicationException e) {
                LOGGER.info("LDAP template initialization failed ! ");
            } catch (Exception e) {
                LOGGER.error(ExceptionUtils.getStackTrace(e));
            }
        }
        return ldapTemplate;
    }

    public static LdapTemplate getLdaptemplate() {
        return ldapTemplate;
    }

}
