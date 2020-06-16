<#macro mainLayout title="Коммуналка">
<!doctype html>
<html>
    <head>
        <title>${title}</title>
        <!-- Required meta tags -->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link rel="stylesheet" type="text/css" href="/styles/main.css">
    </head>
    <body>
        <ul id="navbar">
            <li><a href="/">Главная</a></li>
            <li><a href="/add">Внести показания</a></li>
            <li><a href="/calc">Калькулятор</a></li>
            <li><a href="/grafics">Графики</a></li>
            <li><a href="/invoice">Квитанции</a></li>
            <#if user??>
            <span class="right">
            <li><a href="/user">[${user.name}]</a>
                <ul>
                    <li><a href="/logout">Выйти</a></li>
                </ul>
            </li>
            </span>
            <#else>
                <li><a href="#">Вход?</a>
                    <ul>
                        <li><a href="/login">Войти</a></li>
                        <li><a href="/register">Зарегистрироваться</a></li>
                    </ul>
                </li>
            </#if>
        </ul>
    <div class="container">
        <div class="row m-1">
            <#nested/>
        </div>
    </div>
    </body>
</html>
</#macro>