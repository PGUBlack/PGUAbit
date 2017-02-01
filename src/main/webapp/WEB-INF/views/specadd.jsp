<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Spec Adding Form</title>
    <link href="<c:url value='/static/css/bootstrap.css' />" rel="stylesheet"></link>
    <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
</head>

<body>
<div class="generic-container">
    <%@include file="authheader.jsp" %>

    <div class="well lead">Добавление специальности</div>
    <form:form method="POST" modelAttribute="spec" class="form-horizontal">
        <form:input type="hidden" path="id" id="id"/>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-label" for="name">Name</label>
                <div class="col-md-7">
                    <form:input type="text" path="name" id="name" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="name" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-label" for="code">Code</label>
                <div class="col-md-7">
                    <form:input type="text" path="code" id="code" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="code" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-label" for="shifr">Shifr</label>
                    <div class="col-md-7">
                        <form:input type="text" path="shifr" id="shifr" class="form-control input-sm" />
                        <div class="has-error">
                            <form:errors path="shifr" class="help-inline"/>
                        </div>
                    </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-label" for="dir">Dir</label>
                <div class="col-md-7">
                    <form:input type="dir" path="dir" id="dir" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="dir" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-label" for="fis_id">FIS_ID</label>
                <div class="col-md-7">
                    <form:input type="text" path="fis_id" id="fis_id" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="fis_id" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-label" for="o_b">Och_Budget</label>
                <div class="col-md-7">
                    <form:input type="number" path="o_b" id="o_b" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="o_b" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-label" for="o_l">Och_Lgoty</label>
                <div class="col-md-7">
                    <form:input type="number" path="o_l" id="o_l" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="o_l" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-label" for="o_t">Och_Target</label>
                <div class="col-md-7">
                    <form:input type="number" path="o_t" id="o_t" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="o_t" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-label" for="o_d">Och_Dogovor</label>
                <div class="col-md-7">
                    <form:input type="number" path="o_d" id="o_d" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="o_d" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-label" for="z_b">Zaoch_Budget</label>
                <div class="col-md-7">
                    <form:input type="number" path="z_b" id="z_b" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="z_b" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-label" for="z_l">Zaoch_Lgoty</label>
                <div class="col-md-7">
                    <form:input type="number" path="z_l" id="z_l" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="z_l" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-label" for="z_t">Zaoch_Target</label>
                <div class="col-md-7">
                    <form:input type="number" path="z_t" id="z_t" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="z_t" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-label" for="z_d">Zaoch_Dogovor</label>
                <div class="col-md-7">
                    <form:input type="number" path="z_d" id="z_d" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="z_d" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-actions floatRight">
                <c:choose>
                    <c:when test="${edit}">
                        <input type="submit" value="Update" class="btn btn-primary btn-sm"/> or <a href="<c:url value='/specs' />">Cancel</a>
                    </c:when>
                    <c:otherwise>
                        <input type="submit" value="Add" class="btn btn-primary btn-sm"/> or <a href="<c:url value='/specs' />">Cancel</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</div>
</body>
</html>