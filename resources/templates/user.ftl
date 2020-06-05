<#import "template.ftl" as layout />

<@layout.mainLayout title="Пользователь ${user.name}">
    <table class="table">
    <thead class="thead-dark">
        <tr>
            <th scope="col">id</th>
            <th scope="col">Дата регистрации</th>
            <th scope="col">Имя</th>
            <th scope="col">Логин</th>
        </tr>
    </thead>
    <tbody>
    <#if user??>
            <tr>
                <td>${user.id}</td>
                <td>${user.date_reg}</td>
                <td>${user.name}</td>
                <td>${user.login}</td>
            </tr>
    <#else>Данных нет</#if>
    </tbody>
    </table>

    <table class="table">
        <thead class="thead-dark">
            <tr>
                <th scope="col">Тариф</th>
                <th scope="col">Дата посл. платежа</th>
                <th scope="col">Адрес</th>
                <th scope="col">Вода ФИО</th>
                <th scope="col">Вода ЛС</th>
                <th scope="col">Газ ФИО</th>
                <th scope="col">Газ ЛС</th>
                <th scope="col">Электр. ФИО</th>
                <th scope="col">Электр. ЛС</th>
            </tr>
        </thead>
        <tbody>
    <#if setting??>
            <tr>
                <td>${setting.tarif}</td>
                <td><#if setting.old_pay_Date??>${setting.old_pay_Date}<#else>Нету</#if></td>
                <td><#if setting.adress??>${setting.adress}<#else>Нету</#if></td>
                <td><#if setting.waterFIO??>${setting.waterFIO}<#else>Нету</#if></td>
                <td><#if setting.waterLS??>${setting.waterLS}<#else>Нету</#if></td>
                <td><#if setting.gasFIO??>${setting.gasFIO}<#else>Нету</#if></td>
                <td><#if setting.gasLS??>${setting.gasLS}<#else>Нету</#if></td>
                <td><#if setting.elecFIO??>${setting.elecFIO}<#else>Нету</#if></td>
                <td><#if setting.elecLS??>${setting.elecLS}<#else>Нету</#if></td>
            </tr>
    <#else>Настроек нет</#if>
    </tbody>
    </table>

    <table class="table">
        <thead class="thead-dark">
            <tr>
                <th scope="col">Id</th>
                <th scope="col">Дата</th>
                <th scope="col">Газ</th>
                <th scope="col">Вода</th>
                <th scope="col">Электр. 1</th>
                <th scope="col">Электр. 2</th>
                <th scope="col">Электр. 3</th>
                <th scope="col">Электр. порог 1</th>
                <th scope="col">Электр. порог 2</th>
            </tr>
        </thead>
        <tbody>
    <#if tarifs??>
        <#list tarifs as tarif>
            <tr>
                <td>${tarif.id}</td>
                <td>${tarif.date}</td>
                <td>${tarif.gas}</td>
                <td>${tarif.water}</td>
                <td>${tarif.elec1}</td>
                <td>${tarif.elec2}</td>
                <td>${tarif.elec3}</td>
                <td>${tarif.elec_level1}</td>
                <td>${tarif.elec_level2}</td>
            </tr>
        </#list>
    <#else>Тарифов нет</#if>

    </tbody>
    </table>
    <div class="container">
        <div class="row">
            <a href="/" class="btn btn-secondary float-right" role="button">Сохранить</a>
        </div>
    </div>
</@layout.mainLayout>