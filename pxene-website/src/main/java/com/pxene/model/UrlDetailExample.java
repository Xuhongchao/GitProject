package com.pxene.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class UrlDetailExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UrlDetailExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUrlcodeIsNull() {
            addCriterion("urlCode is null");
            return (Criteria) this;
        }

        public Criteria andUrlcodeIsNotNull() {
            addCriterion("urlCode is not null");
            return (Criteria) this;
        }

        public Criteria andUrlcodeEqualTo(String value) {
            addCriterion("urlCode =", value, "urlcode");
            return (Criteria) this;
        }

        public Criteria andUrlcodeNotEqualTo(String value) {
            addCriterion("urlCode <>", value, "urlcode");
            return (Criteria) this;
        }

        public Criteria andUrlcodeGreaterThan(String value) {
            addCriterion("urlCode >", value, "urlcode");
            return (Criteria) this;
        }

        public Criteria andUrlcodeGreaterThanOrEqualTo(String value) {
            addCriterion("urlCode >=", value, "urlcode");
            return (Criteria) this;
        }

        public Criteria andUrlcodeLessThan(String value) {
            addCriterion("urlCode <", value, "urlcode");
            return (Criteria) this;
        }

        public Criteria andUrlcodeLessThanOrEqualTo(String value) {
            addCriterion("urlCode <=", value, "urlcode");
            return (Criteria) this;
        }

        public Criteria andUrlcodeLike(String value) {
            addCriterion("urlCode like", value, "urlcode");
            return (Criteria) this;
        }

        public Criteria andUrlcodeNotLike(String value) {
            addCriterion("urlCode not like", value, "urlcode");
            return (Criteria) this;
        }

        public Criteria andUrlcodeIn(List<String> values) {
            addCriterion("urlCode in", values, "urlcode");
            return (Criteria) this;
        }

        public Criteria andUrlcodeNotIn(List<String> values) {
            addCriterion("urlCode not in", values, "urlcode");
            return (Criteria) this;
        }

        public Criteria andUrlcodeBetween(String value1, String value2) {
            addCriterion("urlCode between", value1, value2, "urlcode");
            return (Criteria) this;
        }

        public Criteria andUrlcodeNotBetween(String value1, String value2) {
            addCriterion("urlCode not between", value1, value2, "urlcode");
            return (Criteria) this;
        }

        public Criteria andAppcodeIsNull() {
            addCriterion("appCode is null");
            return (Criteria) this;
        }

        public Criteria andAppcodeIsNotNull() {
            addCriterion("appCode is not null");
            return (Criteria) this;
        }

        public Criteria andAppcodeEqualTo(String value) {
            addCriterion("appCode =", value, "appcode");
            return (Criteria) this;
        }

        public Criteria andAppcodeNotEqualTo(String value) {
            addCriterion("appCode <>", value, "appcode");
            return (Criteria) this;
        }

        public Criteria andAppcodeGreaterThan(String value) {
            addCriterion("appCode >", value, "appcode");
            return (Criteria) this;
        }

        public Criteria andAppcodeGreaterThanOrEqualTo(String value) {
            addCriterion("appCode >=", value, "appcode");
            return (Criteria) this;
        }

        public Criteria andAppcodeLessThan(String value) {
            addCriterion("appCode <", value, "appcode");
            return (Criteria) this;
        }

        public Criteria andAppcodeLessThanOrEqualTo(String value) {
            addCriterion("appCode <=", value, "appcode");
            return (Criteria) this;
        }

        public Criteria andAppcodeLike(String value) {
            addCriterion("appCode like", value, "appcode");
            return (Criteria) this;
        }

        public Criteria andAppcodeNotLike(String value) {
            addCriterion("appCode not like", value, "appcode");
            return (Criteria) this;
        }

        public Criteria andAppcodeIn(List<String> values) {
            addCriterion("appCode in", values, "appcode");
            return (Criteria) this;
        }

        public Criteria andAppcodeNotIn(List<String> values) {
            addCriterion("appCode not in", values, "appcode");
            return (Criteria) this;
        }

        public Criteria andAppcodeBetween(String value1, String value2) {
            addCriterion("appCode between", value1, value2, "appcode");
            return (Criteria) this;
        }

        public Criteria andAppcodeNotBetween(String value1, String value2) {
            addCriterion("appCode not between", value1, value2, "appcode");
            return (Criteria) this;
        }

        public Criteria andIndustrycodeIsNull() {
            addCriterion("industryCode is null");
            return (Criteria) this;
        }

        public Criteria andIndustrycodeIsNotNull() {
            addCriterion("industryCode is not null");
            return (Criteria) this;
        }

        public Criteria andIndustrycodeEqualTo(String value) {
            addCriterion("industryCode =", value, "industrycode");
            return (Criteria) this;
        }

        public Criteria andIndustrycodeNotEqualTo(String value) {
            addCriterion("industryCode <>", value, "industrycode");
            return (Criteria) this;
        }

        public Criteria andIndustrycodeGreaterThan(String value) {
            addCriterion("industryCode >", value, "industrycode");
            return (Criteria) this;
        }

        public Criteria andIndustrycodeGreaterThanOrEqualTo(String value) {
            addCriterion("industryCode >=", value, "industrycode");
            return (Criteria) this;
        }

        public Criteria andIndustrycodeLessThan(String value) {
            addCriterion("industryCode <", value, "industrycode");
            return (Criteria) this;
        }

        public Criteria andIndustrycodeLessThanOrEqualTo(String value) {
            addCriterion("industryCode <=", value, "industrycode");
            return (Criteria) this;
        }

        public Criteria andIndustrycodeLike(String value) {
            addCriterion("industryCode like", value, "industrycode");
            return (Criteria) this;
        }

        public Criteria andIndustrycodeNotLike(String value) {
            addCriterion("industryCode not like", value, "industrycode");
            return (Criteria) this;
        }

        public Criteria andIndustrycodeIn(List<String> values) {
            addCriterion("industryCode in", values, "industrycode");
            return (Criteria) this;
        }

        public Criteria andIndustrycodeNotIn(List<String> values) {
            addCriterion("industryCode not in", values, "industrycode");
            return (Criteria) this;
        }

        public Criteria andIndustrycodeBetween(String value1, String value2) {
            addCriterion("industryCode between", value1, value2, "industrycode");
            return (Criteria) this;
        }

        public Criteria andIndustrycodeNotBetween(String value1, String value2) {
            addCriterion("industryCode not between", value1, value2, "industrycode");
            return (Criteria) this;
        }

        public Criteria andBehaviorIsNull() {
            addCriterion("behavior is null");
            return (Criteria) this;
        }

        public Criteria andBehaviorIsNotNull() {
            addCriterion("behavior is not null");
            return (Criteria) this;
        }

        public Criteria andBehaviorEqualTo(String value) {
            addCriterion("behavior =", value, "behavior");
            return (Criteria) this;
        }

        public Criteria andBehaviorNotEqualTo(String value) {
            addCriterion("behavior <>", value, "behavior");
            return (Criteria) this;
        }

        public Criteria andBehaviorGreaterThan(String value) {
            addCriterion("behavior >", value, "behavior");
            return (Criteria) this;
        }

        public Criteria andBehaviorGreaterThanOrEqualTo(String value) {
            addCriterion("behavior >=", value, "behavior");
            return (Criteria) this;
        }

        public Criteria andBehaviorLessThan(String value) {
            addCriterion("behavior <", value, "behavior");
            return (Criteria) this;
        }

        public Criteria andBehaviorLessThanOrEqualTo(String value) {
            addCriterion("behavior <=", value, "behavior");
            return (Criteria) this;
        }

        public Criteria andBehaviorLike(String value) {
            addCriterion("behavior like", value, "behavior");
            return (Criteria) this;
        }

        public Criteria andBehaviorNotLike(String value) {
            addCriterion("behavior not like", value, "behavior");
            return (Criteria) this;
        }

        public Criteria andBehaviorIn(List<String> values) {
            addCriterion("behavior in", values, "behavior");
            return (Criteria) this;
        }

        public Criteria andBehaviorNotIn(List<String> values) {
            addCriterion("behavior not in", values, "behavior");
            return (Criteria) this;
        }

        public Criteria andBehaviorBetween(String value1, String value2) {
            addCriterion("behavior between", value1, value2, "behavior");
            return (Criteria) this;
        }

        public Criteria andBehaviorNotBetween(String value1, String value2) {
            addCriterion("behavior not between", value1, value2, "behavior");
            return (Criteria) this;
        }

        public Criteria andPathIsNull() {
            addCriterion("path is null");
            return (Criteria) this;
        }

        public Criteria andPathIsNotNull() {
            addCriterion("path is not null");
            return (Criteria) this;
        }

        public Criteria andPathEqualTo(String value) {
            addCriterion("path =", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathNotEqualTo(String value) {
            addCriterion("path <>", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathGreaterThan(String value) {
            addCriterion("path >", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathGreaterThanOrEqualTo(String value) {
            addCriterion("path >=", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathLessThan(String value) {
            addCriterion("path <", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathLessThanOrEqualTo(String value) {
            addCriterion("path <=", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathLike(String value) {
            addCriterion("path like", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathNotLike(String value) {
            addCriterion("path not like", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathIn(List<String> values) {
            addCriterion("path in", values, "path");
            return (Criteria) this;
        }

        public Criteria andPathNotIn(List<String> values) {
            addCriterion("path not in", values, "path");
            return (Criteria) this;
        }

        public Criteria andPathBetween(String value1, String value2) {
            addCriterion("path between", value1, value2, "path");
            return (Criteria) this;
        }

        public Criteria andPathNotBetween(String value1, String value2) {
            addCriterion("path not between", value1, value2, "path");
            return (Criteria) this;
        }

        public Criteria andPathtypeIsNull() {
            addCriterion("pathType is null");
            return (Criteria) this;
        }

        public Criteria andPathtypeIsNotNull() {
            addCriterion("pathType is not null");
            return (Criteria) this;
        }

        public Criteria andPathtypeEqualTo(String value) {
            addCriterion("pathType =", value, "pathtype");
            return (Criteria) this;
        }

        public Criteria andPathtypeNotEqualTo(String value) {
            addCriterion("pathType <>", value, "pathtype");
            return (Criteria) this;
        }

        public Criteria andPathtypeGreaterThan(String value) {
            addCriterion("pathType >", value, "pathtype");
            return (Criteria) this;
        }

        public Criteria andPathtypeGreaterThanOrEqualTo(String value) {
            addCriterion("pathType >=", value, "pathtype");
            return (Criteria) this;
        }

        public Criteria andPathtypeLessThan(String value) {
            addCriterion("pathType <", value, "pathtype");
            return (Criteria) this;
        }

        public Criteria andPathtypeLessThanOrEqualTo(String value) {
            addCriterion("pathType <=", value, "pathtype");
            return (Criteria) this;
        }

        public Criteria andPathtypeLike(String value) {
            addCriterion("pathType like", value, "pathtype");
            return (Criteria) this;
        }

        public Criteria andPathtypeNotLike(String value) {
            addCriterion("pathType not like", value, "pathtype");
            return (Criteria) this;
        }

        public Criteria andPathtypeIn(List<String> values) {
            addCriterion("pathType in", values, "pathtype");
            return (Criteria) this;
        }

        public Criteria andPathtypeNotIn(List<String> values) {
            addCriterion("pathType not in", values, "pathtype");
            return (Criteria) this;
        }

        public Criteria andPathtypeBetween(String value1, String value2) {
            addCriterion("pathType between", value1, value2, "pathtype");
            return (Criteria) this;
        }

        public Criteria andPathtypeNotBetween(String value1, String value2) {
            addCriterion("pathType not between", value1, value2, "pathtype");
            return (Criteria) this;
        }

        public Criteria andOperatortypeIsNull() {
            addCriterion("operatorType is null");
            return (Criteria) this;
        }

        public Criteria andOperatortypeIsNotNull() {
            addCriterion("operatorType is not null");
            return (Criteria) this;
        }

        public Criteria andOperatortypeEqualTo(String value) {
            addCriterion("operatorType =", value, "operatortype");
            return (Criteria) this;
        }

        public Criteria andOperatortypeNotEqualTo(String value) {
            addCriterion("operatorType <>", value, "operatortype");
            return (Criteria) this;
        }

        public Criteria andOperatortypeGreaterThan(String value) {
            addCriterion("operatorType >", value, "operatortype");
            return (Criteria) this;
        }

        public Criteria andOperatortypeGreaterThanOrEqualTo(String value) {
            addCriterion("operatorType >=", value, "operatortype");
            return (Criteria) this;
        }

        public Criteria andOperatortypeLessThan(String value) {
            addCriterion("operatorType <", value, "operatortype");
            return (Criteria) this;
        }

        public Criteria andOperatortypeLessThanOrEqualTo(String value) {
            addCriterion("operatorType <=", value, "operatortype");
            return (Criteria) this;
        }

        public Criteria andOperatortypeLike(String value) {
            addCriterion("operatorType like", value, "operatortype");
            return (Criteria) this;
        }

        public Criteria andOperatortypeNotLike(String value) {
            addCriterion("operatorType not like", value, "operatortype");
            return (Criteria) this;
        }

        public Criteria andOperatortypeIn(List<String> values) {
            addCriterion("operatorType in", values, "operatortype");
            return (Criteria) this;
        }

        public Criteria andOperatortypeNotIn(List<String> values) {
            addCriterion("operatorType not in", values, "operatortype");
            return (Criteria) this;
        }

        public Criteria andOperatortypeBetween(String value1, String value2) {
            addCriterion("operatorType between", value1, value2, "operatortype");
            return (Criteria) this;
        }

        public Criteria andOperatortypeNotBetween(String value1, String value2) {
            addCriterion("operatorType not between", value1, value2, "operatortype");
            return (Criteria) this;
        }

        public Criteria andParamIsNull() {
            addCriterion("param is null");
            return (Criteria) this;
        }

        public Criteria andParamIsNotNull() {
            addCriterion("param is not null");
            return (Criteria) this;
        }

        public Criteria andParamEqualTo(String value) {
            addCriterion("param =", value, "param");
            return (Criteria) this;
        }

        public Criteria andParamNotEqualTo(String value) {
            addCriterion("param <>", value, "param");
            return (Criteria) this;
        }

        public Criteria andParamGreaterThan(String value) {
            addCriterion("param >", value, "param");
            return (Criteria) this;
        }

        public Criteria andParamGreaterThanOrEqualTo(String value) {
            addCriterion("param >=", value, "param");
            return (Criteria) this;
        }

        public Criteria andParamLessThan(String value) {
            addCriterion("param <", value, "param");
            return (Criteria) this;
        }

        public Criteria andParamLessThanOrEqualTo(String value) {
            addCriterion("param <=", value, "param");
            return (Criteria) this;
        }

        public Criteria andParamLike(String value) {
            addCriterion("param like", value, "param");
            return (Criteria) this;
        }

        public Criteria andParamNotLike(String value) {
            addCriterion("param not like", value, "param");
            return (Criteria) this;
        }

        public Criteria andParamIn(List<String> values) {
            addCriterion("param in", values, "param");
            return (Criteria) this;
        }

        public Criteria andParamNotIn(List<String> values) {
            addCriterion("param not in", values, "param");
            return (Criteria) this;
        }

        public Criteria andParamBetween(String value1, String value2) {
            addCriterion("param between", value1, value2, "param");
            return (Criteria) this;
        }

        public Criteria andParamNotBetween(String value1, String value2) {
            addCriterion("param not between", value1, value2, "param");
            return (Criteria) this;
        }

        public Criteria andRegexurlIsNull() {
            addCriterion("regexurl is null");
            return (Criteria) this;
        }

        public Criteria andRegexurlIsNotNull() {
            addCriterion("regexurl is not null");
            return (Criteria) this;
        }

        public Criteria andRegexurlEqualTo(String value) {
            addCriterion("regexurl =", value, "regexurl");
            return (Criteria) this;
        }

        public Criteria andRegexurlNotEqualTo(String value) {
            addCriterion("regexurl <>", value, "regexurl");
            return (Criteria) this;
        }

        public Criteria andRegexurlGreaterThan(String value) {
            addCriterion("regexurl >", value, "regexurl");
            return (Criteria) this;
        }

        public Criteria andRegexurlGreaterThanOrEqualTo(String value) {
            addCriterion("regexurl >=", value, "regexurl");
            return (Criteria) this;
        }

        public Criteria andRegexurlLessThan(String value) {
            addCriterion("regexurl <", value, "regexurl");
            return (Criteria) this;
        }

        public Criteria andRegexurlLessThanOrEqualTo(String value) {
            addCriterion("regexurl <=", value, "regexurl");
            return (Criteria) this;
        }

        public Criteria andRegexurlLike(String value) {
            addCriterion("regexurl like", value, "regexurl");
            return (Criteria) this;
        }

        public Criteria andRegexurlNotLike(String value) {
            addCriterion("regexurl not like", value, "regexurl");
            return (Criteria) this;
        }

        public Criteria andRegexurlIn(List<String> values) {
            addCriterion("regexurl in", values, "regexurl");
            return (Criteria) this;
        }

        public Criteria andRegexurlNotIn(List<String> values) {
            addCriterion("regexurl not in", values, "regexurl");
            return (Criteria) this;
        }

        public Criteria andRegexurlBetween(String value1, String value2) {
            addCriterion("regexurl between", value1, value2, "regexurl");
            return (Criteria) this;
        }

        public Criteria andRegexurlNotBetween(String value1, String value2) {
            addCriterion("regexurl not between", value1, value2, "regexurl");
            return (Criteria) this;
        }

        public Criteria andUrlIsNull() {
            addCriterion("url is null");
            return (Criteria) this;
        }

        public Criteria andUrlIsNotNull() {
            addCriterion("url is not null");
            return (Criteria) this;
        }

        public Criteria andUrlEqualTo(String value) {
            addCriterion("url =", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotEqualTo(String value) {
            addCriterion("url <>", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThan(String value) {
            addCriterion("url >", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThanOrEqualTo(String value) {
            addCriterion("url >=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThan(String value) {
            addCriterion("url <", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThanOrEqualTo(String value) {
            addCriterion("url <=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLike(String value) {
            addCriterion("url like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotLike(String value) {
            addCriterion("url not like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlIn(List<String> values) {
            addCriterion("url in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotIn(List<String> values) {
            addCriterion("url not in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlBetween(String value1, String value2) {
            addCriterion("url between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotBetween(String value1, String value2) {
            addCriterion("url not between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andTipIsNull() {
            addCriterion("tip is null");
            return (Criteria) this;
        }

        public Criteria andTipIsNotNull() {
            addCriterion("tip is not null");
            return (Criteria) this;
        }

        public Criteria andTipEqualTo(String value) {
            addCriterion("tip =", value, "tip");
            return (Criteria) this;
        }

        public Criteria andTipNotEqualTo(String value) {
            addCriterion("tip <>", value, "tip");
            return (Criteria) this;
        }

        public Criteria andTipGreaterThan(String value) {
            addCriterion("tip >", value, "tip");
            return (Criteria) this;
        }

        public Criteria andTipGreaterThanOrEqualTo(String value) {
            addCriterion("tip >=", value, "tip");
            return (Criteria) this;
        }

        public Criteria andTipLessThan(String value) {
            addCriterion("tip <", value, "tip");
            return (Criteria) this;
        }

        public Criteria andTipLessThanOrEqualTo(String value) {
            addCriterion("tip <=", value, "tip");
            return (Criteria) this;
        }

        public Criteria andTipLike(String value) {
            addCriterion("tip like", value, "tip");
            return (Criteria) this;
        }

        public Criteria andTipNotLike(String value) {
            addCriterion("tip not like", value, "tip");
            return (Criteria) this;
        }

        public Criteria andTipIn(List<String> values) {
            addCriterion("tip in", values, "tip");
            return (Criteria) this;
        }

        public Criteria andTipNotIn(List<String> values) {
            addCriterion("tip not in", values, "tip");
            return (Criteria) this;
        }

        public Criteria andTipBetween(String value1, String value2) {
            addCriterion("tip between", value1, value2, "tip");
            return (Criteria) this;
        }

        public Criteria andTipNotBetween(String value1, String value2) {
            addCriterion("tip not between", value1, value2, "tip");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("state is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("state is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(String value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(String value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(String value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(String value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(String value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(String value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLike(String value) {
            addCriterion("state like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotLike(String value) {
            addCriterion("state not like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<String> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<String> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(String value1, String value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(String value1, String value2) {
            addCriterion("state not between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andDatetimeIsNull() {
            addCriterion("dateTime is null");
            return (Criteria) this;
        }

        public Criteria andDatetimeIsNotNull() {
            addCriterion("dateTime is not null");
            return (Criteria) this;
        }

        public Criteria andDatetimeEqualTo(Date value) {
            addCriterionForJDBCDate("dateTime =", value, "datetime");
            return (Criteria) this;
        }

        public Criteria andDatetimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("dateTime <>", value, "datetime");
            return (Criteria) this;
        }

        public Criteria andDatetimeGreaterThan(Date value) {
            addCriterionForJDBCDate("dateTime >", value, "datetime");
            return (Criteria) this;
        }

        public Criteria andDatetimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("dateTime >=", value, "datetime");
            return (Criteria) this;
        }

        public Criteria andDatetimeLessThan(Date value) {
            addCriterionForJDBCDate("dateTime <", value, "datetime");
            return (Criteria) this;
        }

        public Criteria andDatetimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("dateTime <=", value, "datetime");
            return (Criteria) this;
        }

        public Criteria andDatetimeIn(List<Date> values) {
            addCriterionForJDBCDate("dateTime in", values, "datetime");
            return (Criteria) this;
        }

        public Criteria andDatetimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("dateTime not in", values, "datetime");
            return (Criteria) this;
        }

        public Criteria andDatetimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("dateTime between", value1, value2, "datetime");
            return (Criteria) this;
        }

        public Criteria andDatetimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("dateTime not between", value1, value2, "datetime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}