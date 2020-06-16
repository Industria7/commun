<#-- @ftlvariable name="error" type="java.lang.String" -->
<#-- @ftlvariable name="login" type="java.lang.String" -->

<#import "template.ftl" as layout />

<@layout.mainLayout title="Новые показания">
  <div style="padding: 5px;text-align: center; border: solid">
    <form method="post" action="/add" enctype="application/x-www-form-urlencoded">
      <div style="padding: 5px; border: ridge"><label for="month">Показания за : </label>
        <input type="month" id="month" name="month" <#if month??>value="${month}" readonly="readonly"</#if> required=""/>
      </div>
     <div style="padding: 5px; border: ridge">Выбран тариф за : [${tarif.date}]</div>
      <div style="padding: 5px; border: ridge">Газ<div >Прошлые показания : 12345</div>
        <input type="number" name="gas" value="" maxlength="15" class="form-control" placeholder="Введите показания" required="" autocomplete="off">
      </div>
      <div style="padding: 5px; border: ridge">Вода<div>Прошлые показания : 12345</div>
        <input type="number" name="water" value="" maxlength="15" class="form-control" placeholder="Введите показания" required="" autocomplete="off">
      </div>
      <div style="padding: 5px; border: ridge">Электричество<div>Прошлые показания : 12345</div>
        <input type="number" name="elec" value="" maxlength="15" class="form-control" placeholder="Введите показания" required="" autocomplete="off">
      </div>
      <input type="hidden" id="tarif" name="tarif" value="${setting.tarif}">
          <input type="hidden" id="action" name="action" value="add">
      <div style="padding: 5px;text-align: center"><button type="submit">Внести</button></div>
    </form>
  </div>
</@layout.mainLayout>