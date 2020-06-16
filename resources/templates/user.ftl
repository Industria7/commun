<#import "template.ftl" as layout />

<@layout.mainLayout title="Пользователь ${user.name}">
    <#if info??>
        <div style="color: red;">${info}</div>
    </#if>
    <div>Ваше имя : <form method="post" action="/user">
        <input type="text" name="name" value="${user.name}" minlength="3" maxlength="15" class="form-control" placeholder="Введите имя" required="" autocomplete="off">
        <input type="hidden" id="action" name="action" value="name">
        <button type="submit">Изменить имя</button>
    </form>
    </div>

    <div><p>Дата регистрации : ${user.date_reg}</p></div>

    <div><p>Дата последнего платежа : <#if setting.old_pay_Date??>${setting.old_pay_Date}<#else>Нету</#if></p>
        <p>Адресс : <#if setting.adress??>${setting.adress}<#else>Нету</#if></p>
        <p>Вода ФИО : <#if setting.waterFIO??>${setting.waterFIO}<#else>Нету</#if></p>
        <p>Вода ЛС : <#if setting.waterLS??>${setting.waterLS}<#else>Нету</#if></p>
        <p>Газ ФИО : <#if setting.gasFIO??>${setting.gasFIO}<#else>Нету</#if></p>
        <p>Газ ЛС : <#if setting.gasLS??>${setting.gasLS}<#else>Нету</#if></p>
        <p>Электричество ФИО : <#if setting.elecFIO??>${setting.elecFIO}<#else>Нету</#if></p>
        <p>Электричество ЛС : <#if setting.elecLS??>${setting.elecLS}<#else>Нету</#if></p>
        </div>

    <div><p>Выбран тариф : ${tarif.date} <a href="./edit"><span>Изменить</span></a></p></div>

    <div>Выбран тариф : <form method="post" action="/user">
        <input type="date" name="name" value="${tarif.date}" required="" autocomplete="off">
        <input type="hidden" id="action" name="action" value="tarif">
        <button type="submit">Выбрать тариф</button>
    </form>
    </div>

    <div><p>Газ : ${tarif.gas}</p></div>
    <div><p>Вода : ${tarif.water}</p></div>

    <div><p>Электричество ур. 1 : ${tarif.elec1}</p></div>
    <div><p>Электричество ур. 2 : ${tarif.elec2}</p></div>
    <div><p>Электричество ур. 3 : ${tarif.elec3}</p></div>

    <div><p>Электричество порог 1 : ${tarif.elec_level1}</p></div>
    <div><p>Электричество порог 2 : ${tarif.elec_level2}</p></div>
    
    <div>
      <label for="country">Страна</label>
      <select name="country">
        <option>Выберите страну проживания</option>
        <option value="1">Россия</option>
        <option value="2">Украина</option>
        <option value="3">Беларусь</option>
      </select> 
      <div class="select-arrow"></div> 
    </div>
</@layout.mainLayout>