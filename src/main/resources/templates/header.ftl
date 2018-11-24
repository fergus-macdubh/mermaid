<#setting locale="ru_UA">

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Русалочка</title>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="/css/kanban.css">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" type="text/css" href="/css/jquery-ui.css">
    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/jquery.validate.min.js"></script>
    <script src="/js/bootstrap.min.js"></script>
    <script src="/js/jquery-ui.js"></script>
    <script>
        function replaceComma(element) {
            element.value = element.value.replace(',', '.');
        }

        jQuery.validator.addMethod(
                "commaDotNumber",
                function (value, element) {
                    element.value = value.replace(',', '.');
                    return this.optional(element) || /^-?(\d+|\d*\.\d{1,2})$/.test(element.value);
                },
                "Please specify the correct number format");
    </script>
</head>
<body>
<header class="container-fluid blue-gradient">
    <img id="logo" src="/img/mermaid.png"/>
<#if currentUser.role! == "ROLE_ADMIN"
|| currentUser.role! == "ROLE_OPERATOR"
|| currentUser.role! == "ROLE_PAINTER">
    | <a href="/storage">Склад</a>
</#if>
<#if currentUser.role! == "ROLE_ADMIN"
|| currentUser.role! == "ROLE_OPERATOR">
    | <a href="/product">Товары</a>
</#if>
<#if currentUser.role! == "ROLE_ADMIN"
|| currentUser.role! == "ROLE_OPERATOR"
|| currentUser.role! == "ROLE_SALES">
    | <a href="/order">Заказы</a>
</#if>
<#if currentUser.role! == "ROLE_ADMIN"
|| currentUser.role! == "ROLE_OPERATOR"
|| currentUser.role! == "ROLE_SALES"
|| currentUser.role! == "ROLE_PAINTER">
    | <a href="/kanban">KANBAN</a>
</#if>
<#if currentUser.role! == "ROLE_ADMIN">
    | <a href="/users">Пользователи</a>
</#if>
<#if currentUser.role! == "ROLE_ADMIN">
    | <a href="/clients">Клиенты</a>
</#if>
<#if currentUser.role! == "ROLE_ADMIN">
    | <a href="/teams">Бригады</a>
</#if>
<#if currentUser.role! == "ROLE_ADMIN">
    | <a href="/reports">Отчеты</a>
</#if>
    |
<#if currentUser.role! == "ROLE_ADMIN"
|| currentUser.role! == "ROLE_OPERATOR">
    <a class="btn btn-info" style="color: white; font-weight: bold; height: 1.5em;padding-top: 0" href="/order/add">Новый заказ</a>
</#if>
<#if currentUser.role! == "ROLE_ADMIN">
    <a href="/options" style="float: right; margin-top: 0.5em"><img src="/img/options.png" style="height: 2em"/></a>
</#if>
    <br/><br/>
    Привет, ${currentUser.givenName}! (<a href="https://accounts.google.com/Logout">Выход</a>)
</header>
<div class="container-fluid">