<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Русалочка</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="/css/kanban.css">
    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/jquery.validate.min.js"></script>
    <script src="/js/bootstrap.min.js"></script>
    <script>
        function replaceComma(element) {
            element.value = element.value.replace(',', '.');
        }

        jQuery.validator.addMethod(
                "commaDotNumber",
                function (value, element) {
                    element.value = value.replace(',', '.');
                    return this.optional(element) || /^(\d*\.\d{1,2})$/.test(element.value);
                },
                "Please specify the correct number format");
    </script>
</head>
<body>
<header class="container-fluid">
    <img id="logo" src="/img/mermaid.png"/>
<#if user.role! == "ROLE_ADMIN"
|| user.role! == "ROLE_OPERATOR"
|| user.role! == "ROLE_PAINTER">
    | <a href="/storage">Склад</a>
</#if>
<#if user.role! == "ROLE_ADMIN"
|| user.role! == "ROLE_OPERATOR">
    | <a href="/storage/product/purchase">Пополнить склад</a>
</#if>
<#if user.role! == "ROLE_ADMIN"
|| user.role! == "ROLE_OPERATOR">
    | <a href="/product">Товары</a>
</#if>
<#if user.role! == "ROLE_ADMIN"
|| user.role! == "ROLE_OPERATOR">
    | <a href="/product/add">Новый товар</a>
</#if>
<#if user.role! == "ROLE_ADMIN"
|| user.role! == "ROLE_OPERATOR"
|| user.role! == "ROLE_SALES">
    | <a href="/order">Заказы</a>
</#if>
<#if user.role! == "ROLE_ADMIN"
|| user.role! == "ROLE_OPERATOR">
    | <a href="/order/add">Новый заказ</a>
</#if>
<#if user.role! == "ROLE_ADMIN"
|| user.role! == "ROLE_OPERATOR"
|| user.role! == "ROLE_SALES"
|| user.role! == "ROLE_PAINTER">
    | <a href="/kanban">KANBAN</a>
</#if>
<#if user.role! == "ROLE_ADMIN">
    | <a href="/users">Пользователи</a>
</#if>
    |
    <br/><br/>
    Привет, ${user.givenName}! (<a href="https://accounts.google.com/Logout">Выход</a>)
</header>
<div class="container-fluid">