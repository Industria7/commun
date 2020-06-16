<#-- @ftlvariable name="error" type="java.lang.String" -->
<#-- @ftlvariable name="login" type="java.lang.String" -->

<#import "template.ftl" as layout />

<@layout.mainLayout title="Выберите месяц">
<#if inf??><div style="padding: 5px;text-align: center; border: solid; color=red">${inf}</div></#if>
  <div style="padding: 5px;text-align: center; border: solid">
    <form method="post" action="/add" enctype="application/x-www-form-urlencoded">
      <div style="padding: 5px; border: ridge"><label for="month">Показания за : </label>
        <input type="month" id="month" name="month" required=""/>
      </div>
          <input type="hidden" id="action" name="action" value="month">
      <div style="padding: 5px;text-align: center"><button type="submit">Далее</button></div>
    </form>
  </div>
</@layout.mainLayout>