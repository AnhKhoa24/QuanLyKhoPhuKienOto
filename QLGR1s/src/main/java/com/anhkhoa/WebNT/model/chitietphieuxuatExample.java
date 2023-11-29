package com.anhkhoa.WebNT.model;

import java.util.ArrayList;
import java.util.List;

public class chitietphieuxuatExample {
   
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public chitietphieuxuatExample() {
        oredCriteria = new ArrayList<>();
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
            criteria = new ArrayList<>();
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

        public Criteria andSophieuxuatIsNull() {
            addCriterion("SoPhieuXuat is null");
            return (Criteria) this;
        }

        public Criteria andSophieuxuatIsNotNull() {
            addCriterion("SoPhieuXuat is not null");
            return (Criteria) this;
        }

        public Criteria andSophieuxuatEqualTo(Integer value) {
            addCriterion("SoPhieuXuat =", value, "sophieuxuat");
            return (Criteria) this;
        }

        public Criteria andSophieuxuatNotEqualTo(Integer value) {
            addCriterion("SoPhieuXuat <>", value, "sophieuxuat");
            return (Criteria) this;
        }

        public Criteria andSophieuxuatGreaterThan(Integer value) {
            addCriterion("SoPhieuXuat >", value, "sophieuxuat");
            return (Criteria) this;
        }

        public Criteria andSophieuxuatGreaterThanOrEqualTo(Integer value) {
            addCriterion("SoPhieuXuat >=", value, "sophieuxuat");
            return (Criteria) this;
        }

        public Criteria andSophieuxuatLessThan(Integer value) {
            addCriterion("SoPhieuXuat <", value, "sophieuxuat");
            return (Criteria) this;
        }

        public Criteria andSophieuxuatLessThanOrEqualTo(Integer value) {
            addCriterion("SoPhieuXuat <=", value, "sophieuxuat");
            return (Criteria) this;
        }

        public Criteria andSophieuxuatIn(List<Integer> values) {
            addCriterion("SoPhieuXuat in", values, "sophieuxuat");
            return (Criteria) this;
        }

        public Criteria andSophieuxuatNotIn(List<Integer> values) {
            addCriterion("SoPhieuXuat not in", values, "sophieuxuat");
            return (Criteria) this;
        }

        public Criteria andSophieuxuatBetween(Integer value1, Integer value2) {
            addCriterion("SoPhieuXuat between", value1, value2, "sophieuxuat");
            return (Criteria) this;
        }

        public Criteria andSophieuxuatNotBetween(Integer value1, Integer value2) {
            addCriterion("SoPhieuXuat not between", value1, value2, "sophieuxuat");
            return (Criteria) this;
        }

        public Criteria andMabanthanhphanIsNull() {
            addCriterion("MaBanThanhPhan is null");
            return (Criteria) this;
        }

        public Criteria andMabanthanhphanIsNotNull() {
            addCriterion("MaBanThanhPhan is not null");
            return (Criteria) this;
        }

        public Criteria andMabanthanhphanEqualTo(Integer value) {
            addCriterion("MaBanThanhPhan =", value, "mabanthanhphan");
            return (Criteria) this;
        }

        public Criteria andMabanthanhphanNotEqualTo(Integer value) {
            addCriterion("MaBanThanhPhan <>", value, "mabanthanhphan");
            return (Criteria) this;
        }

        public Criteria andMabanthanhphanGreaterThan(Integer value) {
            addCriterion("MaBanThanhPhan >", value, "mabanthanhphan");
            return (Criteria) this;
        }

        public Criteria andMabanthanhphanGreaterThanOrEqualTo(Integer value) {
            addCriterion("MaBanThanhPhan >=", value, "mabanthanhphan");
            return (Criteria) this;
        }

        public Criteria andMabanthanhphanLessThan(Integer value) {
            addCriterion("MaBanThanhPhan <", value, "mabanthanhphan");
            return (Criteria) this;
        }

        public Criteria andMabanthanhphanLessThanOrEqualTo(Integer value) {
            addCriterion("MaBanThanhPhan <=", value, "mabanthanhphan");
            return (Criteria) this;
        }

        public Criteria andMabanthanhphanIn(List<Integer> values) {
            addCriterion("MaBanThanhPhan in", values, "mabanthanhphan");
            return (Criteria) this;
        }

        public Criteria andMabanthanhphanNotIn(List<Integer> values) {
            addCriterion("MaBanThanhPhan not in", values, "mabanthanhphan");
            return (Criteria) this;
        }

        public Criteria andMabanthanhphanBetween(Integer value1, Integer value2) {
            addCriterion("MaBanThanhPhan between", value1, value2, "mabanthanhphan");
            return (Criteria) this;
        }

        public Criteria andMabanthanhphanNotBetween(Integer value1, Integer value2) {
            addCriterion("MaBanThanhPhan not between", value1, value2, "mabanthanhphan");
            return (Criteria) this;
        }

        public Criteria andSoluongIsNull() {
            addCriterion("SoLuong is null");
            return (Criteria) this;
        }

        public Criteria andSoluongIsNotNull() {
            addCriterion("SoLuong is not null");
            return (Criteria) this;
        }

        public Criteria andSoluongEqualTo(Integer value) {
            addCriterion("SoLuong =", value, "soluong");
            return (Criteria) this;
        }

        public Criteria andSoluongNotEqualTo(Integer value) {
            addCriterion("SoLuong <>", value, "soluong");
            return (Criteria) this;
        }

        public Criteria andSoluongGreaterThan(Integer value) {
            addCriterion("SoLuong >", value, "soluong");
            return (Criteria) this;
        }

        public Criteria andSoluongGreaterThanOrEqualTo(Integer value) {
            addCriterion("SoLuong >=", value, "soluong");
            return (Criteria) this;
        }

        public Criteria andSoluongLessThan(Integer value) {
            addCriterion("SoLuong <", value, "soluong");
            return (Criteria) this;
        }

        public Criteria andSoluongLessThanOrEqualTo(Integer value) {
            addCriterion("SoLuong <=", value, "soluong");
            return (Criteria) this;
        }

        public Criteria andSoluongIn(List<Integer> values) {
            addCriterion("SoLuong in", values, "soluong");
            return (Criteria) this;
        }

        public Criteria andSoluongNotIn(List<Integer> values) {
            addCriterion("SoLuong not in", values, "soluong");
            return (Criteria) this;
        }

        public Criteria andSoluongBetween(Integer value1, Integer value2) {
            addCriterion("SoLuong between", value1, value2, "soluong");
            return (Criteria) this;
        }

        public Criteria andSoluongNotBetween(Integer value1, Integer value2) {
            addCriterion("SoLuong not between", value1, value2, "soluong");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table chitietphieuxuat
     *
     * @mbg.generated do_not_delete_during_merge Mon Nov 06 21:00:07 ICT 2023
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table chitietphieuxuat
     *
     * @mbg.generated Mon Nov 06 21:00:07 ICT 2023
     */
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