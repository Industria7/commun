<#macro mainLayout title="Коммуналка">
<!doctype html>
<html>
    <head>
        <title>${title}</title>
        <!-- Required meta tags -->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/pure/0.6.0/pure-min.css">
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/pure/0.6.0/grids-responsive-min.css">
        <link rel="stylesheet" type="text/css" href="/styles/main.css">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    </head>
    <body>
        <ul id="navbar">
            <li><a href="/">Главная</a></li>
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