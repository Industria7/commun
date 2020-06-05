<#-- @ftlvariable name="error" type="java.lang.String" -->
<#-- @ftlvariable name="login" type="java.lang.String" -->

<#import "template.ftl" as layout />

<@layout.mainLayout title="Вход">
  <form method="post" action="/login" class="login" method="post" enctype="application/x-www-form-urlencoded">
      <#if error??>
              <p class="error">${error}</p>
          </#if>
    <p>
      <label for="login">Логин:</label>
      <input type="text" name="login" id="login">
    </p>

    <p>
      <label for="password">Пароль:</label>
      <input type="password" name="password" id="password">
    </p>

    <p class="login-submit">
      <button type="submit" class="login-button">Войти</button>
    </p>
  </form>
</@layout.mainLayout>