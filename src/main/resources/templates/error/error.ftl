<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link rel="stylesheet" type="text/css" href="${style}">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css">
</head>
<body>
<header class="container-fluid">
    <img id="logo" src="${logo}"/>
</header>
<div class="container-fluid">
<h1>Ошибка</h1>
К сожалению, произошла ошибка и в данный момент страница не может быть отображена. Если ошибка повторяется, пожалуйста свяжитесь с разработчиком:
<ul>
    <li>Email: fergusmacdubh@gmail.com</li>
    <li>Skype: fergus_macdubh</li>
</ul>
    Отправьте ему пожалуйста следующую информацию:
    <ul>
        <li>Requested URL: ${url!}</li>
        <li>Messages: <ul>
            <#list messages as message>
            <li>${message!}</li>
            </#list>
        </ul>
        </li>
    </ul>
</div>
</body>
</html>