<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Русалочка</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="/css/kanban.css">
    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap.min.js"></script>
</head>
<body>
<header class="container-fluid">
    <img id="logo" src="/img/mermaid.png"/>
<#if user.authorities?seq_contains("ROLE_ADMIN")
|| user.authorities?seq_contains("ROLE_OPERATOR")
|| user.authorities?seq_contains("ROLE_PAINTER")>
    | <a href="/storage">Склад</a>
</#if>
<#if user.authorities?seq_contains("ROLE_ADMIN")
|| user.authorities?seq_contains("ROLE_OPERATOR")>
    | <a href="/storage/product/purchase">Пополнить склад</a>
</#if>
<#if user.authorities?seq_contains("ROLE_ADMIN")
|| user.authorities?seq_contains("ROLE_OPERATOR")>
    | <a href="/product">Товары</a>
</#if>
<#if user.authorities?seq_contains("ROLE_ADMIN")
|| user.authorities?seq_contains("ROLE_OPERATOR")>
    | <a href="/product/add">Новый товар</a>
</#if>
<#if user.authorities?seq_contains("ROLE_ADMIN")
|| user.authorities?seq_contains("ROLE_OPERATOR")
|| user.authorities?seq_contains("ROLE_SALES")>
    | <a href="/order">Заказы</a>
</#if>
<#if user.authorities?seq_contains("ROLE_ADMIN")
|| user.authorities?seq_contains("ROLE_OPERATOR")>
    | <a href="/order/add">Новый заказ</a>
</#if>
<#if user.authorities?seq_contains("ROLE_ADMIN")
|| user.authorities?seq_contains("ROLE_OPERATOR")
|| user.authorities?seq_contains("ROLE_SALES")
|| user.authorities?seq_contains("ROLE_PAINTER")>
    | <a href="/kanban">KANBAN</a>
</#if>
    |
    <br/><br/>
    Привет, ${user.givenName}! (<a href="https://accounts.google.com/Logout">Выход</a>)
</header>
<div class="container-fluid">