<#-- @ftlvariable name="error" type="java.lang.String" -->
<#-- @ftlvariable name="pageUser" type="model.User" -->

<#import "template.ftl" as layout />

<@layout.mainLayout title="Регистрация">
<form class="pure-form-stacked" action="/register" method="post" enctype="application/x-www-form-urlencoded">
    <#if error??>
        <p class="error">${error}</p>
    </#if>
        <p>
          <label for="name">Имя:</label>
          <input type="text" name="name" id="name" value="fggjfk">
        </p>

        <p>
          <label for="login">Логин:</label>
          <input type="text" name="login" id="login" value="login">
        </p>

        <p>
          <label for="password">Пароль:</label>
          <input type="password" name="password" id="password" value="testtest">
        </p>

        <p class="register-submit">
          <button type="submit" class="register-button">Зарегистрироваться</button>
        </p>
</form>
</@layout.mainLayout>
